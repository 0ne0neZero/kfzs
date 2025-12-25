package com.wishare.finance.domains.bill.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.configure.subject.fo.SubjectLevelJson;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceDetailAndReceiptV;
import com.wishare.finance.apps.model.reconciliation.consts.enums.ClaimTypeEnum;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.configure.subject.SubjectMapRulesAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.GatherBillApproveA;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.support.GatherBillApproveListenerFactory;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.dto.GatherDetailInfo;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.refund.GatherDetailInfoF;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.OverdueStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportGatherBillData;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 应付账单领域
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class GatherBillDomainService {

    private static final Pattern compile = Pattern.compile("[A-Z]");

    private final GatherBillRepository gatherBillRepository;

    private final SubjectMapRulesAppService subjectMapRulesAppService;

    private final GatherDetailRepository gatherDetailRepository;

    private final BillCarryoverRepository carryoverRepository;

    private final BillRefundRepository refundRepository;

    private final BillApproveRepository approveRepository;

    private final BillReverseRepository reverseRepository;

    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;

    private final ReceivableBillRepository receivableBillRepository;

    private final AdvanceBillRepository advanceBillRepository;

    private final SharedBillAppService sharedBillAppService;

    private final ChargeItemRepository chargeItemRepository;

    private final FlowClaimDetailRepository flowClaimDetailRepository;

    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;



    /**
     * 批量新增收款单
     *
     * @param addGatherBillCommands
     * @return
     */
    @Transactional
    public List<GatherBillV> addBatch(List<AddGatherBillCommand> addGatherBillCommands) {
        List<GatherBill> gatherBillList = Lists.newArrayList();
        List<GatherDetail> gatherDetailList = Lists.newArrayList();
        addGatherBillCommands.forEach(command -> {
            GatherBill gatherBill = Global.mapperFacade.map(command, GatherBill.class);
            initPayBill(gatherBill);
            List<AddGatherBillDetailCommand> addGatherBillDetailCommandList = command.getAddGatherBillDetailCommandList();
            for (AddGatherBillDetailCommand addGatherBillDetailCommand : addGatherBillDetailCommandList) {
                GatherDetail gatherDetail = Global.mapperFacade.map(addGatherBillDetailCommand, GatherDetail.class);
                gatherDetail.setRecPayAmount(addGatherBillDetailCommand.getPayAmount());
                gatherDetail.setGatherBillId(gatherBill.getId());
                gatherDetail.setGatherBillNo(gatherBill.getBillNo());
                gatherDetailList.add(gatherDetail);
            }
            gatherBillList.add(gatherBill);
        });
        //自动关联票据
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = null;
        for (GatherBill gatherBill : gatherBillList) {
            invoiceReceiptDetailES = autoBindInvoice(gatherBill);
        }
        gatherBillRepository.saveBatch(gatherBillList);
        gatherDetailRepository.saveBatch(gatherDetailList);
        if (CollectionUtils.isNotEmpty(invoiceReceiptDetailES)){
            invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailES);
        }
        BizLog.normalBatch(gatherBillList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        return Global.mapperFacade.mapAsList(gatherBillList, GatherBillV.class);
    }

    /**
     * 批量同步收款单
     *
     * @param gatherBillList
     * @return
     */
    public Boolean addGatherBatch(List<GatherBill> gatherBillList) {
        return gatherBillRepository.saveBatch(gatherBillList);
    }

    /**
     * 批量同步收款单明细
     *
     * @param gatherDetailList
     * @return
     */
    public Boolean addGatherDetailBatch(List<GatherDetail> gatherDetailList) {
         return gatherDetailRepository.saveBatch(gatherDetailList);
    }

    public List<GatherBill> getByOutBusId(String hncId,String communityId) {
       return gatherBillRepository.getByOutBusId(hncId,communityId);
    }

    /**
     * 自动认领
     * @param gatherBill
     */
    public List<InvoiceReceiptDetailE> autoBindInvoice(GatherBill gatherBill){
        //根据id获取所有的票认领和未认领的票
        List<InvoiceBillDetailDto> invoiceBillDetailDtos = invoiceReceiptDetailRepository.getInvoiceByInvRecUnitId(gatherBill.getOutBusId());
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(invoiceBillDetailDtos)){
            Map<Long, Long> invoiceMap = new HashMap<>(); //票据未认领完成的金额
            for (InvoiceBillDetailDto invoiceBillDetailDto : invoiceBillDetailDtos) {
                Long invoiceId = invoiceBillDetailDto.getId(); //票据id
                //如果票未绑定账，或者这个票的账没有被认领完， 减去被认领的金额
                if (invoiceMap.containsKey(invoiceId) && Objects.nonNull(invoiceBillDetailDto.getBillId())){
                    //剩余开票金额
                    long invoiceAmount = invoiceMap.get(invoiceId) - invoiceBillDetailDto.getInvoiceAmount();
                    if(invoiceAmount > 0){
                        invoiceMap.put(invoiceId, invoiceAmount);
                    }else {
                        invoiceMap.remove(invoiceId);
                    }
                }else {
                    invoiceMap.put(invoiceId, invoiceBillDetailDto.getReceiptAmount());
                }
            }
            if (!invoiceMap.isEmpty()){
                Map<Long, List<InvoiceBillDetailDto>> billDetailMap = invoiceBillDetailDtos.stream().filter(i -> invoiceMap.containsKey(i.getId())).collect(Collectors.groupingBy(InvoiceBillDetailDto::getId));
                //账单的开票金额
                long billInvoiceAmount = 0;
                //账单剩余的开票金额
                long totalBillInvoiceAmount = 0;
                long remBillInvoiceAmount = gatherBill.getTotalAmount();
                for(Map.Entry<Long, Long> entry : invoiceMap.entrySet()){
                    billInvoiceAmount = (remBillInvoiceAmount <= entry.getValue()) ? remBillInvoiceAmount : entry.getValue();
                    remBillInvoiceAmount -= billInvoiceAmount;
                    InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
                    invoiceReceiptDetailE.setInvoiceReceiptId(entry.getKey());
                    invoiceReceiptDetailE.setWithTaxFlag(1);
                    invoiceReceiptDetailE.setTaxRate(null == gatherBill.getTaxRate()? "0.00000" : gatherBill.getTaxRate().toString());
                    invoiceReceiptDetailE.setBillId(gatherBill.getId());
                    invoiceReceiptDetailE.setBillNo(gatherBill.getBillNo());
                    invoiceReceiptDetailE.setBillType(BillTypeEnum.收款单.getCode());
                    invoiceReceiptDetailE.setBillStartTime(gatherBill.getStartTime());
                    invoiceReceiptDetailE.setBillEndTime(gatherBill.getEndTime());
                    invoiceReceiptDetailE.setPriceTaxAmount(billDetailMap.get(entry.getKey()).get(0).getReceiptAmount());
                    invoiceReceiptDetailE.setInvoiceAmount(billInvoiceAmount);
                    invoiceReceiptDetailE.setSettleAmount(gatherBill.getTotalAmount());
                    invoiceReceiptDetailES.add(invoiceReceiptDetailE);
                    totalBillInvoiceAmount += billInvoiceAmount;
                    if (remBillInvoiceAmount <= 0){
                        break;
                    }
                }
                gatherBill.invoice(totalBillInvoiceAmount,true);
            }
        }
        return invoiceReceiptDetailES;
    }


    /**
     * 初始化付款单
     *
     * @param gatherBill 收款单
     */
    private void initPayBill(GatherBill gatherBill) {
        gatherBill.generateIdentifier();
        gatherBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
    }

    /**
     * 分页查询已审核收款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<GatherBillV> getApprovedPage(PageF<SearchF<?>> queryF) {
        queryF.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
        queryF.getConditions().getFields().removeIf(a-> "b.room_id".equals(a.getName()));


        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();

        Field reconcileState = Global.ac.getBean(SpacePermissionAppService.class).getField(queryF, "b.reconcile_state");
        if(Objects.nonNull(reconcileState)){
            queryF.getConditions().getFields().removeIf(a-> "b.reconcile_state".equals(a.getName()));
            //对账状态 已核对(2)/未核对
            if("2".equals(reconcileState.getValue().toString())){
                queryWrapper.and(wrapper -> wrapper.ne("b.reconcile_state", 2) .or().ne("b.mc_reconcile_state",2));
            }else{
                queryWrapper.ne("b.reconcile_state", 2);
                queryWrapper.ne("b.mc_reconcile_state", 2);
            }
        }

        //添加正常账单条件
        queryWrapper.in("b.approved_state", BillApproveStateEnum.已审核.getCode(), BillApproveStateEnum.审核中.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("gd.available", DataDeletedEnum.NORMAL.getCode());
//        queryWrapper.orderByDesc("b.gmt_create");
        normalBillCondition(queryWrapper);
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "gd." + BillSharedingColumn.收款明细.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());

        IPage<GatherBillDto> gatherBillPage = gatherBillRepository.queryPage(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), queryWrapper,gatherBillName,gatherDetailName);
        Integer count = gatherBillRepository.queryPageCount(queryWrapper,gatherBillName,gatherDetailName);
        if(Objects.isNull(count)) {
            count = 0;
        }
        Field supCpUnitIdField = queryF.getConditions().getFields().stream()
            .filter(v -> (("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())) && Objects.nonNull(v.getValue())).findAny().get();
        handleDetail(gatherBillPage.getRecords(), supCpUnitIdField.getValue().toString());
        return PageV.of(queryF.getPageNum(), queryF.getPageSize(), count, Global.mapperFacade.mapAsList(gatherBillPage.getRecords(), GatherBillV.class));
    }

    /**
     * 分页查询收款记录
     * @param queryF 查询参数
     * @return {@link GatherBillV}
     */
    public PageV<GatherBillV> billQueryPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        //添加正常账单条件
        queryWrapper.in("b.approved_state", BillApproveStateEnum.已审核.getCode(), BillApproveStateEnum.审核中.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("b.reversed", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("gd.available", DataDeletedEnum.NORMAL.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "gd." + BillSharedingColumn.收款明细.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        IPage<GatherBillDto> gatherBillPage = gatherBillRepository.queryGatherPage(Page.of(queryF.getPageNum(), queryF.getPageSize(),false), queryWrapper,gatherBillName,gatherDetailName);
        if (CollectionUtils.isNotEmpty(gatherBillPage.getRecords())){
            String supCpUnitId = gatherBillPage.getRecords().get(0).getSupCpUnitId();
            List<Long> gatherBillIds = gatherBillPage.getRecords().stream().map(GatherBillDto::getId).collect(Collectors.toList());
            List<GatherDetail> gatherDetailList = gatherDetailRepository.queryByPayBillIdList(gatherBillIds, supCpUnitId);
            if (CollectionUtils.isNotEmpty(gatherDetailList)) {
                Map<Long, List<GatherDetail>> gatherDetailMap = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
                for (GatherBillDto record : gatherBillPage.getRecords()) {
                    record.setGatherDetails(gatherDetailMap.get(record.getId()));
                }
            }
        }
        Integer count = gatherBillRepository.queryPageCount(queryWrapper,gatherBillName,gatherDetailName);
        if(Objects.isNull(count)) {
            count = 0;
        }
        return PageV.of(queryF.getPageNum(), queryF.getPageSize(), count, Global.mapperFacade.mapAsList(gatherBillPage.getRecords(), GatherBillV.class));
    }


    /**
     * 获取收款明细
     * @param records 收款单数据
     * @param supCpUnitId 上级收费单元id
     */
    private void addDetail(List<GatherBillDto> records, String supCpUnitId) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> gatherBillIds = records.stream().map(GatherBillDto::getId)
                    .collect(Collectors.toList());
            List<GatherDetail> gatherDetailList = gatherDetailRepository.getListByGatherBillIds(
                    gatherBillIds, supCpUnitId);
            Map<Long, List<GatherDetail>> gatherDetailMap = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
            records.forEach(a->{
                List<GatherDetail> details = gatherDetailMap.get(a.getId());
                List<GatherDetail> gatherDetails = Global.mapperFacade.mapAsList(details,
                        GatherDetail.class);
                a.setGatherDetails(gatherDetails);
            });
        }
    }

    /**
     * 分页查询未审核收款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public PageV<GatherBillV> queryNotApprovedPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        //添加正常账单条件
        queryWrapper.ne("b.approved_state", BillApproveStateEnum.已审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        normalBillCondition(queryWrapper);
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "gd." + BillSharedingColumn.收款账单.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());

        IPage<GatherBillDto> gatherBillPage = gatherBillRepository.queryPage(Page.of(queryF.getPageNum(), queryF.getPageSize()), queryWrapper, gatherBillName, gatherDetailName);
        return PageV.of(gatherBillPage.getCurrent(), gatherBillPage.getSize(), gatherBillPage.getTotal(), Global.mapperFacade.mapAsList(gatherBillPage.getRecords(), GatherBillV.class));
    }

    /**
     * 分页查询未审核收款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public PageV<GatherBillV> getPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());

        IPage<GatherBillDto> gatherBillPage = gatherBillRepository.queryPage(Page.of(queryF.getPageNum(), queryF.getPageSize()), queryWrapper, gatherBillName, gatherDetailName);
        return PageV.of(gatherBillPage.getCurrent(), gatherBillPage.getSize(), gatherBillPage.getTotal(), Global.mapperFacade.mapAsList(gatherBillPage.getRecords(), GatherBillV.class));
    }

    /**
     * 分页查询未开票收款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public IPage<UnInvoiceGatherBillDto> unInvoiceGatherBillPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());
        //添加正常账单条件
        queryWrapper.in("b.approved_state", BillApproveStateEnum.已审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("b.invoice_state", InvoiceStateEnum.未开票.getCode());
        queryWrapper.eq("gd.available", DataDeletedEnum.NORMAL.getCode());
        return gatherBillRepository.unInvoiceGatherBillPage(Page.of(queryF.getPageNum(), queryF.getPageSize()), queryWrapper, gatherBillName, gatherDetailName);
    }

    /**
     * 分页补充明细
     *
     * @param records
     * @param records
     */
    private void handleDetail(List<GatherBillDto> records, String supCpUnitId) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> gatherBillIds = records.stream().map(GatherBillDto::getId).collect(Collectors.toList());
            List<GatherDetail> gatherDetailList = gatherDetailRepository.queryByPayBillIdList(gatherBillIds, supCpUnitId);
            if (CollectionUtils.isNotEmpty(gatherDetailList)) {
                List<Long> recBillIds = gatherDetailList.stream().filter(a->a.getGatherType().equals(GatherTypeEnum.应收.getCode()))
                        .map(GatherDetail::getRecBillId).collect(Collectors.toList());
                List<Long> advBillIds = gatherDetailList.stream().filter(a->a.getGatherType().equals(GatherTypeEnum.预收.getCode()))
                        .map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Map<Long, List<GatherDetail>> gatherDetailMap = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
                Map<Long, List<ReceivableBill>> recBillInfoMap = !CollectionUtils.isEmpty(recBillIds)?receivableBillRepository.list(new QueryWrapper<ReceivableBill>().in("id", recBillIds)
                        .eq("sup_cp_unit_id", supCpUnitId).eq("deleted",0)).stream().collect(Collectors.groupingBy(ReceivableBill::getId)):new HashMap<>();
                Map<Long, List<AdvanceBill>> advBillInfoMap = !CollectionUtils.isEmpty(advBillIds)?advanceBillRepository.list(new QueryWrapper<AdvanceBill>().in("id", advBillIds)
                        .eq("sup_cp_unit_id", supCpUnitId).eq("deleted",0)).stream().collect(Collectors.groupingBy(AdvanceBill::getId)):new HashMap<>();
                records.forEach(record -> {
                    List<GatherDetail> details = gatherDetailMap.get(record.getId());
                    if (CollectionUtils.isNotEmpty(details)) {
                        for (GatherDetail gatherDetail : details) {
                            // 获取对应账单的部分信息，账单类型与是否为违约金
                            if (gatherDetail.getGatherType().equals(GatherTypeEnum.应收.getCode())){
                                List<ReceivableBill> receivableBillList = recBillInfoMap.get(gatherDetail.getRecBillId());
                                if (CollectionUtils.isEmpty(receivableBillList)){continue;}
                                ReceivableBill receivableBill = receivableBillList.get(0);
                                gatherDetail.setIsDefault(Optional.ofNullable(receivableBill).map(ReceivableBill::getOverdue).orElse(OverdueStateEnum.无违约金.getCode()));
                                gatherDetail.setIsDefaultName(Optional.ofNullable(gatherDetail.getIsDefault()).map(a->OverdueStateEnum.valueOfByCode(a).getValue()).orElse(null));
                                gatherDetail.setBillType(Optional.ofNullable(receivableBill).map(ReceivableBill::getBillType).orElse(null));
                                gatherDetail.setBillTypeName(Optional.ofNullable(gatherDetail.getBillType()).map(a->BillTypeEnum.valueOfByCode(a).getValue()).orElse(null));
                                gatherDetail.setBillMethod(receivableBill.getBillMethod());
                                gatherDetail.setUnitPrice(receivableBill.getUnitPrice());
                                gatherDetail.setReceivableDate(receivableBill.getReceivableDate());
                            }else {
                                List<AdvanceBill> advBillList = advBillInfoMap.get(gatherDetail.getRecBillId());
                                if (CollectionUtils.isEmpty(advBillList)){continue;}
                                AdvanceBill advanceBill = advBillList.get(0);
                                gatherDetail.setIsDefault(OverdueStateEnum.无违约金.getCode());
                                gatherDetail.setIsDefaultName(Optional.ofNullable(gatherDetail.getIsDefault()).map(a->OverdueStateEnum.valueOfByCode(a).getValue()).orElse(null));
                                gatherDetail.setBillType(BillTypeEnum.预收账单.getCode());
                                gatherDetail.setBillTypeName(BillTypeEnum.预收账单.getValue());
                                gatherDetail.setBillMethod(advanceBill.getBillMethod());
                                gatherDetail.setUnitPrice(advanceBill.getUnitPrice());
                            }
                            gatherDetail.setStatutoryBodyId(record.getStatutoryBodyId());
                            gatherDetail.setSysSource(record.getSysSource());
                            gatherDetail.setState(record.getState());
                            gatherDetail.setVerifyState(record.getVerifyState());
                            gatherDetail.setApprovedState(record.getApprovedState());
                            gatherDetail.setReversed(record.getReversed());
                            gatherDetail.setTotal(details.size());

                            gatherDetail.setPayWayName(Optional.ofNullable(gatherDetail.getPayWay()).map(a->SettleWayEnum.valueOfByCode(a).getValue()).orElse(null));
                            gatherDetail.setPayInfosString(Optional.ofNullable(record.getPaySource())
                                            .map(PaySourceEnum::valueOfByCode)
                                            .map(paySourceEnum -> paySourceEnum + "-" + SettleChannelEnum.valueNameOfByCode(gatherDetail.getPayChannel()))
                                            .orElse(SettleChannelEnum.valueNameOfByCode(gatherDetail.getPayChannel()))
                            );
                            if (TenantUtil.bf64() && PaySourceEnum.业主端app.getCode().equals(record.getPaySource())){
                                gatherDetail.setPayInfosString(Optional.of(record.getPaySource())
                                        .map(PaySourceEnum::valueOfByCode).get().getValue());
                            }
                            gatherDetail.setStatutoryBodyName(record.getStatutoryBodyName());
                            SubjectLevelJson subjectInfo = subjectMapRulesAppService.getByUnitId(gatherDetail.getChargeItemId());
                            gatherDetail.setSubjectName(Objects.nonNull(subjectInfo)?subjectInfo.getCashFlowName():null);
                        }
                        record.setGatherDetails(details);
                        // 重新设置收款单房号
                        String roomNameUnionString = details.stream()
                                .map(GatherDetail::getCpUnitName)
                                .filter(Objects::nonNull)  // 过滤掉null值
                                .distinct()
                                .filter(cpUnitName -> !cpUnitName.isEmpty())
                                .collect(Collectors.joining(","));
                        record.setCpUnitName(roomNameUnionString);
                        record.setPayWayName(Optional.ofNullable(record.getPayWay()).map(a->SettleWayEnum.valueOfByCode(a).getValue()).orElse(null));
                    }
                    Integer paySource = record.getPaySource();
                    record.setPayInfosString(Optional.ofNullable(paySource)
                                    .map(PaySourceEnum::valueOfByCode)
                                    .map(paySourceEnum -> paySourceEnum + "-" + SettleChannelEnum.valueNameOfByCode(record.getPayChannel()))
                                    .orElse(SettleChannelEnum.valueNameOfByCode(record.getPayChannel())));
                    if (TenantUtil.bf64() && PaySourceEnum.业主端app.getCode().equals(record.getPaySource())){
                        record.setPayInfosString(Optional.of(record.getPaySource())
                                .map(PaySourceEnum::valueOfByCode).get().getValue());
                    }
                });
            }
        }
    }

    /**
     * 查询收款单信息
     *
     * @param gatherBillId 收款单id
     * @return GatherBill
     */
    public GatherBillDto queryById(Long gatherBillId, String supCpUnitId) {
        String gatherBillName = sharedBillAppService.getShareTableName(supCpUnitId,
            TableNames.GATHER_BILL);
        String gatherDetailName = sharedBillAppService.getShareTableName(supCpUnitId,
            TableNames.GATHER_DETAIL);
        //收款单(gather_bill)、gather_detail
        GatherBillDto gatherBillDto = gatherBillRepository.queryById(gatherBillId, gatherBillName, gatherDetailName);
        GatherBillDto dto = new GatherBillDto();
        if (Objects.nonNull(gatherBillDto)) {
            //收款单详情表(gather_detail)
            List<GatherDetail> gatherDetails = gatherDetailRepository.queryByGatherBillId(gatherBillId,supCpUnitId);
            gatherBillDto.setGatherDetails(gatherDetails);
            return gatherBillDto;
        } else {
            //预收账单没有收款单表,但是交易记录中需要查询交易明细 收款单详情表(gather_detail)
            List<GatherDetail> gatherDetails = gatherDetailRepository.queryByGatherBillId(gatherBillId,supCpUnitId);
            List<String> gatherBillNoList = gatherDetails.stream().map(GatherDetail::getGatherBillNo).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(gatherDetails)) {
                GatherDetail gatherDetail = gatherDetails.get(0);
                //远洋移动端，有个预收统一入账接口，一次入账outPayNo会相同
                String outPayNo = gatherDetail.getOutPayNo();
                if (StringUtils.isNotEmpty(outPayNo)) {
                    //收款单详情表(gather_detail)
                    gatherDetails = gatherDetailRepository.queryByOutPayNo(outPayNo, supCpUnitId);
                    gatherBillNoList = gatherDetails.stream().map(GatherDetail::getGatherBillNo).distinct().collect(Collectors.toList());
                }
                dto.setGmtCreate(gatherDetail.getGmtCreate());
                dto.setPayTime(gatherDetail.getPayTime());
                dto.setTradeNo(gatherDetail.getOutPayNo());
                dto.setPayChannel(gatherDetail.getPayChannel());
                dto.setPayWay(gatherDetail.getPayWay());
            }
            dto.setId(null);
            dto.setBillNo(StringUtils.join(gatherBillNoList,","));
            dto.setDescription(null);
            dto.setGatherDetails(gatherDetails);
            return dto;
        }
    }

    /**
     * 根据id集合获取收款单
     *
     * @param gatherBillIdList 收款单id集合
     * @return List
     */
    public List<GatherBillV> queryByIdList(List<Long> gatherBillIdList, String supCpUnitId) {
        List<GatherBillDto> gatherBillDtoList = gatherBillRepository.queryByIdList(gatherBillIdList, supCpUnitId);
        if (CollectionUtils.isNotEmpty(gatherBillDtoList)) {
            List<GatherDetail> gatherDetailList = gatherDetailRepository.queryByPayBillIdList(gatherBillIdList, supCpUnitId);
            Map<Long, List<GatherDetail>> collect = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
            for (GatherBillDto gatherBillDto : gatherBillDtoList) {
                gatherBillDto.setGatherDetails(collect.get(gatherBillDto.getId()));
            }
        }
        return Global.mapperFacade.mapAsList(gatherBillDtoList, GatherBillV.class);
    }

    /**
     * 查询收款单详情(包含明细)
     *
     * @param gatherBillId 收款单id
     * @return GatherBillDetailV
     */
    public GatherBillDetailV queryDetailById(Long gatherBillId, String supCpUnitId) {
        GatherBillDto gatherBill = queryById(gatherBillId, supCpUnitId);
        GatherBillDetailV gatherBillDetailV = Global.mapperFacade.map(gatherBill, GatherBillDetailV.class);
        if (Objects.nonNull(gatherBill)) {
            //账单结转记录表(bill_carryover)
            gatherBillDetailV.setCarryovers(Global.mapperFacade.mapAsList(carryoverRepository.listByCarriedBillId(gatherBillId), BillCarryoverV.class));
            //退款单表(bill_refund)
            gatherBillDetailV.setRefunds(Global.mapperFacade.mapAsList(refundRepository.listByBillId(gatherBillId), BillRefundV.class));
        }
        return gatherBillDetailV;
    }

    /**
     * 删除收款单
     *
     * @param gatherBillId 收款单id
     * @return Boolean
     */
    public Boolean delete(Long gatherBillId) {
        return gatherBillRepository.removeById(gatherBillId);
    }

    /**
     * 统计收款单信息
     *
     * @param queryF 统计条件
     * @return BillTotalDto
     */
    public BillTotalDto queryTotal(StatisticsBillTotalQuery queryF) {
        QueryWrapper<?> wrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(queryF.getBillIds())) {
            wrapper = new QueryWrapper<>().in("b.id", queryF.getBillIds());
        } else {
            PageF<SearchF<?>> query = queryF.getQuery();
            query.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
            wrapper = query.getConditions().getQueryModel();
        }
        //无效账单条件
        Integer billInvalid = queryF.getBillInvalid();
        if (Objects.nonNull(billInvalid) && billInvalid == 1) {
            invalidBillCondition(wrapper);
        } else {
            normalBillCondition(wrapper);
        }
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        wrapper.in("b.approved_state", BillApproveStateEnum.已审核.getCode(), BillApproveStateEnum.审核中.getCode());;
        wrapper.eq("gd.available", DataDeletedEnum.NORMAL.getCode());

        a: if(queryF!=null&&queryF.getQuery()!=null
                &&queryF.getQuery().getConditions()!=null&&CollectionUtils.isNotEmpty(queryF.getQuery().getConditions().getFields())){
            List<Field> fields = queryF.getQuery().getConditions().getFields();
            for (Field field : fields) {
                if (StringUtils.isNotBlank(field.getName())&&field.getName().contains("gd.")){
                    break a;
                }
                Map<String, Object> map = field.getMap();
                if(MapUtils.isNotEmpty(map)) {
                    for(String key : map.keySet()){
                        if(key.contains("gd.")){
                            break a;
                        }
                    }
                }
            }
            queryF.getQuery().getConditions().getFields().add(new Field("b." + BillSharedingColumn.收款账单.getColumnName(),  queryF.getSupCpUnitId(), 1));
            queryF.getQuery().getConditions().getFields().add(new Field("gd." + BillSharedingColumn.收款明细.getColumnName(),  queryF.getSupCpUnitId(), 1));
            String gatherBillName = sharedBillAppService.getShareTableName(queryF.getQuery(), TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
            String gatherDetailName = sharedBillAppService.getShareTableName(queryF.getQuery(), TableNames.GATHER_DETAIL, "gd." + BillSharedingColumn.收款明细.getColumnName());
            wrapper.eq("b." + BillSharedingColumn.收款账单.getColumnName(), queryF.getSupCpUnitId());
            return gatherBillRepository.queryTotalNew(wrapper,gatherBillName,gatherDetailName);
        }
        wrapper.eq("gd.available", DataDeletedEnum.NORMAL.getCode());
        wrapper.eq("b." + BillSharedingColumn.收款账单.getColumnName(), queryF.getSupCpUnitId());
        wrapper.eq("gd." + BillSharedingColumn.收款明细.getColumnName(), queryF.getSupCpUnitId());
        return gatherBillRepository.queryTotal(wrapper);
    }

    /**
     * 查询历史缴费记录
     *
     * @param pageF 查询入参
     * @return List
     */
    public Page<PayListDto> payList(PayListF pageF) {
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize());
        QueryWrapper<?> queryWrapper = getQueryWrapper(pageF.getCommunityId(), pageF.getChargeItemId(), pageF.getRoomId(), pageF.getTargetObjId(), pageF.getOrderBy());
        return gatherBillRepository.payList(page, queryWrapper);
    }

    /**
     * 查询开票收款单列表
     *
     * @param pageF 查询入参
     * @return List
     */
    public Page<PayListDto> payInvoiceList(PayInvoiceListF pageF) {
        Page<Object> page = Page.of(pageF.getPageNum(), pageF.getPageSize(),false);
        QueryWrapper<?> queryWrapper = getQueryWrapper(pageF);
        //手动算出分表表名
        String gatherBillTableName = sharedBillAppService.getShareTableName(pageF.getCommunityId(), TableNames.GATHER_BILL);
        String gatherDetailTableName = sharedBillAppService.getShareTableName(pageF.getCommunityId(), TableNames.GATHER_DETAIL);
        return gatherBillRepository.payInvoiceList(page, queryWrapper, gatherBillTableName, gatherDetailTableName);
    }


    /**
     * 无效账单条件
     */
    private void invalidBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper.and(wrapper -> wrapper.eq("b.state", BillStateEnum.作废.getCode())
                .or().eq("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .or().eq("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .or().eq("b.refund_state", BillRefundStateEnum.已退款.getCode()));
    }

    /**
     * 正常账单条件
     */
    private void normalBillCondition(QueryWrapper<?> queryWrapper) {
        queryWrapper.ne("b.state", BillStateEnum.作废.getCode())
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode());
//                .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
//                .ne("b.refund_state", BillRefundStateEnum.已退款.getCode());
    }

    public QueryWrapper<?> getQueryWrapper(String communityId, String chargeItemId, String roomId, String targetObjId, List<OrderBy> orderBy) {
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(communityId)) {
            queryWrapper.eq("b.sup_cp_unit_id", communityId);
            queryWrapper.eq("gd.sup_cp_unit_id", communityId);
        }
        if (StringUtils.isNotEmpty(chargeItemId)) {
            queryWrapper.eq("gd.charge_item_id", chargeItemId);
        }
        if (StringUtils.isNotEmpty(roomId)) {
            queryWrapper.eq("gd.cp_unit_id", roomId);
        }
        if (StringUtils.isNotEmpty(targetObjId)) {
            queryWrapper.eq("gd.payer_id", targetObjId);
        }
        if (CollectionUtils.isNotEmpty(orderBy)) {
            for (OrderBy order : orderBy) {
                if (Objects.nonNull(order) && StringUtils.isNotEmpty(order.getField())) {
                    String field = order.getField();
                    Matcher matcher = compile.matcher(field);
                    StringBuilder sb = new StringBuilder();
                    while (matcher.find()) {
                        matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                    }
                    matcher.appendTail(sb);
                    queryWrapper.orderBy(Boolean.TRUE, order.isAsc(), sb.toString());
                }
            }
        } else {
            queryWrapper.orderByAsc("gd.pay_time");
        }
        queryWrapper.eq("gd.deleted", 0);
        queryWrapper.groupBy("b.id");
        return queryWrapper;
    }

    public QueryWrapper<?> getQueryWrapper(PayInvoiceListF pageF) {
        QueryWrapper<?> queryWrapper = new QueryWrapper<>();
        List<String> communityIds = pageF.getCommunityIds();
        String communityId = pageF.getCommunityId();
        String chargeItemId = pageF.getChargeItemId();
        String roomId = pageF.getRoomId();
        String targetObjId = pageF.getTargetObjId();
        List<OrderBy> orderBy = pageF.getOrderBy();
        List<String> payChannelList = pageF.getPayChannels();
        List<Integer> invoiceState = pageF.getInvoiceStates();
        List<String> targetObjIds = pageF.getTargetObjIds();
        List<String> roomIds = pageF.getRoomIds();
        LocalDateTime payStartTime = pageF.getPayStartTime();
        LocalDateTime payEndTime = pageF.getPayEndTime();

        if (StringUtils.isNotEmpty(communityId)) {
            queryWrapper.eq("gd.sup_cp_unit_id", communityId);
        }
        if (CollectionUtils.isNotEmpty(communityIds)) {
            queryWrapper.in("gd.sup_cp_unit_id", communityIds);
        }
        if (StringUtils.isNotEmpty(chargeItemId)) {
            queryWrapper.eq("gd.charge_item_id", chargeItemId);
        }
        if (StringUtils.isNotEmpty(roomId)) {
            queryWrapper.like("gd.cp_unit_id", roomId);
        }
        if (StringUtils.isNotEmpty(targetObjId)) {
            queryWrapper.eq("gd.payer_id", targetObjId);
        }
        if(CollectionUtils.isNotEmpty(roomIds)){
            queryWrapper.in("gd.cp_unit_id", roomIds);
        }
        if(CollectionUtils.isNotEmpty(targetObjIds)){
            queryWrapper.in("gd.payer_id", targetObjIds);
        }
        if(CollectionUtils.isNotEmpty(payChannelList)){
            queryWrapper.in("gd.pay_channel", payChannelList);
        }
        if(CollectionUtils.isNotEmpty(invoiceState)){
            queryWrapper.in("b.invoice_state", invoiceState);
        }
        if(Objects.nonNull(payStartTime)){
            queryWrapper.ge("gd.pay_time",payStartTime);
        }
        if(Objects.nonNull(payEndTime)){
            queryWrapper.le("gd.pay_time",payEndTime);
        }

        queryWrapper.eq("gd.deleted", 0);
        return queryWrapper;
    }

    /**
     * 通过账单id和开票状态获取收款单记录
     *
     * @param billId
     * @return
     */
    public List<GatherBill> getGatherBillByInvoiceState(Long billId, List<Integer> invoiceStateList, String supCpUnitId) {
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(billId, supCpUnitId);
        if (CollectionUtils.isNotEmpty(gatherDetails)) {
            //收款单ids
            List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(gatherBillIds)) {
                List<GatherBill> gatherBills = gatherBillRepository.listByIdsAndInvoiceState(gatherBillIds, invoiceStateList, supCpUnitId);
                if (CollectionUtils.isNotEmpty(gatherBills)) {
                    return gatherBills;
                }
            }
        }
        return null;
    }

    /**
     * 通过账单id和结算状态
     *
     * @param recBillId
     * @return
     */
    public List<GatherBill> getGatherBills(Long recBillId, String supCpUnitId) {
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(recBillId, supCpUnitId);
        if (CollectionUtils.isNotEmpty(gatherDetails)) {
            //收款单ids
            List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(gatherBillIds)) {
                return gatherBillRepository.getGatherBill(gatherBillIds, supCpUnitId);
            }
        }
        return null;
    }

    /**
     * 收款单开票直接设置收款单开票状态
     * @param gatherBillId
     * @return
     */
    public List<GatherBill> gatherBillFinishInvoice(Long gatherBillId,String supCpUnitId) {
        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id",gatherBillId).eq("sup_cp_unit_id",supCpUnitId));
        //开票金额（单位：分）
        gatherBill.setInvoiceAmount(gatherBill.getTotalAmount());
        //开票状态：0未开票，1开票中，2部分开票，3已开票
        gatherBill.setInvoiceState(BillInvoiceStateEnum.已开票.getCode());
        return Lists.newArrayList(gatherBill);
    }

    /**
     * 根据收款单ids和开票金额完成开票
     *
     * @param gatherBillEList
     * @param billId
     * @param invoiceAmountAll
     * @return
     */
    public List<GatherBill> gatherBillFinishInvoice(List<GatherBill> gatherBillEList, Long billId, Long invoiceAmountAll, String supCpUnitId) {
        List<GatherBill> gatherBills = null;
        if (CollectionUtils.isEmpty(gatherBillEList)) {
            gatherBills = this.getGatherBillByInvoiceState(billId, Lists.newArrayList(BillInvoiceStateEnum.未开票.getCode(), BillInvoiceStateEnum.部分开票.getCode()),supCpUnitId);
        }else {
            gatherBills = gatherBillEList;
        }
        if (CollectionUtils.isNotEmpty(gatherBills)) {
            for (GatherBill gatherBill : gatherBills) {
                //当前付款单的需开票金额
                Long invoiceAmountNeed = gatherBill.getTotalAmount() - gatherBill.getRefundAmount() - gatherBill.getCarriedAmount();
                //可开票金额
                Long invoiceAmountCan = invoiceAmountNeed - gatherBill.getInvoiceAmount();
                //处理票据金额
                if (invoiceAmountAll.longValue() <= invoiceAmountCan.longValue()) {
                    /**开票金额（单位：分）*/
                    gatherBill.setInvoiceAmount(gatherBill.getInvoiceAmount() + invoiceAmountAll);
                } else {
                    /**开票金额（单位：分）*/
                    gatherBill.setInvoiceAmount(gatherBill.getInvoiceAmount() + invoiceAmountCan);
                    invoiceAmountAll = invoiceAmountAll - invoiceAmountCan;
                }
                /**开票状态：0未开票，1开票中，2部分开票，3已开票*/
                gatherBill.setInvoiceState(this.handleInvoiceState(gatherBill.getInvoiceAmount(), invoiceAmountNeed));
            }
            return gatherBills;
        }
        return null;
    }

    /**
     * 根据收款单ids和开票金额 批量作废红冲开票金额
     *
     * @param billId
     * @param voidRedAmountAll
     * @return
     */
    public List<GatherBill> voidBatch(List<GatherBill> gatherBillEList, Long billId, Long voidRedAmountAll, String supCpUnitId) {
        List<GatherBill> gatherBills;
        if (CollectionUtils.isEmpty(gatherBillEList)) {
            gatherBills = this.getGatherBillByInvoiceState(billId, Lists.newArrayList(BillInvoiceStateEnum.已开票.getCode(), BillInvoiceStateEnum.部分开票.getCode()), supCpUnitId);
        }else {
            gatherBills = gatherBillEList;
        }
        if (CollectionUtils.isNotEmpty(gatherBills)) {
            for (GatherBill gatherBill : gatherBills) {
                //当前付款单的需开票金额
                Long invoiceAmountNeed = gatherBill.getTotalAmount() - gatherBill.getRefundAmount() - gatherBill.getCarriedAmount();

                //已经开票金额
                Long invoiceAmount = gatherBill.getInvoiceAmount();
                if (invoiceAmount.longValue() >= voidRedAmountAll.longValue()) {
                    /**开票金额（单位：分）*/
                    gatherBill.setInvoiceAmount(invoiceAmount.longValue() - voidRedAmountAll.longValue());
                } else {
                    /**开票金额（单位：分）*/
                    gatherBill.setInvoiceAmount(0L);
                    voidRedAmountAll = voidRedAmountAll - invoiceAmount;
                }
                /** 开票状态：0未开票，1开票中，2部分开票，3已开票 */
                gatherBill.setInvoiceState(handleInvoiceState(gatherBill.getInvoiceAmount(), invoiceAmountNeed));
            }
            return gatherBills;
        }
        return null;
    }

    /**
     * 收款单退款
     *
     * @param recBillId
     * @param refundAmountAll
     */
    public List<GatherBill> refundAmount(Long recBillId, Long refundAmountAll, BillApproveE billApprove) {
        if(StringUtils.isBlank(billApprove.getSupCpUnitId())) {
            throw new IllegalArgumentException("必传supCpUnitId !");
        }
        List<GatherBill> gatherBills = this.getGatherBills(recBillId, billApprove.getSupCpUnitId());
        long remRefundAmount = refundAmountAll.longValue();
        if (CollectionUtils.isNotEmpty(gatherBills)) {
            for (GatherBill gatherBill : gatherBills) {
                //账单实收金额
                Long actualPayAmount = gatherBill.getTotalAmount() - gatherBill.getRefundAmount() - gatherBill.getCarriedAmount();
                //应缴总金额
                Long actualUnpayAmount = gatherBill.getTotalAmount();
                if (remRefundAmount <= actualPayAmount) {
                    gatherBill.setRefundAmount(gatherBill.getRefundAmount() + remRefundAmount);
                    saveGatherRefund(gatherBill, refundAmountAll, billApprove);
                    break;
                } else {
                    gatherBill.setRefundAmount(gatherBill.getRefundAmount() + actualPayAmount);
                    saveGatherRefund(gatherBill, actualPayAmount, billApprove);
                    remRefundAmount -= actualPayAmount;
                }
                gatherBill.setRefundState(handleRefundState(gatherBill.getRefundAmount(), actualUnpayAmount));
                //日志记录
                BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.退款,
                        new Content().option(new ContentOption(new PlainTextDataItem("退款金额为：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(refundAmountAll), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            }
        }
        return gatherBills;
    }

    /**
     * 保存收款单
     */
    private void saveGatherRefund(GatherBill gatherBill, Long refundAmount, BillApproveE billApprove) {
        BillRefundE billRefundE = new BillRefundE();
        billRefundE.setBillId(gatherBill.getId());
        billRefundE.setBillType(BillTypeEnum.收款单.getCode());
        billRefundE.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefundE.setRefundChannel(SettleChannelEnum.其他.getCode());
        billRefundE.setRefundWay(SettleWayEnum.线上.getCode());
        billRefundE.setRefundAmount(refundAmount);
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setBillApproveId(billApprove.getId());
        billRefundE.setApproveTime(LocalDateTime.now());
        billRefundE.setRefunderId(gatherBill.getPayerId());
        billRefundE.setRefunderName(gatherBill.getPayerName());
        billRefundE.setState(RefundStateEnum.已退款.getCode());
        billRefundE.setChargeStartTime(gatherBill.getStartTime());
        billRefundE.setChargeEndTime(gatherBill.getEndTime());
        billRefundE.setRemark(billRefundE.getRemark());
        billRefundE.setInferenceState(BillInferStateEnum.未推凭.getCode());
        Global.ac.getBean(BillRefundRepository.class).save(billRefundE);
    }

    /**
     * 付款单退款
     *
     * @param gatherBillId
     * @param refundAmountAll
     */
    public GatherBill refundAmountByGatherBillId(Long gatherBillId, Long refundAmountAll) {
        GatherBill gatherBill = gatherBillRepository.getById(gatherBillId);
        //账单实收金额
        Long actualPayAmount = gatherBill.getTotalAmount() - gatherBill.getRefundAmount() - gatherBill.getCarriedAmount();
        //应缴总金额
        Long actualUnpayAmount = gatherBill.getTotalAmount();
        if (refundAmountAll.longValue() < actualPayAmount) {
            gatherBill.setRefundAmount(gatherBill.getRefundAmount() + refundAmountAll);
        } else {
            gatherBill.setRefundAmount(gatherBill.getRefundAmount() + actualPayAmount);
        }
        gatherBill.setRefundState(handleRefundState(gatherBill.getRefundAmount(), actualUnpayAmount));
        return gatherBill;
    }

    /**
     * @return
     */
    private Integer handleRefundState(Long refundAmount, Long actualUnpayAmount) {
        if (refundAmount.longValue() == 0) {
            return BillRefundStateEnum.未退款.getCode();
        } else if (refundAmount.longValue() < actualUnpayAmount.longValue()) {
            return BillRefundStateEnum.部分退款.getCode();
        } else if (refundAmount.longValue() == actualUnpayAmount.longValue()) {
            return BillRefundStateEnum.已退款.getCode();
        }
        return null;
    }

    /**
     * 根据可开票金额和当前总开票金额判断开票状态
     *
     * @param invoiceAmount     开票金额
     * @param invoiceAmountNeed 当前需要开票金额
     * @return
     */
    public Integer handleInvoiceState(Long invoiceAmount, Long invoiceAmountNeed) {
        //处理开票状态
        if (invoiceAmount.longValue() == 0) {
            return InvoiceStateEnum.未开票.getCode();
        } else if (invoiceAmount.longValue() < invoiceAmountNeed.longValue()) {
            return InvoiceStateEnum.部分开票.getCode();
        } else if (invoiceAmount.longValue() == invoiceAmountNeed.longValue()) {
            return InvoiceStateEnum.已开票.getCode();
        }
        return null;
    }


    /**
     * 导出收款单
     *
     * @param queryF   查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "gd." + BillSharedingColumn.收款明细.getColumnName());
        //获取导出账单
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        queryWrapper.ne("b.approved_state", BillApproveStateEnum.待审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        normalBillCondition(queryWrapper);
        String fileName = "收款单";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportGatherBillData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportGatherBillData> resultList;
            while (pageNumber <= totalPage) {
                String gatherBillName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
                String gatherDetailName = sharedBillAppService.getShareTableName(queryF, TableNames.GATHER_DETAIL, "b." + BillSharedingColumn.收款明细.getColumnName());

                IPage<GatherBillDto> gatherBillDtoPage = gatherBillRepository.queryPage(Page.of(pageNumber, pageSize), queryWrapper, gatherBillName, gatherDetailName);
                resultList = Global.mapperFacade.mapAsList(gatherBillDtoPage.getRecords(), ExportGatherBillData.class);
                if (CollectionUtils.isEmpty(resultList)) {
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet, writeTable);
                totalPage = gatherBillDtoPage.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取账单推凭信息
     *
     * @param page
     * @param eventType
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceInfo(PageF<SearchF<BillInferenceV>> page, Integer eventType, String supCpUnitId) {
        SearchF<BillInferenceV> searchF = new SearchF<>();
        searchF.setFields(new ArrayList<>());
        QueryWrapper<BillInferenceV> queryModel = searchF.getQueryModel();
        if (page.getConditions().getFields() != null && !page.getConditions().getFields().isEmpty()) {
            for (Field field : page.getConditions().getFields()) {
                switch (field.getMethod()) {
                    case 1:
                        queryModel.eq(field.getName(), field.getValue());
                        break;
                    case 6:
                        queryModel.le(field.getName(), field.getValue());
                        break;
                    case 4:
                        queryModel.ge(field.getName(), field.getValue());
                        break;
                    case 15:
                        queryModel.in(field.getName(), (List) field.getValue());
                        break;
                    case 16:
                        queryModel.notIn(field.getName(), (List) field.getValue());
                        break;
                    default:
                        break;
                }
            }
        }
        queryModel.eq("b.deleted", 0);
        queryModel.eq("bd.deleted", 0);
        queryModel.eq("b.sup_cp_unit_id", supCpUnitId);
        queryModel.eq("bd.sup_cp_unit_id", supCpUnitId);
        if (EventTypeEnum.收款结算.getEvent() == eventType) {
            queryModel.eq("bd.inference_state", 0);
        } else if (EventTypeEnum.冲销作废.getEvent() == eventType) {
            queryModel.eq("bd.inference_state", 1);
            return gatherBillRepository.pageBillInferenceOffInfo(page, queryModel);
        }
        return gatherBillRepository.pageBillInferenceInfo(page, queryModel);
    }

    /**
     * 修改详情的推凭状态
     *
     * @param concatIds
     * @param state
     */
    public void batchUpdateDetailInferenceSate(List<Long> concatIds, int state) {
        gatherDetailRepository.batchUpdateDetailInferenceSate(concatIds, state);
    }

    /**
     * 获取账单推凭信息
     *
     * @param billInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF) {
        if (BillActionEventEnum.结算.getCode() == billInferenceF.getActionEventCode()) {
            return gatherDetailRepository.listInferenceInfoByIdAndInfer(billInferenceF.getBillId(), 0, billInferenceF.getSupCpUnitId());
        }
        return gatherDetailRepository.listInferenceInfoByIdAndInfer(billInferenceF.getBillId(), 1, billInferenceF.getSupCpUnitId());
    }

    /**
     * 获取账单推凭信息
     *
     * @param batchBillInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF) {
        if (BillActionEventEnum.结算.getCode() == batchBillInferenceF.getActionEventCode()) {
            return gatherDetailRepository.listInferenceInfoByIdsAndInfer(batchBillInferenceF.getBillIds(), 0, batchBillInferenceF.getSupCpUnitId());
        }
        return gatherDetailRepository.listInferenceInfoByIdsAndInfer(batchBillInferenceF.getBillIds(), 1, batchBillInferenceF.getSupCpUnitId());
    }

    /**
     * 发送审核申请
     *
     * @param applyInfo
     * @return
     */
    @Transactional
    public Long apply(BillApplyCommand applyInfo) {
        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id",applyInfo.getBillId())
                .eq("sup_cp_unit_id", applyInfo.getSupCpUnitId()));
        ErrorAssertUtil.notNullThrow300(gatherBill, ErrorMessage.BILL_NOT_EXIST);
        verifyInvoiceState(gatherBill.getId(), applyInfo.getSupCpUnitId());
        //获取审核中的记录
        BillApproveE approvingE = approveRepository.getApprovingAByBillId(applyInfo.getBillId(), applyInfo.getSupCpUnitId());
        if (BillApproveStateEnum.待审核.equalsByCode(gatherBill.getApprovedState())) {
            //账单未审核进行调整，判断是否审核过（处理反审的情况，将上次审核状态设置为已审核）
            if (approveRepository.hasApproved(applyInfo.getBillId(), applyInfo.getSupCpUnitId())) {
                applyInfo.setLastApprovedState(BillApproveStateEnum.已审核.getCode());
            }
        }
        GatherBillApproveA approvingA = new GatherBillApproveA(gatherBill).init(approvingE);
        approvingA.apply(applyInfo);
        gatherBillRepository.update(approvingA.getGatherBill(), new QueryWrapper<GatherBill>()
                .eq("id", approvingA.getGatherBill().getId())
                .eq("sup_cp_unit_id", approvingA.getGatherBill().getSupCpUnitId()));
        approveRepository.save(approvingA);
        return approvingA.getApproveE().getId();
    }

    public List<GatherDetailInfoF> queryGatherDetail(List<Long> gatherBillIds,String supCpUnitId){
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GatherDetail::getGatherBillId,gatherBillIds)
                .eq(GatherDetail::getSupCpUnitId,supCpUnitId);
        List<GatherDetail> list = gatherDetailRepository.list(wrapper);
        List<GatherDetailInfoF>  gatherDetailInfoFS = list.stream().map(gatherDetail -> {
            GatherDetailInfoF detailInfoF = new GatherDetailInfoF();
            BeanUtil.copyProperties(gatherDetail, detailInfoF);
            detailInfoF.setGatherDetailId(gatherDetail.getId());
            return detailInfoF;
        }).collect(Collectors.toList());
        //List<GatherDetailInfoF> gatherDetailInfoFS = Global.mapperFacade.mapAsList(list, GatherDetailInfoF.class);
        return gatherDetailInfoFS;
    }

    /**
     * 发送批量审核申请
     *
     * @param param 申请参数
     * @return {@link List} 审批表ID集合
     */
    @Transactional
    public List<Long> applyBatch(BillApplyBatchCommand<?> param) {
        // 校验收款单信息
        List<GatherBill> gatherBillList = gatherBillRepository.list(new QueryWrapper<GatherBill>().in("id",param.getBillIds())
                .eq("sup_cp_unit_id", param.getSupCpUnitId()));
        Assert.validate(() -> CollectionUtils.isNotEmpty(gatherBillList), ()-> BizException.throw400("未能查询到相关收款单哦，请稍后重试"));
        // 获取关联审批数据
        List<BillApproveE> billApproveList = approveRepository.getApprovingByBillIdList(param.getBillIds(), param.getSupCpUnitId());
        Map<Long, List<BillApproveE>> collect = billApproveList.stream().collect(Collectors.groupingBy(BillApproveE::getBillId));
        Map<Long, Object> detailMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(param.getDetails())) {
            detailMap = param.getDetails().stream()
                    .collect(Collectors.toMap(BillDetailBase::getBillId, Function.identity()));
        }
        boolean apply = false;
        List<Long> result = new ArrayList<>();
        for (GatherBill gatherBill : gatherBillList) {
            // 对每笔收款单进行封装处理
            List<BillApproveE> approveList = collect.get(gatherBill.getId());
            GatherBillApproveA approvingA = new GatherBillApproveA(gatherBill).init(CollectionUtils.isNotEmpty(approveList) ?
                    approveList.get(0) : null);
            BillApplyCommand applyCommand = new BillApplyCommand(gatherBill.getId(),
                    param.getReason(),
                    param.getApproveOperateType(),
                    param.getOutApproveId(), param.getExtField1(),
                    detailMap.get(gatherBill.getId()),
                    param.getSupCpUnitId(),
                    param.getOperationRemark());
            if (BillApproveStateEnum.待审核.equalsByCode(gatherBill.getApprovedState())) {
                //收款单未审核进行调整，判断是否审核过（处理反审的情况，将上次审核状态设置为已审核）
                if (approveRepository.hasApproved(gatherBill.getId(), gatherBill.getSupCpUnitId())) {
                    applyCommand.setLastApprovedState(BillApproveStateEnum.已审核.getCode());
                }
            }
            apply = approvingA.apply(applyCommand);
            gatherBillRepository.update(approvingA.getGatherBill(), new QueryWrapper<GatherBill>()
                    .eq("id", approvingA.getGatherBill().getId())
                    .eq("sup_cp_unit_id", approvingA.getGatherBill().getSupCpUnitId()));
            approveRepository.save(approvingA);
            result.add(approvingA.getApproveE().getId());
        }
        return result;
    }


    /**
     * 验证收款单开票状态
     * @param gatherBillId
     */
    public void verifyInvoiceState(Long gatherBillId, String supCpUnitId) {
        List<GatherDetail> gatherDetails = gatherDetailRepository.queryByGatherBillId(gatherBillId, supCpUnitId);
        gatherDetails.stream().filter(gatherDetail -> gatherDetail.getRecBillId() != null).forEach(gatherDetail -> {
            if (gatherDetail.getGatherType().equals(GatherTypeEnum.应收.getCode())) {
                ReceivableBill receivableBill = receivableBillRepository.getOne(new QueryWrapper<ReceivableBill>().eq("id",gatherDetail.getRecBillId())
                        .eq("sup_cp_unit_id",supCpUnitId));
                Integer invoiceState = receivableBill.getInvoiceState();
                ErrorAssertUtil.isFalseThrow402(InvoiceStateEnum.开票中.equalsByCode(invoiceState), ErrorMessage.BILL_IS_OPERATING, InvoiceStateEnum.valueOfByCode(invoiceState));
            } else if (gatherDetail.getGatherType().equals(GatherTypeEnum.预收.getCode())) {
                AdvanceBill advanceBill = advanceBillRepository.getById(gatherDetail.getRecBillId());
                Integer invoiceState = advanceBill.getInvoiceState();
                ErrorAssertUtil.isFalseThrow402(InvoiceStateEnum.开票中.equalsByCode(invoiceState), ErrorMessage.BILL_IS_OPERATING, InvoiceStateEnum.valueOfByCode(invoiceState));
            }
        });
    }

    /**
     * 审核应收账单
     *
     * @param command
     * @return
     */
    public Boolean approve(ApproveCommand command) {
        if(StringUtils.isBlank(command.getSupCpUnitId())) {
            throw new IllegalArgumentException("处理收款单,必须传入上级收费单元ID（supCpUnitId）字段!");
        }
        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>()
            .eq("id", command.getBillId()).eq("sup_cp_unit_id", command.getSupCpUnitId()));
        ErrorAssertUtil.notNullThrow300(gatherBill, ErrorMessage.BILL_NOT_EXIST);
        BillApproveE approvingE = approveRepository.getApprovingAByBillId(command.getBillId(), command.getSupCpUnitId());

        GatherBillApproveA approvingA = new GatherBillApproveA(gatherBill).init(approvingE);
        if (Objects.isNull(approvingE)) {
            approvingA.initDefaultApprove(); //默认生成审核
            approveRepository.save(approvingA);
        }
        approvingA.setApprovedAction(command.getApprovedAction());
        approvingA.setChargeItemId(command.getChargeItemId());
        approvingA.setChargeItemName(command.getChargeItemName());
        boolean approved = approvingA.approve(BillApproveStateEnum.valueOfByCode(command.getApproveState()), command.getRejectReason(),
                GatherBillApproveListenerFactory.getListener(BillApproveOperateTypeEnum.valueOfByCode(approvingA.getOperateType())));
        if (approved) {
            gatherBillRepository.update(approvingA.getGatherBill(), new QueryWrapper<GatherBill>()
                    .eq("id", approvingA.getGatherBill().getId())
                    .eq("sup_cp_unit_id", approvingA.getGatherBill().getSupCpUnitId()));
            approveRepository.update(approvingA, new QueryWrapper<BillApproveE>()
                    .eq("id", approvingA.getId())
                    .eq("sup_cp_unit_id", approvingA.getSupCpUnitId()));
        }
        return approved;
    }


    /**
     * 批量审核收款单
     * @param command 审核结果
     * @param billType 账单类型 == 7
     * @return
     */
    public Boolean approveBatch(BatchApproveBillCommand command, int billType) {
        // 校验数据
        Assert.validate(() -> StringUtils.isNotBlank(command.getSupCpUnitId()), ()-> BizException.throw400("处理收款单,必须传入上级收费单元ID（supCpUnitId）字段!"));
        List<GatherBill> gatherBillList = gatherBillRepository.list(new QueryWrapper<GatherBill>()
                .in("id", command.getBillIds()).eq("sup_cp_unit_id", command.getSupCpUnitId()));
        Assert.validate(() -> CollectionUtils.isNotEmpty(gatherBillList), ()-> BizException.throw400("未能查询到相关收款单哦，请稍后重试"));

        // 审核收款单处理
        for (GatherBill gatherBill : gatherBillList) {
            // 将可能存在的明细退款信息传递
            gatherBill.setGatherMap(command.getGatherMap());
            // 查询收款单对应的审核记录
            BillApproveE approvingE = approveRepository.getApprovingAByBillId(gatherBill.getId(), command.getSupCpUnitId());
            GatherBillApproveA approvingA = new GatherBillApproveA(gatherBill).init(approvingE);
            if (Objects.isNull(approvingE)) {
                approvingA.initDefaultApprove(); //默认生成审核
                approveRepository.save(approvingA);
            }
            boolean approved = approvingA.approve(command.getApproveState(), command.getRejectReason(),
                    GatherBillApproveListenerFactory.getListener(BillApproveOperateTypeEnum.valueOfByCode(approvingA.getOperateType())));
            if (approved) {
                gatherBillRepository.update(approvingA.getGatherBill(), new QueryWrapper<GatherBill>()
                        .eq("id", approvingA.getGatherBill().getId())
                        .eq("sup_cp_unit_id", approvingA.getGatherBill().getSupCpUnitId()));

                approveRepository.update(approvingA, new UpdateWrapper<BillApproveE>()
                        .eq("sup_cp_unit_id", approvingA.getSupCpUnitId()).eq("id", approvingA.getId()));
            }
        }
        return true;
    }

    /**
     * 作废收款单
     *
     * @param command
     * @return
     */
    public Boolean invalid(InvalidCommand command) {
        ErrorAssertUtil.notEmptyThrow400(command.getSupCpUnitId(), ErrorMessage.SUP_CP_UNIT_ID_NOT_EXIST);

        GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>().eq("id",command.getBillId()).eq("sup_cp_unit_id",command.getSupCpUnitId()));

        gatherBill.invalid();
        boolean flag = gatherBillRepository.update(gatherBill , new UpdateWrapper<GatherBill>().eq("id", gatherBill.getId())
                .eq(StringUtils.isNotBlank(gatherBill.getSupCpUnitId()), "sup_cp_unit_id", gatherBill.getSupCpUnitId()));
        //日志记录
        BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.作废,
                new Content().option(new PlainTextDataItem("作废原因:")).option(new PlainTextDataItem(command.getReason())));
        return flag;
    }

    /**
     * 处理账单相关其他mq相关业务
     *
     * @param bill        账单id
     * @param operateType 操作类型
     */
    private void handleOtherMq(GatherBill bill, Integer operateType) {
        BillApproveOperateTypeEnum billApproveOperateTypeEnum = BillApproveOperateTypeEnum.valueOfByCode(operateType);
        if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.作废.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.INVALIDED)
                    .payload(BillActionEvent.invalid(bill.getId(), BillTypeEnum.收款单.getCode(), "作废", bill.getTenantId()))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.冲销.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.REVERSED)
                    .payload(BillActionEvent.reverse(bill.getId(), BillTypeEnum.收款单.getCode(), "冲销", bill.getSupCpUnitId()))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.调整.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.ADJUSTED)
                    .payload(BillActionEvent.adjust(bill.getId(), BillTypeEnum.收款单.getCode(), "调整", bill.getSupCpUnitId()))
                    .build());
        } else if (billApproveOperateTypeEnum.equalsByCode(BillApproveOperateTypeEnum.生成审核.getCode())) {
            EventLifecycle.apply(EventMessage.builder()
                    .headers("action", BillAction.APPROVED)
                    .payload(BillActionEvent.approved(bill.getId(), BillTypeEnum.收款单.getCode(), "审核(计提)", bill.getSupCpUnitId()))
                    .build());
        }
    }

    /**
     * 处理数据
     *
     * @return
     */
    public Boolean handleData(String supCpUnitId) {
        List<GatherBill> list = gatherBillRepository.list(new QueryWrapper<GatherBill>().eq("sup_cp_unit_id", supCpUnitId));
        List<Long> gatherBillIds = list.stream().map(GatherBill::getId).collect(Collectors.toList());

        QueryWrapper<GatherDetail> wrapper = new QueryWrapper<>();
        wrapper.notIn(CollectionUtils.isNotEmpty(gatherBillIds),"gather_bill_id", gatherBillIds);
        wrapper.eq("gather_type", GatherTypeEnum.应收.getCode());
        wrapper.eq("sup_cp_unit_id", supCpUnitId);
        long count = gatherDetailRepository.count(wrapper);
        int current = 1;
        int Size = 10000;
        while (true) {
            Page<GatherDetail> page = gatherDetailRepository.page(Page.of(current++, Size), wrapper);
            if (CollectionUtils.isNotEmpty(page.getRecords())) {
                List<GatherDetail> gatherDetails = page.getRecords();
                List<GatherBill> gatherBillList = Lists.newArrayList();
                gatherDetails.forEach(detail -> {
                    GatherBill gatherBill = new GatherBill();
                    gatherBill.setId(detail.getGatherBillId());
                    gatherBill.setBillNo(detail.getGatherBillNo());
                    gatherBill.setSupCpUnitId(supCpUnitId);
                    gatherBill.setStatutoryBodyId(Long.valueOf(detail.getPayeeId()));
                    gatherBill.setStatutoryBodyName(detail.getPayeeName());
                    gatherBill.setPayTime(detail.getGmtCreate());
                    gatherBill.setPayChannel(detail.getPayChannel());
                    gatherBill.setPayWay(detail.getPayWay());
                    gatherBill.setDiscounts(Lists.newArrayList());
                    gatherBill.setCurrency("CNY");
                    gatherBill.setTotalAmount(detail.getPayAmount());
                    gatherBill.setPayeeId(detail.getPayeeId());
                    gatherBill.setPayeeName(detail.getPayeeName());
                    gatherBill.setSysSource(1);
                    gatherBill.setApprovedState(2);
                    gatherBill.setTenantId(detail.getTenantId());
                    gatherBill.setCreator(detail.getCreator());
                    gatherBill.setCreatorName(detail.getCreatorName());
                    gatherBill.setGmtCreate(detail.getGmtCreate());
                    gatherBill.setOperator(detail.getOperator());
                    gatherBill.setOperatorName(detail.getOperatorName());
                    gatherBill.setGmtModify(detail.getGmtModify());
                    gatherBillList.add(gatherBill);
                });
                gatherBillRepository.saveBatch(gatherBillList);
            } else {
                break;
            }
        }
        return true;
    }

    /**
     * 入账
     * @param enterCommands
     * @return
     */
    public List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId) {
        return new ArrayList<>();
    }



    /**
     * 获取应收账账单
     *
     * @param receivableBillIds
     * @return
     */
    private List<ReceivableBill> getReceivableList(List<Long> receivableBillIds,String supCpUnitId) {
        //根据收费结束时间和创建时间排序
        return getReceivableBills(receivableBillIds,supCpUnitId);
    }

    public static List<ReceivableBill> getReceivableBills(List<Long> receivableBillIds,String supCpUnitId) {
        QueryWrapper<ReceivableBill> wrapper = new QueryWrapper<>();
        wrapper.in("id", receivableBillIds);
        wrapper.eq("sup_cp_unit_id", supCpUnitId);
        wrapper.orderByDesc("end_time","gmt_create");
        List<ReceivableBill> receivableBillList = Global.ac.getBean(ReceivableBillRepository.class).list(wrapper);
        return receivableBillList;
    }

    /**
     * 根据预收单ids获取预收单
     *
     * @param advanceIds
     * @return
     */
    private List<AdvanceBill> getAdvanceList(List<Long> advanceIds) {
        //根据收费结束时间和创建时间排序
        return getAdvanceBills(advanceIds);
    }

    public static List<AdvanceBill> getAdvanceBills(List<Long> advanceIds) {
        LambdaQueryWrapper<AdvanceBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AdvanceBill::getId, advanceIds);
        wrapper.orderByDesc(AdvanceBill::getEndTime,AdvanceBill::getGmtCreate);
        List<AdvanceBill> advanceBillList = Global.ac.getBean(AdvanceBillRepository.class).list(wrapper);
        return advanceBillList;
    }

    public Page<GatherDto> queryPageGatherBillIgnore(PageF<SearchF<?>> pageF) {
        PageQueryUtils.validQueryContainsFieldAndValue(pageF, "b." + BillSharedingColumn.收款账单.getColumnName());
        Field supCpUnitIdField = pageF.getConditions().getFields().stream()
            .filter(v -> (("b." + BillSharedingColumn.收款账单.getColumnName()).equals(v.getName())) && Objects.nonNull(v.getValue())).findAny().get();;
        String supCpUnitId = supCpUnitIdField.getValue().toString();
            Page<GatherDto> gatherDtoPage = gatherBillRepository.queryPageGatherBillIgnore(pageF);
        if (CollectionUtils.isNotEmpty(gatherDtoPage.getRecords())){
            List<GatherDto> records = gatherDtoPage.getRecords();
            for (GatherDto record : records) {
                List<GatherDetail> gatherDetails = gatherDetailRepository.queryByGatherBillIdIgnore(record.getId(), supCpUnitId);
                record.setGatherDetails(gatherDetails);
            }
        }
        return gatherDtoPage;
    }

    public Page<GatherDetailV> queryPageGatherDetail(PageF<SearchF<?>> pageF) {
        PageQueryUtils.validQueryContainsFieldAndValue(pageF, "b." + BillSharedingColumn.收款明细.getColumnName());
        Page<GatherDetailV> result = gatherDetailRepository.queryPageGatherDetail(pageF);
        return result;
    }

    /**
     * 收款单其他账户代收
     */
    public Boolean collect(GatherCollectF gatherCollectF) {
        UpdateWrapper<GatherBill> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", gatherCollectF.getGatherBillId());
        updateWrapper.set("collect_bank_account", gatherCollectF.getCollectBankAccount());
        updateWrapper.set("collect_serial_number", gatherCollectF.getCollectSerialNumber());
        updateWrapper.set("sup_cp_unit_id", gatherCollectF.getSupCpUnitId());
        return gatherBillRepository.update(updateWrapper);
    }

    public List<GetPaySourceByBillNoV> getPaySourceByBillNo(GetPaySourceByBillNoF getPaySourceByBillNoF) {
        QueryWrapper<GatherDetail> wrapper = new QueryWrapper<>();
        wrapper.in("rec_bill_no", getPaySourceByBillNoF.getBillNos());
        wrapper.eq("deleted", 0);
        wrapper.eq("sup_cp_unit_id",getPaySourceByBillNoF.getSupCpUnitId());
        wrapper.orderByDesc("pay_time");
        List<GatherDetail> list = gatherDetailRepository.list(wrapper);
        List<GetPaySourceByBillNoV> returnList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            for (GatherDetail gatherDetail : list) {
                GetPaySourceByBillNoV getPaySourceByBillNoV = new GetPaySourceByBillNoV();
                getPaySourceByBillNoV.setPayTime(gatherDetail.getPayTime());
                getPaySourceByBillNoV.setPayWay(gatherDetail.getPayWay());
                getPaySourceByBillNoV.setPayChannel(SettleChannelEnum.valueNameOfByCode(gatherDetail.getPayChannel()));
                getPaySourceByBillNoV.setBillNo(gatherDetail.getRecBillNo());
                returnList.add(getPaySourceByBillNoV);
            }
        }
        return returnList;
    }

    /**
     * 收款单审核失败更新状态
     * @param gatherApplyUpdateF gatherApplyUpdateF
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean updateApplyInfo(GatherApplyUpdateF gatherApplyUpdateF) {
        List<GatherBill> gatherBills = gatherBillRepository.list(new QueryWrapper<GatherBill>().in("id",gatherApplyUpdateF.getGatherBillIds())
                .eq("sup_cp_unit_id",gatherApplyUpdateF.getSupCpUnitId()));
        if (CollectionUtils.isEmpty(gatherBills)){return false;}
        gatherBills.forEach(x->{
            x.setApprovedState(BillApproveStateEnum.已审核.getCode());
            gatherBillRepository.update(x, new UpdateWrapper<GatherBill>(). eq("id", x.getId()).eq("sup_cp_unit_id", x.getSupCpUnitId()));
        });
        // 更新对应审核表状态
        approveRepository.update(new UpdateWrapper<BillApproveE>().set("approved_state",BillApproveStateEnum.未通过.getCode()).in("bill_id", gatherApplyUpdateF.getGatherBillIds())
                .eq("sup_cp_unit_id", gatherApplyUpdateF.getSupCpUnitId()).in("approved_state",List.of(BillApproveStateEnum.待审核.getCode(),BillApproveStateEnum.审核中.getCode()))
                .in("operate_type", List.of(OperateTypeEnum.收款单退款.getCode(),OperateTypeEnum.收款单冲销.getCode())));
        if (Objects.nonNull(gatherApplyUpdateF.getOprType()) && OperateTypeEnum.收款单冲销.getCode().equals(gatherApplyUpdateF.getOprType())){
            // 将关联的冲销记录标识
            reverseRepository.update(new UpdateWrapper<BillReverseE>().set("deleted",0).in("bill_id",gatherApplyUpdateF.getGatherBillIds()).eq("state",0));
        }
        approveBill(gatherApplyUpdateF.getGatherBillIds(),gatherApplyUpdateF.getSupCpUnitId(),BillApproveStateEnum.已审核.getCode());
        return true;
    }

    /**
     * 将收款单关联的账单置为已经审核
     * @param supCpUnitId 上级收费单元ID
     * @param approveState 变更状态值
     */
    private void approveBill(List<Long> gatherBillIds,String supCpUnitId,Integer approveState){
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getByGatherBillIds(gatherBillIds, supCpUnitId);
        if (CollectionUtils.isNotEmpty(detailList)) {
            Map<Integer, List<GatherDetail>> gatherTypeMap = detailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherType));
            List<GatherDetail> advList = gatherTypeMap.get(GatherTypeEnum.预收.getCode());
            List<GatherDetail> recList = gatherTypeMap.get(GatherTypeEnum.应收.getCode());
            if (CollectionUtils.isNotEmpty(recList)){
                List<Long> billIds = recList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(ReceivableBillRepository.class).update(new UpdateWrapper<ReceivableBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("approved_state",approveState));
            }else if (CollectionUtils.isNotEmpty(advList)){
                List<Long> billIds = advList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(AdvanceBillRepository.class).update(new UpdateWrapper<AdvanceBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("approved_state",approveState));
            }
        }
    }

    /**
     * 发起开票
     * 根据收款单ids 修改收款单信息为开票中 [invoiceState = 1]
     * @param gatherBillIds
     * @return
     */
    public Boolean gatherBillInvoiceBatch(List<Long> gatherBillIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return gatherBillRepository.gatherBillInvoiceBatch(gatherBillIds,supCpUnitId, billIdsMap);
    }

    /**
     * 发起开票
     * 根据收款明细ids 修改收款明细信息为开票中 [invoiceState = 1]
     * @param gatherDetailIds
     * @return
     */
    public Boolean gatherDetailInvoiceBatch(List<Long> gatherDetailIds,String supCpUnitId, Map<Long,Integer> billIdsMap) {
        return gatherDetailRepository.gatherDetailInvoiceBatch(gatherDetailIds,supCpUnitId, billIdsMap);
    }

    /**
     * 更新收款单及明细开票状态
     */
    public void updateGatherInvoiceStatus(String supCpUnitId){
        List<InvoiceDetailAndReceiptV> defaultInvoiceDetailList = new ArrayList<>();
        List<InvoiceDetailAndReceiptV> invoiceDetailList = invoiceReceiptDetailRepository.queryInvoiceDetailList(supCpUnitId,null);
        Map<Long, List<InvoiceDetailAndReceiptV>> detailListMaps = invoiceDetailList.stream().collect(Collectors.groupingBy(InvoiceDetailAndReceiptV::getBillId));
        List<GatherDetail> allGatherDetails = new ArrayList<>();
        List<InvoiceReceiptDetailE> allInvoiceDetailList = new ArrayList<>();
        for (Map.Entry<Long, List<InvoiceDetailAndReceiptV>> map : detailListMaps.entrySet() ){
            List<InvoiceDetailAndReceiptV> detailList = map.getValue();
            Long sumInvoiceAmount = detailList.stream().mapToLong(InvoiceDetailAndReceiptV::getInvoiceAmount).sum();
            //若结果等于0 说明此票已红冲
            if(0L == sumInvoiceAmount){
                defaultInvoiceDetailList.addAll(detailList);
                continue;
            }
            for(InvoiceDetailAndReceiptV detailAndReceiptV : detailList){
                if(InvoiceReceiptStateEnum.开票成功.getCode() == detailAndReceiptV.getState() && detailAndReceiptV.getInvoiceAmount() > 0L){
                    LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
                    if(StringUtils.isBlank(detailAndReceiptV.getCommunityId())){
                        continue;
                    }
                    wrapper.eq(GatherDetail::getRecBillId,detailAndReceiptV.getBillId());
                    wrapper.eq(GatherDetail::getSupCpUnitId,detailAndReceiptV.getCommunityId());
                    wrapper.ne(GatherDetail::getInvoiceState,InvoiceStateEnum.已开票.getCode());
                    List<GatherDetail> gatherDetails = gatherDetailRepository.list(wrapper);
                    if(CollectionUtils.isEmpty(gatherDetails)){
                        defaultInvoiceDetailList.add(detailAndReceiptV);
                        continue;
                    }
                    for (GatherDetail gatherDetail : gatherDetails){
                        Long targetAmount = detailAndReceiptV.getInvoiceAmount();
                        long gatherAmount = gatherDetail.getCanInvoiceAmount();
                        if(targetAmount.compareTo(gatherAmount) == 0){
                            detailAndReceiptV.setGatherBillId(gatherDetail.getGatherBillId());
                            detailAndReceiptV.setGatherBillNo(gatherDetail.getGatherBillNo());
                            detailAndReceiptV.setGatherDetailId(gatherDetail.getId());
                            gatherDetail.setInvoiceState(InvoiceStateEnum.已开票.getCode());
                            allGatherDetails.add(gatherDetail);
                            allInvoiceDetailList.add(detailAndReceiptV);
                            gatherDetails.remove(gatherDetail);
                            break;
                        }
                        int index = gatherDetails.indexOf(gatherDetail);
                        if(index < gatherDetails.size() -1){
                            GatherDetail nexGatherDetail = gatherDetails.get(index + 1);
                            Long nexGatherAmount = nexGatherDetail.getCanInvoiceAmount();
                            Long gatherAmountSum = gatherAmount + nexGatherAmount;
                            if(targetAmount.compareTo(gatherAmountSum) == 0){
                                InvoiceReceiptDetailE receiptDetailE = Global.mapperFacade.map(detailAndReceiptV, InvoiceReceiptDetailE.class);
                                detailAndReceiptV.setGatherBillId(gatherDetail.getGatherBillId());
                                detailAndReceiptV.setGatherBillNo(gatherDetail.getGatherBillNo());
                                detailAndReceiptV.setGatherDetailId(gatherDetail.getId());
                                detailAndReceiptV.setInvoiceAmount(gatherAmount);
                                detailAndReceiptV.setPriceTaxAmount(gatherAmount);
                                nexGatherDetail.setInvoiceState(InvoiceStateEnum.已开票.getCode());
                                gatherDetail.setInvoiceState(InvoiceStateEnum.已开票.getCode());
                                allGatherDetails.add(gatherDetail);
                                allGatherDetails.add(nexGatherDetail);
                                allInvoiceDetailList.add(detailAndReceiptV);
                                gatherDetails.remove(gatherDetail);
                                gatherDetails.remove(nexGatherDetail);
                                receiptDetailE.setGatherBillId(nexGatherDetail.getGatherBillId());
                                receiptDetailE.setGatherBillNo(nexGatherDetail.getGatherBillNo());
                                receiptDetailE.setGatherDetailId(nexGatherDetail.getId());
                                receiptDetailE.setInvoiceAmount(nexGatherAmount);
                                receiptDetailE.setPriceTaxAmount(nexGatherAmount);
                                receiptDetailE.setSettleAmount(receiptDetailE.getSettleAmount() - gatherAmount);
                                receiptDetailE.setId(null);
                                receiptDetailE.generateIdentifier();
                                invoiceReceiptDetailRepository.save(receiptDetailE);
                                break;
                            }
                        }
                    }
                }else {
                    defaultInvoiceDetailList.add(detailAndReceiptV);
                }
            }
        }
        //更新收款单信息
        allGatherDetails.forEach(gatherDetail -> gatherDetailRepository.update(gatherDetail, new UpdateWrapper<GatherDetail>().eq("id",gatherDetail.getId()).eq("sup_cp_unit_id",gatherDetail.getSupCpUnitId())));
        //更新开票明细信息
        allInvoiceDetailList.forEach(detail -> invoiceReceiptDetailRepository.update(detail, new UpdateWrapper<InvoiceReceiptDetailE>().eq("id",detail.getId())));
        //未收款开票及红冲后设置默认值123456789
        defaultInvoiceDetailList.forEach(detail ->{
            detail.setGatherDetailId(123456789L);
            invoiceReceiptDetailRepository.update(detail, new UpdateWrapper<InvoiceReceiptDetailE>().eq("id",detail.getId()));
        });

    }

    /**
     * 根据收款明细id集合获取收款信息列表
     *
     * @param gatherDetailIds 收款明细id集合
     * @return List
     */
    public List<GatherDetailV> gatherDetailList(List<Long> gatherDetailIds, String supCpUnitId) {
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GatherDetail::getId, gatherDetailIds)
                .eq(GatherDetail::getSupCpUnitId, supCpUnitId);
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(wrapper);
        List<GatherDetailV> gatherDetailVS = Global.mapperFacade.mapAsList(gatherDetails, GatherDetailV.class);
        Map<Integer, List<GatherDetailV>> groupMaps = gatherDetailVS.stream().collect(Collectors.groupingBy(GatherDetailV::getGatherType));
        return fallGatherDetails(supCpUnitId, gatherDetailVS, groupMaps);
    }

    private List<GatherDetailV> fallGatherDetails(String supCpUnitId, List<GatherDetailV> gatherDetailVS, Map<Integer, List<GatherDetailV>> groupMaps) {
        gatherDetailVS.clear();
        groupMaps.forEach((k, v) -> {
            List<Long> billId = v.stream().map(GatherDetailV::getRecBillId).collect(Collectors.toList());
            if (k == GatherTypeEnum.应收.getCode()) {
                List<ReceivableBill> receivableBills = receivableBillRepository.list(new LambdaQueryWrapper<ReceivableBill>().in(ReceivableBill::getId, billId).
                        eq(ReceivableBill::getSupCpUnitId, supCpUnitId));
                Map<Long, List<ReceivableBill>> receivableBillGroupMaps = receivableBills.stream().collect(Collectors.groupingBy(ReceivableBill::getId));
                v.forEach(gatherDetail -> {
                    ReceivableBill receivableBill = receivableBillGroupMaps.get(gatherDetail.getRecBillId()).get(0);
                    gatherDetail.setChargeItemType(receivableBill.getChargeItemType());
                    gatherDetail.setBillType(receivableBill.getBillType());
                    gatherDetail.setStatutoryBodyId(receivableBill.getStatutoryBodyId());
                    gatherDetail.setSpaceId(Long.valueOf(receivableBill.getRoomId()));
                });
            }else if (k == GatherTypeEnum.预收.getCode()) {
                v.forEach(gatherDetail -> {
                    gatherDetail.setBillType(BillTypeEnum.预收账单.getCode());
                    AdvanceBill advanceBill = advanceBillRepository.getById(gatherDetail.getRecBillId());
                    gatherDetail.setStatutoryBodyId(advanceBill.getStatutoryBodyId());
                    gatherDetail.setSpaceId(Long.valueOf(advanceBill.getRoomId()));
                });
            }
            gatherDetailVS.addAll(v);
        });

        return gatherDetailVS;
    }


    /**
     * 查询收款单可开票金额
     *
     * @param gatherInvoiceF
     */
    public BillSimpleInfoV canInvoiceInfo(GatherInvoiceF gatherInvoiceF) {
        BillSimpleInfoV billSimpleInfoV = new BillSimpleInfoV();
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        Long canInvoiceAmount = 0L;
        if(InvoiceGatherTypeEnum.收款单.equalsByCode(gatherInvoiceF.getGatherBillType())){
            wrapper.eq(GatherDetail::getGatherBillId, gatherInvoiceF.getGatherBillId()).
                    eq(GatherDetail::getSupCpUnitId, gatherInvoiceF.getSupCpUnitId()).
                    in(GatherDetail::getInvoiceState, List.of(InvoiceStateEnum.未开票.getCode(), InvoiceStateEnum.部分开票.getCode()));
            List<GatherDetail> gatherDetails = filterInvoiceGatherDetails(gatherDetailRepository.list(wrapper), gatherInvoiceF.getInvoiceType());
            if (CollectionUtils.isNotEmpty(gatherDetails)) {
                canInvoiceAmount = gatherDetails.stream().mapToLong(GatherDetail::getCanInvoiceAmount).sum();
            }
        }else {
            wrapper.eq(GatherDetail::getId, gatherInvoiceF.getGatherDetailId()).
                    eq(GatherDetail::getSupCpUnitId, gatherInvoiceF.getSupCpUnitId()).
                    in(GatherDetail::getInvoiceState, List.of(InvoiceStateEnum.未开票.getCode(), InvoiceStateEnum.部分开票.getCode()));
            GatherDetail gatherDetail = gatherDetailRepository.getOne(wrapper);
            if (Objects.nonNull(gatherDetail)) {
                canInvoiceAmount = gatherDetail.getCanInvoiceAmount();
            }
        }
        billSimpleInfoV.setCanInvoiceAmount(canInvoiceAmount);
        billSimpleInfoV.setSupCpUnitId(gatherInvoiceF.getSupCpUnitId());
        return billSimpleInfoV;
    }

    /**
     * 中交环境开票过滤预收账单，预收账单只允许开收据
     * @param gatherDetails
     * @return
     */
    private List<GatherDetail> filterInvoiceGatherDetails(List<GatherDetail> gatherDetails, Integer invoiceType) {
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            if (Objects.nonNull(invoiceType)) {
                switch (InvoiceLineEnum.valueOfByCode(invoiceType)) {
                    case 纸质收据:
                    case 电子收据:
                    case 收据:
                        break;
                    default:
                        return gatherDetails.stream()
                                .filter(gatherDetail ->
                                        !GatherTypeEnum.预收.equalsByCode(gatherDetail.getGatherType()))
                                .collect(Collectors.toList());
                }
            }
        }
        return gatherDetails;
    }

    /**
     * 根据收款明细id集合获取收款信息列表
     *
     * @param gatherDetailIds 收款明细id集合
     * @return List
     */
    public List<GatherDetailV> getGatherDetails(List<Long> gatherDetailIds, String supCpUnitId) {
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GatherDetail::getId, gatherDetailIds)
                .eq(GatherDetail::getSupCpUnitId, supCpUnitId);
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(wrapper);
        return Global.mapperFacade.mapAsList(gatherDetails, GatherDetailV.class);
    }


    /**
     * 根据账单编号集合获取收款信息列表
     *
     * @param receivableBillIds 账单编号集合
     * @return List
     */
    public List<GatherDetailV> getGatherDetailsByRecIds(List<Long> receivableBillIds, String supCpUnitId) {
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GatherDetail::getRecBillId, receivableBillIds)
                .eq(GatherDetail::getSupCpUnitId, supCpUnitId);
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(wrapper);
        return Global.mapperFacade.mapAsList(gatherDetails, GatherDetailV.class);
    }

    /**
     * 获取收款单统计信息
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statistics(SearchF<?> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "gd." + BillSharedingColumn.收款明细.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(form, "b." + BillSharedingColumn.收款账单.getColumnName());

        String gatherBillName = sharedBillAppService.getShareTableName(form,
                TableNames.GATHER_BILL, "b." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(form,
                TableNames.GATHER_DETAIL, "gd." + BillSharedingColumn.收款明细.getColumnName());

        return gatherDetailRepository.statistics(form,gatherBillName,gatherDetailName);
    }


    /**
     * 根据收款明细id集合删除收款单和明细
     * @param gatherDetailIds supCpUnitId 收款明细id集合
     * @return Boolean
     */
    public Boolean deleteGatherBillDetails(List<Long> gatherDetailIds, String supCpUnitId) {
        LambdaQueryWrapper<GatherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(GatherDetail::getId, gatherDetailIds)
                .eq(GatherDetail::getSupCpUnitId, supCpUnitId).eq(GatherDetail::getDeleted,0);
        List<GatherDetail> gatherDetails = gatherDetailRepository.list(wrapper);
        List<Long> gatherBillIdList = new ArrayList<>();

        if(CollectionUtils.isNotEmpty(gatherDetails)){
            for(GatherDetail detail : gatherDetails){
                Long gatherBillId = detail.getGatherBillId();
                List<Long> gatherBillList = Lists.newArrayList();
                gatherBillList.add(gatherBillId);
                List<GatherDetail> gatherDetailLists = gatherDetailRepository.getByGatherBillIds(gatherBillList,supCpUnitId);
                if(CollectionUtils.isNotEmpty(gatherDetailLists) && gatherDetailLists.size() == 1){
                    gatherBillIdList.add(gatherBillId);
                }
            }
        }
        gatherDetailRepository.updateGatherDetailDeleted(gatherDetailIds,supCpUnitId);
        if(CollectionUtils.isNotEmpty(gatherBillIdList)){
            gatherBillRepository.updateGatherBillDeleted(gatherBillIdList,supCpUnitId);
        }

        //如果detail表的gather_bill_id的所有数据都被删除了，则把对应的gather_bill表数据删了
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getAllDetailByIds(gatherDetailIds,supCpUnitId);
        if(CollectionUtils.isNotEmpty(gatherDetailList)){
            for(GatherDetail detail : gatherDetailList){
                Long gatherBillId = detail.getGatherBillId();
                List<Long> gatherBillList = Lists.newArrayList();
                gatherBillList.add(gatherBillId);
                List<GatherDetail> gatherDetailLists = gatherDetailRepository.getAllDetailByGatherBillIds(gatherBillList,supCpUnitId);
                if(CollectionUtils.isNotEmpty(gatherDetailLists)){
                    List<GatherDetail> collect = gatherDetailLists.stream().filter(bill -> bill.getDeleted() == 0).collect(Collectors.toList());
                    //如果为空，说明detail数据全部都被删了，则把gather_bill也删了
                    if(CollectionUtils.isEmpty(collect)){
                        List<Long> gatherBillIdDeletedList = new ArrayList<>();
                        gatherBillIdDeletedList.add(gatherBillId);
                        gatherBillRepository.updateGatherBillDeleted(gatherBillIdDeletedList,supCpUnitId);
                    }
                }
            }
        }
        return Boolean.TRUE;
    }




    /**
     *
     * 根据预收账单id获取收款单id
     * @param advanceBillId
     * @param supCpUnitId
     * @return Long
     */
    public Long getGatherBillId(Long advanceBillId, String supCpUnitId) {
        List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(advanceBillId, supCpUnitId);
        return CollectionUtils.isEmpty(gatherDetails)?0L:gatherDetails.get(0).getGatherBillId();
    }

    public Boolean editGatherBillFlag(BillFlagF billFlagF){
        List<FlowClaimDetailE> list = flowClaimDetailRepository.list(new LambdaQueryWrapper<FlowClaimDetailE>().in(FlowClaimDetailE::getInvoiceId, billFlagF.getBillIdList())
                .eq(FlowClaimDetailE::getClaimType, ClaimTypeEnum.账单认领.getCode())
                .eq(FlowClaimDetailE::getState, 0)
                .eq(FlowClaimDetailE::getDeleted, 0));
        if (billFlagF.getFlag().equals(1)) {
            // 添加标记
            // 判断是否已经认领  已认领不能添加标记
            List<Long> collect = list.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            for (Long l : collect) {
                billFlagF.getBillIdList().removeIf(i -> i.equals(l));
            }
            if(CollectionUtils.isNotEmpty(billFlagF.getBillIdList())) {
                gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 1)
                        .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                        .in("id", billFlagF.getBillIdList()));
                gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 1)
                        .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                        .in("gather_bill_id", billFlagF.getBillIdList()));
            }
        } else if (billFlagF.getFlag().equals(0)){
            List<Long> collect = list.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            for (Long l : collect) {
                billFlagF.getBillIdList().removeIf(i -> i.equals(l));
            }
            if(CollectionUtils.isNotEmpty(billFlagF.getBillIdList())) {
                gatherBillRepository.update(new UpdateWrapper<GatherBill>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                        .in("id", billFlagF.getBillIdList()));

                gatherDetailRepository.update(new UpdateWrapper<GatherDetail>().set("inference_state", 0)
                        .eq("sup_cp_unit_id", billFlagF.getSupCpUnitId())
                        .in("gather_bill_id", billFlagF.getBillIdList()));
            }

        }
        return true;
    }


}
