package com.wishare.finance.domains.invoicereceipt.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.FinishInvoiceF;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.invoice.invoice.dto.BillInvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceAndReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceChildDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceInfoDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.InvoiceStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.NuonuoInvoiceInfoDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchBlueF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBatchF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceGatherDetailAmount;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceListF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoicePrintF;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoicePreviewDetailV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoicePreviewV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.apps.model.invoice.nuonuo.vo.QueryInvoiceResultV;
import com.wishare.finance.apps.service.bill.AdvanceBillAppService;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.InvoiceGatherTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.facade.MsgFacade;
import com.wishare.finance.domains.bill.repository.BillRepositoryFactory;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.configure.accountbook.facade.AmpFinanceFacade;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.facade.CostCenterFacade;
import com.wishare.finance.domains.configure.organization.repository.StatutoryBodyAccountRepository;
import com.wishare.finance.domains.expensereport.enums.KingDeePushStateEnum;
import com.wishare.finance.domains.invoicereceipt.aggregate.EnterInvoiceA;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceA;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceBlueA;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceRedA;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceReceiptDetailCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.EnterInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.SendInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.PushModeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceBillDetailDto;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceDetailDto;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceMessageDto;
import com.wishare.finance.domains.invoicereceipt.dto.InvoiceSendDto;
import com.wishare.finance.domains.invoicereceipt.dto.ReceiptMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceBookE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceChildE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.enums.InvoiceReceiptSourceEnum;
import com.wishare.finance.domains.invoicereceipt.facade.InvoiceExternalService;
import com.wishare.finance.domains.invoicereceipt.facade.SpaceFacade;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceBookRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceChildRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiveDetailedRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRedApplyRepository;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceRepository;
import com.wishare.finance.domains.invoicereceipt.repository.ReceiptRepository;
import com.wishare.finance.domains.invoicereceipt.support.InvoiceDetailHelper;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.service.FlowClaimRecordDomainService;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataClaimEnum;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.conts.TextContentEnum;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongContentRF;
import com.wishare.finance.infrastructure.remote.fo.external.lingshuitong.LingshuitongInvoiceRF;
import com.wishare.finance.infrastructure.remote.vo.external.nuonuo.QueryInvoiceResultRV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.ChineseNumber;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import com.wishare.finance.infrastructure.utils.FileUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.TextContentUtil;
import com.wishare.finance.infrastructure.utils.ZipUtils;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.enums.OperationEnum;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InvoiceDomainService implements ApiBase {

    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final InvoiceChildRepository invoiceChildRepository;
    private final InvoiceReceiptRepository invoiceReceiptRepository;
    private final InvoiceReceiptDetailRepository invoiceReceiptDetailRepository;
    private final InvoiceReceiveDetailedRepository invoiceReceiveDetailedRepository;
    private final InvoiceRedApplyRepository invoiceRedApplyRepository;
    private final InvoiceBookRepository invoiceBookRepository;
    private final AccountOrgFacade accountOrgFacade;
    private final InvoiceExternalService invoiceExternalService;
    private final FlowClaimRecordDomainService flowClaimRecordDomainService;
    private final SpaceFacade spaceFacade;
    private final FileStorage fileStorage;
    private final BillFacade billFacade;
    private final StatutoryBodyAccountRepository statutoryBodyAccountRepository;
    private final ThirdSystemDomainService thirdSystemDomainService;
    private final AmpFinanceFacade ampFinanceFacade;
    private final AdvanceBillAppService advanceBillAppService;
    private final ReceivableBillAppService receivableBillAppService;
    private final GatherBillRepository gatherBillRepository;
    private final GatherDetailRepository gatherDetailRepository;
    private final ExternalClient externalClient;
    private final TemporaryChargeBillAppService temporaryChargeBillAppService;
    private final CostCenterFacade costCenterFacade;
    private final OrgClient orgClient;
    private final SpaceClient spaceClient;

    @Value("${wishare.file.host:}")
    private String fileHost;
    @Autowired
    private MsgFacade msgFacade;
    @Setter(onMethod_ = @Autowired)
    protected SharedBillAppService sharedBillAppService;

    private final FlowClaimDetailRepository flowClaimDetailRepository;

    /**
     * 分页查询开票列表
     *
     * @param form
     * @return
     */
    public Page<InvoiceDto> queryPage(PageF<SearchF<?>> form) {
        return invoiceRepository.queryPage(form);
    }

    /**
     * 根据条件查询开票列表
     *
     * @param form
     * @return
     */
    public List<BillInvoiceDto> listByBillIds(InvoiceListF form) {
        return invoiceRepository.listByBillIds(form);
    }


    /**
     * 调用财务中心，批量完成开票
     *
     * @param invoiceReceiptDetailCommands
     * @param success
     */
    private void handleBillStateFinishInvoice(List<AddInvoiceReceiptDetailCommand> invoiceReceiptDetailCommands, Boolean success) {
        List<FinishInvoiceF> finishInvoiceFList = Lists.newArrayList();
        invoiceReceiptDetailCommands.forEach(addInvoiceReceiptDetailCommand -> {
            FinishInvoiceF finishInvoiceF = new FinishInvoiceF();
            finishInvoiceF.setBillId(addInvoiceReceiptDetailCommand.getBillId());
            finishInvoiceF.setInvoiceAmount(addInvoiceReceiptDetailCommand.getTaxIncludedAmount());
            finishInvoiceF.setSuccess(success);
            finishInvoiceFList.add(finishInvoiceF);
        });
        switch (BillTypeEnum.valueOfByCode(invoiceReceiptDetailCommands.get(0).getBillType())) {
            case 应收账单:
                receivableBillAppService.finishInvoiceBatch(finishInvoiceFList);
                break;
            case 临时收费账单:
                temporaryChargeBillAppService.finishInvoiceBatch(finishInvoiceFList);
                break;
            case 预收账单:
                advanceBillAppService.finishInvoiceBatch(finishInvoiceFList);
                break;
        }
    }

    /**
     * 根据账单id获取发票明细
     *
     * @param billId
     * @return
     */
    public List<InvoiceReceiptE> getByBillId(Long billId) {
        List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.getByBillId(billId, Lists.newArrayList(InvoiceLineEnum.增值税电子专票.getCode(),
                InvoiceLineEnum.增值税专用发票.getCode(), InvoiceLineEnum.增值税普通发票.getCode(), InvoiceLineEnum.增值税电子发票.getCode(), InvoiceLineEnum.全电普票.getCode(),
                InvoiceLineEnum.全电专票.getCode(),InvoiceLineEnum.定额发票.getCode()));
        return invoiceReceiptES;
    }

    /**
     * 获取发票聚合类
     * @param invoiceReceiptId
     * @return
     */
    public InvoiceA getInvoiceA(Long invoiceReceiptId) {
        InvoiceE invoiceE = invoiceRepository.getByInvoiceReceiptId(invoiceReceiptId);
        if (Objects.isNull(invoiceE)) {
            throw BizException.throw400(ErrorMessage.INVOICE_NOT_EXISTS.getErrMsg());
        }
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = invoiceReceiptDetailRepository.getByInvoiceReceipt(
                invoiceReceiptId);
        return new InvoiceA(invoiceReceiptE, invoiceE, invoiceReceiptDetailEList);
    }


    /**
     * 根据发票号码查询发票数量
     * @param invoiceNo
     * @return
     */
    public long countByInvoiceNo(String invoiceNo) {
        return invoiceReceiptRepository.countByInvoiceNo(invoiceNo,Lists.newArrayList(InvoiceReceiptStateEnum.已红冲.getCode(),InvoiceReceiptStateEnum.已作废.getCode()));
    }

    /**
     * 统计发票信息
     *
     * @param form
     * @return
     */
    public InvoiceStatisticsDto statistics(PageF<SearchF<?>> form) {
        return invoiceRepository.statistics(form);
    }

    /**
     * 分页获取发票收据主表明细（用于流水认领发票明细列表）
     *
     * @return Page
     */
    public Page<InvoiceReceiptE> queryInvoiceReceiptPage(PageF<SearchF<InvoiceReceiptE>> query) {
        QueryWrapper<InvoiceReceiptE> queryModel = query.getConditions().getQueryModel();
        Page<InvoiceReceiptE> page = new Page<>(query.getPageNum(), query.getPageSize());
        return invoiceReceiptRepository.page(page, queryModel);
    }

    /**
     * 根据蓝票id获取关联的红票信息
     *
     * @return List
     */
    public List<InvoiceAndReceiptDto> getRedInvoiceByBlueInvoiceId(List<Long> blueInvoiceReceiptId){
        List<InvoiceAndReceiptDto> redInvoiceList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(blueInvoiceReceiptId)){
            redInvoiceList = invoiceRepository.getRedInvoiceInfoByBlueInvoiceReceiptId(blueInvoiceReceiptId);
        }
        return redInvoiceList;
    }

    /**
     * 分页查询发票和收据列表
     *
     * @param form form
     * @return PageV
     */
    public Page<InvoiceAndReceiptDto> queryDetailPage(PageF<SearchF<?>> form, Integer type) {
        if (!costCenterFacade.changeNodeIdSearch(form.getConditions(), "ir.cost_center_id")) {
            return new Page<>();
        }
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        queryWrapper.in("ir.state", List.of(InvoiceReceiptStateEnum.开票成功.getCode(),InvoiceReceiptStateEnum.部分红冲.getCode()));
        queryWrapper.eq("ir.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.eq("ir.claim_status", DataClaimEnum.未认领.getCode());
        queryWrapper.eq("ir.sys_source", type);
        // queryWrapper.gt("rb.settle_amount", 0);
        queryWrapper.and(wrapper -> wrapper.isNull("i.invoice_type").or().eq("i.invoice_type", InvoiceTypeEnum.蓝票.getCode()));
        PageQueryUtils.validQueryContainsFieldAndValue(form, "rb." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(form, TableNames.RECEIVABLE_BILL, "rb." + BillSharedingColumn.应收账单.getColumnName());
        Page<InvoiceAndReceiptDto> invoiceAndReceiptDtoPage = invoiceReceiptRepository.queryDetailPage(Page.of(form.getPageNum(), form.getPageSize()), queryWrapper, receivableBillName);
        //获取蓝票的红票信息
        List<InvoiceAndReceiptDto> records = invoiceAndReceiptDtoPage.getRecords();
        List<Long> blueInvoiceReceiptId = records.stream().map(InvoiceAndReceiptDto::getId).collect(Collectors.toList());
        List<InvoiceAndReceiptDto> redInvoiceList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(blueInvoiceReceiptId)){
            redInvoiceList = invoiceRepository.getRedInvoiceInfoByBlueInvoiceReceiptId(blueInvoiceReceiptId);
        }
        Map<Long, List<InvoiceAndReceiptDto>> collect = redInvoiceList.stream().collect(Collectors.groupingBy(InvoiceAndReceiptDto::getId));
        for (InvoiceAndReceiptDto record : records) {
            List<InvoiceAndReceiptDto> invoiceAndReceiptDtoList = collect.get(record.getId());
            if(CollectionUtils.isNotEmpty(invoiceAndReceiptDtoList)){
                record.setRedTaxAmount(Math.abs(invoiceAndReceiptDtoList.get(0).getRedTaxAmount()));
            }else{
                record.setRedTaxAmount(0L);
            }
        }
        return invoiceAndReceiptDtoPage;
    }

    /**
     * 分页查询发票和收据列表-新模式
     *
     * @param form form
     * @return PageV
     */
    public Page<InvoiceAndReceiptDto> queryDetailPageNew(PageF<SearchF<?>> form) {
        if (!costCenterFacade.changeNodeIdSearch(form.getConditions(), "ir.cost_center_id")) {
            return new Page<>();
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(form.getConditions(), "ir.statutory_body_id")) {
            return new Page<>();
        }
        // 补充条件
        RepositoryUtil.invoiceClaimPageConvertSearchType(form.getConditions());
        // 判断是否是修改时查询，传入流水认领记录ID
        List<Long> claimInvoiceReceiptIds = getClaimInvoiceReceiptIds(form.getConditions());
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        queryWrapper.in("ir.state", List.of(InvoiceReceiptStateEnum.开票成功.getCode(),InvoiceReceiptStateEnum.部分红冲.getCode()));
        queryWrapper.eq("ir.deleted", DataDeletedEnum.NORMAL.getCode());
        if (CollectionUtils.isEmpty(claimInvoiceReceiptIds)) {
            queryWrapper.eq("ir.claim_status", DataClaimEnum.未认领.getCode());
        } else {
            queryWrapper.and(wrapper -> wrapper.eq("ir.claim_status", DataClaimEnum.未认领.getCode())
                    .or().in("ir.id", claimInvoiceReceiptIds));
        }
        queryWrapper.and(wrapper -> wrapper.isNull("i.invoice_type").or().eq("i.invoice_type", InvoiceTypeEnum.蓝票.getCode()));
        PageQueryUtils.validQueryContainsFieldAndValue(form, "rb." + BillSharedingColumn.应收账单.getColumnName());
        String receivableBillName = sharedBillAppService.getShareTableName(form, TableNames.RECEIVABLE_BILL, "rb." + BillSharedingColumn.应收账单.getColumnName());
        Page<InvoiceAndReceiptDto> invoiceAndReceiptDtoPage = invoiceReceiptRepository.queryDetailPage(Page.of(form.getPageNum(), form.getPageSize()), queryWrapper, receivableBillName);
        //获取蓝票的红票信息
        List<InvoiceAndReceiptDto> records = invoiceAndReceiptDtoPage.getRecords();
        List<Long> blueInvoiceReceiptId = records.stream().map(InvoiceAndReceiptDto::getId).collect(Collectors.toList());
        List<InvoiceAndReceiptDto> redInvoiceList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(blueInvoiceReceiptId)){
            redInvoiceList = invoiceRepository.getRedInvoiceInfoByBlueInvoiceReceiptId(blueInvoiceReceiptId);
        }
        Map<Long, List<InvoiceAndReceiptDto>> collect = redInvoiceList.stream().collect(Collectors.groupingBy(InvoiceAndReceiptDto::getId));
        for (InvoiceAndReceiptDto record : records) {
            List<InvoiceAndReceiptDto> invoiceAndReceiptDtoList = collect.get(record.getId());
            if(CollectionUtils.isNotEmpty(invoiceAndReceiptDtoList)){
                record.setRedTaxAmount(Math.abs(invoiceAndReceiptDtoList.get(0).getRedTaxAmount()));
            }else{
                record.setRedTaxAmount(0L);
            }
        }
        return invoiceAndReceiptDtoPage;
    }

    private List<Long> getClaimInvoiceReceiptIds(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }
        Iterator<Field> iterator = fields.iterator();
        Field field;

        while (iterator.hasNext()) {
            field = iterator.next();
            // 传入了认领记录ID，说明是修改时查询
            if ("flowClaimRecordId".equals(field.getName())) {
                iterator.remove();
                return flowClaimDetailRepository.queryInvoiceIdsByFlowClaimRecordId(field.getValue());
            }
        }
        return Collections.emptyList();
    }

    /**
     * 查询发票和收据合计金额(用于流水领用)
     *
     * @param idList 发票id集合
     * @return Long
     */
    public InvoiceAndReceiptStatisticsDto statisticsAmount(List<Long> idList, Integer sysSource, String supCpUnitId) {
        String receivableBillName = sharedBillAppService.getShareTableName(supCpUnitId, BillSharedingColumn.应收账单.getTableName());
        return invoiceReceiptRepository.queryAmount(idList, sysSource, supCpUnitId, receivableBillName);
    }

    /**
     * 查询发票和收据合计金额(用于流水领用)
     *
     * @return Long
     */
    public InvoiceAndReceiptStatisticsDto statisticsAmount2(PageF<SearchF<?>> form) {
        if (!costCenterFacade.changeNodeIdSearch(form.getConditions(), "ir.cost_center_id")) {
            return new InvoiceAndReceiptStatisticsDto();
        }
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        queryWrapper.eq("ir.deleted", DataDeletedEnum.NORMAL.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(form, "rb." + BillSharedingColumn.应收账单.getColumnName());
        String oneTable = sharedBillAppService.getShareTableName(form, TableNames.RECEIVABLE_BILL, "rb." + BillSharedingColumn.应收账单.getColumnName());
        return invoiceReceiptRepository.queryAmount2(queryWrapper,oneTable);
    }

    /**
     * 根据发票id集合获取发票信息
     *
     * @param idList 发票id集合
     * @return List
     */
    public List<InvoiceReceiptE> queryByIdList(List<Long> idList) {
        return invoiceReceiptRepository.listByIds(idList);
    }

    /**
     * 根据发票id集合获取发票信息
     *
     * @param idList 发票id集合
     * @return List
     */
    public List<InvoiceReceiptE> queryByIdListOrderByPriceTaxAmountDesc(List<Long> idList) {
        return invoiceReceiptRepository.listByIdsOrderByPriceTaxAmountDesc(idList);
    }

    /**
     * 更新票据领用状态
     *
     * @param invoiceIdList 票据id集合
     * @param claimState    领用状态
     */
    public Boolean updateClaimStatusByIdList(List<Long> invoiceIdList, int claimState) {
        return invoiceReceiptRepository.updateClaimStatusByIdList(invoiceIdList, claimState);
    }

    /**
     * 根据账单id批量获取发票数据
     *
     * @param advanceBillIds
     * @param billType
     * @return
     */
    public Optional<Map<Long, List<InvoiceBillDto>>> getBillInvoiceMap(List<Long> advanceBillIds, Integer billType) {
        if (advanceBillIds.isEmpty()) {
            return Optional.empty();
        }
        List<InvoiceBillDto> invoiceDtoList = invoiceReceiptRepository.getBillInvoiceList(advanceBillIds, billType);
        if (invoiceDtoList.isEmpty()) {
            return Optional.empty();
        }
        Map<Long, List<InvoiceBillDto>> map = new HashMap<>();

        for (InvoiceBillDto invoiceBillDto : invoiceDtoList) {
            List<InvoiceBillDto> invoiceBillDtos = map.getOrDefault(invoiceBillDto.getBillId(),
                    new ArrayList<>());
            invoiceBillDtos.add(invoiceBillDto);
            map.put(invoiceBillDto.getBillId(), invoiceBillDtos);
        }

        return Optional.of(map);
    }

    /**
     * 根据账单获取开票详情
     *
     * @param billId
     * @param billType
     * @return
     */
    public List<InvoiceBillDto> getInvoiceDetailByBillId(Long billId, BillTypeEnum billType) {
        return invoiceReceiptRepository.getBillInvoiceList(List.of(billId), billType.getCode());
    }

    /**
     * 作废发票
     *
     * @param invoiceReceiptId
     * @return
     */
    @Transactional
    public Boolean voidInvoice(Long invoiceReceiptId) {
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        if (null == invoiceReceiptE) {
            throw BizException.throw400("发票不存在");
        }
        if (InvoiceReceiptStateEnum.已作废.getCode().equals(invoiceReceiptE.getState())) {
            throw BizException.throw400("该发票已作废");
        }
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptId));
        invoiceRepository.voidReceipt(invoiceReceiptE);
        Boolean res = billFacade.invoiceVoidBatch(invoiceReceiptDetailES, invoiceReceiptE.getCommunityId());
        return res;

    }

    /**
     * 获取发票子表数据
     *
     * @param invoiceReceiptId
     * @return
     */
    public List<InvoiceChildDto> invoiceChildList(Long invoiceReceiptId) {
        return invoiceChildRepository.getByInvoiceReceiptId(invoiceReceiptId);
    }

    /**
     * 获取发票url地址
     * @param invoiceReceiptId
     * @return
     */
    public NuonuoInvoiceInfoDto getNuonuoInvoiceUrl(Long invoiceReceiptId) {
        LambdaQueryWrapper<InvoiceE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvoiceE::getInvoiceReceiptId, invoiceReceiptId);
        InvoiceE invoiceE = invoiceRepository.getOne(wrapper);
        LambdaQueryWrapper<InvoiceReceiptE> invoiceReceiptWrapper = new LambdaQueryWrapper<>();
        invoiceReceiptWrapper.eq(InvoiceReceiptE::getId, invoiceReceiptId);
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getOne(invoiceReceiptWrapper);
        if (invoiceReceiptE == null) {
            throw BizException.throw400("未查询到对应数据");
        }
        NuonuoInvoiceInfoDto nuonuoInvoiceInfoDto = new NuonuoInvoiceInfoDto();
        nuonuoInvoiceInfoDto.setNuonuoUrl(invoiceE.getNuonuoUrl());
        nuonuoInvoiceInfoDto.setBuyerName(invoiceE.getBuyerName());
        nuonuoInvoiceInfoDto.setFailReason(invoiceE.getFailReason());
        nuonuoInvoiceInfoDto.setState(invoiceReceiptE.getState());
        nuonuoInvoiceInfoDto.setInvoiceCode(invoiceE.getInvoiceCode());
        nuonuoInvoiceInfoDto.setInvoiceNo(invoiceE.getInvoiceNo());
        nuonuoInvoiceInfoDto.setInvoiceTitleType(invoiceE.getInvoiceTitleType());
        nuonuoInvoiceInfoDto.setPriceTaxAmount(invoiceReceiptE.getPriceTaxAmount());
        nuonuoInvoiceInfoDto.setBillingTime(invoiceReceiptE.getBillingTime());
        nuonuoInvoiceInfoDto.setSalerTaxNum(invoiceE.getSalerTaxNum());
        nuonuoInvoiceInfoDto.setInvoiceReceiptId(invoiceReceiptE.getId());
        return nuonuoInvoiceInfoDto;
    }

    /**
     * 批量开具发票
     *
     * @param command
     * @return
     */
    /*public Long invoiceBatch(AddInvoiceCommand command, String tenantId) {
        //根据账单id获取账单信息
        List<BillDetailMoreV> billDetailMoreVList = batchGetBillDetail(command.getBillIds(), command.getBillType(), command.getSupCpUnitId());
        //校检开票中,已经开票 相同法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)
        billFacade.checkBillDetail(billDetailMoreVList,command);

        //获取销售方的银行账户
        if (StringUtils.isBlank(command.getSalerAccount()) && Objects.nonNull(billDetailMoreVList.get(0).getSbAccountId())) {
            //法定单位银行账户表(statutory_body_account)
            StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountRepository.getById(billDetailMoreVList.get(0).getSbAccountId());
            String salerAccount = statutoryBodyAccountE.getBankName() + " " + statutoryBodyAccountE.getBankAccount();
            command.setSalerAccount(salerAccount);

        }

        //查询商品名称
        ampFinanceFacade.queryByGoodsList(billDetailMoreVList);

        //通过账单的法定单位获取法定单位税号作为销售方信息
        OrgFinanceRv orgFinanceRv = accountOrgFacade.getOrgFinance(billDetailMoreVList.get(0).getStatutoryBodyId());

        //构造蓝票(包含校检)
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA(billDetailMoreVList, command, orgFinanceRv);
        //开票之前添加销方名称
        invoiceExternalService.preInvoice(invoiceBlueA);

        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();



        //通知财务中心开票准备
        Boolean res = false;
        //增值税电子发票||全电普票
        if (InvoiceLineEnum.增值税电子发票.getCode().equals(invoiceReceiptE.getType()) || InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType())
                || InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())) {

            //补齐税目
            List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
            if (taxIteamMapByChargeId == null) {
                throw BizException.throw400("该批次费项未配置对应的税目编码");
            }
            // 判断是否包含 3040502029902000000-经营租赁租赁费  税目编码  且是否只有这一种税目编码
            // 判断账单是否是 同一个房号
            // 否则就抛异常

            invoiceExternalService.CheckRealPropertyRentInfo(billDetailMoreVList, invoiceE, chargeItemIds, invoiceReceiptE);

            // 转换统一发票明细
            List<InvoiceDetailDto> invoiceDetailDtoList = InvoiceDetailHelper.blueDetailsToInvoiceDetailDtoList(invoiceReceiptDetailES, taxIteamMapByChargeId);
            //修改bill为开票中
            res = billFacade.invoiceBatch(invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getBillId).collect(Collectors.toList()), BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), command.getSupCpUnitId());
            try {
                //调用诺诺进行电子开票 自定义开票 获取发票流水号
                String nuonuoCommunityId = spaceFacade.getNuonuoCommunityId(invoiceReceiptE.getCommunityId());
                if (invoiceReceiptE.getType().equals(InvoiceLineEnum.增值税电子发票.getCode())) {
                    //增值税电子发票(蓝票)
                    invoiceE.setInvoiceSerialNum(invoiceExternalService.nuonuoBillingNew(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxIteamMapByChargeId, tenantId, InvoiceTypeEnum.蓝票));
                } else if (InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) || InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())) {
                    invoiceE.setInvoiceSerialNum(invoiceExternalService.opeMplatformBillingNew(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxIteamMapByChargeId, tenantId));
                }
            } catch (Exception e) {
                //回滚状态
                billFacade.handleBillStateFinishInvoice(BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), invoiceReceiptDetailES, false, invoiceReceiptE.getCommunityId());
                throw e;
            }
        } else {
            //排除（增值税电子发票||全电普票）开票
            //获取发票代码
            String invoiceCode = getInvoiceCode(invoiceE.getInvoiceNo(), invoiceReceiptE.getType());
            if (StringUtils.isNotBlank(invoiceCode)) {
                invoiceE.setInvoiceCode(invoiceCode);
                //票据领用明细表(invoice_receive_detailed) 修改状态
                invoiceReceiveDetailedRepository.useByInvoiceNo(Long.valueOf(invoiceE.getInvoiceNo()), command.getType());
            }
            //开票完成
            res = billFacade.handleBillStateFinishInvoice(BillTypeEnum.valueOfByCode(invoiceReceiptE.getBillType()), invoiceReceiptDetailES, true, invoiceReceiptE.getCommunityId());
        }
        if (res) {
            //成功开票
            gatherBillRepository.updateInvoiceState(command.getGatherBillId(), BillInvoiceStateEnum.开票中);
            invoiceReceiptRepository.save(invoiceReceiptE);
            invoiceRepository.save(invoiceE);
            invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailES);

            //记日志
            if (CollectionUtils.isNotEmpty(invoiceReceiptDetailES)){
                invoiceReceiptDetailES.forEach(iba -> {
                    BizLog.initiate(String.valueOf(iba.getBillId()), LogContext.getOperator(), LogObject.账单, LogAction.开票,
                            new Content().option(new ContentOption(new PlainTextDataItem("票据类型：" + InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType()).getDes(), true)))
                                    .option(new ContentOption(new PlainTextDataItem("开票金额：", false)))
                                    .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(iba.getInvoiceAmount()), false), OptionStyle.normal()))
                                    .option(new ContentOption(new PlainTextDataItem("元", false))));
                });
            }

            return invoiceReceiptE.getId();
        }
        throw BizException.throw400("开票失败");
    }*/


    /**
     * 处理 销方银行开户行及账号
     */
    private void getSalerAccount(AddInvoiceCommand command,List<BillDetailMoreV> billDetailMoreVList){
        if (StringUtils.isBlank(command.getSalerAccount()) && Objects.nonNull(billDetailMoreVList.get(0).getSbAccountId())) {
            //法定单位银行账户表(statutory_body_account)
            StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountRepository.getById(billDetailMoreVList.get(0).getSbAccountId());
           if (Objects.nonNull(statutoryBodyAccountE)){
               String salerAccount = statutoryBodyAccountE.getBankName() + " " + statutoryBodyAccountE.getBankAccount();
               /** command 增加 销方银行开户行及账号 */
               command.setSalerAccount(salerAccount);
           }
        }
    }


    /**
     *（电子发票||全电发票）开票
     * @param invoiceBlueA
     * @param billDetailMoreVList
     * @param command
     * @return
     */
    private boolean doElectronicInvoice(InvoiceBlueA invoiceBlueA,List<BillDetailMoreV> billDetailMoreVList,AddInvoiceCommand command){
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        //发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();
        String tenantId = Optional.ofNullable(ThreadLocalUtil.curIdentityInfo()).map(IdentityInfo::getTenantId).get();
        //补齐税目
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(taxIteamMapByChargeId),"该批次费项未配置对应的税目编码");
        // 判断是否包含 3040502029902000000-经营租赁租赁费  税目编码  且是否只有这一种税目编码
        // 判断账单是否是 同一个房号
        // 否则就抛异常
        invoiceExternalService.checkRealPropertyRentInfo(billDetailMoreVList, invoiceE, chargeItemIds, invoiceReceiptE);
        // 判断是否包含 305030-建设服务  税目编码  且是否只有这一种税目编码
        invoiceExternalService.checkBuildingServiceInfo(billDetailMoreVList, invoiceE, chargeItemIds, invoiceReceiptE,command.getServiceInfoF());
        // 转换统一发票明细
        List<InvoiceDetailDto> invoiceDetailDtoList = InvoiceDetailHelper.blueDetailsToInvoiceDetailDtoList(invoiceReceiptDetailES, taxIteamMapByChargeId);
        //修改bill为开票中[advance_bill || receivable_bill  (gather_bill && gather_detail)]
        boolean res = billFacade.invoiceBatch(invoiceReceiptDetailES, command.getSupCpUnitId());
        try {
            //调用诺诺进行电子开票 自定义开票 获取发票流水号
            String nuonuoCommunityId = spaceFacade.getNuonuoCommunityId(invoiceReceiptE.getCommunityId());
            if (invoiceReceiptE.getType().equals(InvoiceLineEnum.增值税电子发票.getCode())) {
                //增值税电子发票(蓝票)
                invoiceE.setInvoiceSerialNum(invoiceExternalService.nuonuoBillingNew(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxIteamMapByChargeId, tenantId, InvoiceTypeEnum.蓝票));
            } else if (InvoiceLineEnum.全电普票.getCode().equals(invoiceReceiptE.getType()) || InvoiceLineEnum.全电专票.getCode().equals(invoiceReceiptE.getType())) {
                invoiceE.setInvoiceSerialNum(invoiceExternalService.opeMplatformBillingNew(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxIteamMapByChargeId, tenantId));
            } else if (InvoiceLineEnum.增值税专用发票.getCode().equals(invoiceReceiptE.getType())){
                invoiceE.setInvoiceSerialNum(invoiceExternalService.paperInvoices(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceDetailDtoList, nuonuoCommunityId, taxIteamMapByChargeId, tenantId, InvoiceTypeEnum.蓝票, null));
            }
        } catch (Exception e) {
            //回滚状态
            billFacade.handleBillStateFinishInvoice(invoiceReceiptDetailES, false, invoiceReceiptE.getCommunityId());
            throw e;
        }
        return res;
    }

    /**
     *（纸质发票||普通发票）开票
     * @param invoiceBlueA
     * @param command
     * @return
     */
    private boolean doOrdinaryInvoice(InvoiceBlueA invoiceBlueA, AddInvoiceCommand command){
        //（增值税电子发票||全电普票）开票
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        //发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();
        //获取发票代码
        String invoiceCode = this.getInvoiceCode(invoiceE.getInvoiceNo(), invoiceReceiptE.getType());
        if (StringUtils.isNotBlank(invoiceCode)) {
            invoiceE.setInvoiceCode(invoiceCode);
            //票据领用明细表[invoice_receive_detailed] 修改状态
            invoiceReceiveDetailedRepository.useByInvoiceNo(Long.valueOf(invoiceE.getInvoiceNo()), command.getType());
        }
        //开票完成
        return billFacade.handleBillStateFinishInvoice(invoiceReceiptDetailES, true, invoiceReceiptE.getCommunityId());
    }

    /**
     * @param form 方法参考 doElectronicInvoice 实现的
     * @return
     */
    public InvoiceBuildingServiceInfoF buildingServiceInfo(InvoiceBatchF form) {
        AddInvoiceCommand command = Global.mapperFacade.map(form, AddInvoiceCommand.class);
        //获取收款单明细(额外收集账单ids)
        List<BillDetailMoreV> billDetailMoreVList = this.getBillDetailMoreVList(command);

        //通过账单的法定单位获取法定单位税号作为销售方信息
        OrgFinanceRv orgFinanceRv = accountOrgFacade.getOrgFinance(billDetailMoreVList.get(0).getStatutoryBodyId());
        //构造蓝票(包含校检)
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA(billDetailMoreVList, command, orgFinanceRv);
        //发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();
        //补齐税目
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

        return invoiceExternalService.handleBuildingServiceInfo(chargeItemIds,billDetailMoreVList);
    }

    /**
     * 批量开具发票
     *
     * @param command
     * @return
     */
    public Long invoiceBatch(AddInvoiceCommand command,List<BillDetailMoreV> billDetailMoreVList) {
        /** command 增加 销方银行开户行及账号 */
        this.getSalerAccount(command,billDetailMoreVList);
        /** billDetailMoreVList 增加 商品名称 */
        ampFinanceFacade.queryByGoodsList(billDetailMoreVList);
        //通过账单的法定单位获取法定单位税号作为销售方信息
        OrgFinanceRv orgFinanceRv = accountOrgFacade.getOrgFinance(billDetailMoreVList.get(0).getStatutoryBodyId());
        //构造蓝票(包含校检)
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA(billDetailMoreVList, command, orgFinanceRv);
        //开票之前添加销方名称
        invoiceExternalService.preInvoice(invoiceBlueA);
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        //发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();
        //通知财务中心开票准备
        boolean res = InvoiceLineEnum.getWayStatus(invoiceReceiptE.getType())?
                this.doElectronicInvoice(invoiceBlueA, billDetailMoreVList,command) :
                this.doOrdinaryInvoice(invoiceBlueA, command);
        ErrorAssertUtils.isTrueThrow400(res, "开票失败");
        //开票成功
        invoiceReceiptRepository.save(invoiceReceiptE);
        invoiceRepository.save(invoiceE);
        invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailES);
        //记日志
        this.invoiceBatchLog(invoiceReceiptDetailES, invoiceReceiptE);
        return invoiceReceiptE.getId();

    }

    /**
     * 自定义收款明细开票金额
     * @param billDetailMoreVList
     * @param command
     * @return
     */
    public List<BillDetailMoreV> customizedInvoiceAmount(List<BillDetailMoreV> billDetailMoreVList,
            AddInvoiceCommand command) {
        List<InvoiceGatherDetailAmount> invoiceGatherDetailAmounts = command.getInvoiceGatherDetailAmounts();
        if (CollectionUtils.isNotEmpty(invoiceGatherDetailAmounts)) {
            List<BillDetailMoreV> newBillDetailMoreVList = new ArrayList<>();
            Map<Long, BillDetailMoreV> billMap = billDetailMoreVList.stream()
                    .collect(Collectors.toMap(BillDetailMoreV::getGatherDetailId, detail -> detail));
            invoiceGatherDetailAmounts.forEach(amount -> {
                BillDetailMoreV billDetailMoreV = billMap.get(amount.getGatherDetailId());
                if (billDetailMoreV.getCanInvoiceAmount() < amount.getInvoiceAmount()) {
                    log.error("开票单据:{}, 开票金额:{}", JSON.toJSONString(billDetailMoreV), amount.getInvoiceAmount());
                    throw BizException.throw400("开票金额不能大于可开票金额");
                }
                billDetailMoreV.setCanInvoiceAmount(amount.getInvoiceAmount());
                newBillDetailMoreVList.add(billDetailMoreV);
            });
            return newBillDetailMoreVList;
        }
        return billDetailMoreVList;
    }

    /**
     * 记录账单侧边栏流程日志
     * @param invoiceReceiptDetailES
     * @param invoiceReceiptE
     */
    public void invoiceBatchLog(List<InvoiceReceiptDetailE> invoiceReceiptDetailES,InvoiceReceiptE invoiceReceiptE){
        if (CollectionUtils.isNotEmpty(invoiceReceiptDetailES)){
            Map<Long, List<InvoiceReceiptDetailE>> detailMap = invoiceReceiptDetailES.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId));
            detailMap.forEach((billId, details) -> {
                long invoiceAmount = details.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
                BizLog.initiate(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.开票,
                        new Content().option(new ContentOption(new PlainTextDataItem("票据类型：" + InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType()).getDes(), true)))
                                .option(new ContentOption(new PlainTextDataItem("开票金额：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(invoiceAmount), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            });
        }
    }

    /**
     * 获取收款单明细(额外收集账单ids)
     * 场景：
     * 1. 账单开票：涉及到批量时有缴费和未缴费的
     * 2. 收款单开票：所有单子都是已缴费的
     * @param command
     * @return
     */
    public List<BillDetailMoreV> getBillDetailMoreVList(AddInvoiceCommand command) {
        List<BillDetailMoreV> billDetailMoreVS = new ArrayList<>();

        // 收款那边会传递此值，理论上不需要，所以清空
        if (command.getGatherBillType()!=null){
            command.setBillIds(null);
        }

        //获取收款单明细(额外收集账单ids)
        List<GatherDetail> gatherDetails = this.getGatherDetails(command);
        if (CollectionUtils.isNotEmpty(gatherDetails)) {
            billDetailMoreVS.addAll(this.batchGetGatherDetailBillDetail(gatherDetails, command.getSupCpUnitId()));
        }

        // HXYUN-19372 在批量开票场景中，处理未结算的账单
        List<Long> billIds = new ArrayList<>(command.getBillIds()==null?new ArrayList<>():command.getBillIds());
        if (CollectionUtils.isNotEmpty(gatherDetails) && CollectionUtils.isNotEmpty(billIds)) {
            // 删除所有已缴费账单id
            billIds.removeAll(gatherDetails.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()));
        }

        // 删除结束后 剩下的账单id都是未缴费的
        if (CollectionUtils.isNotEmpty(billIds)){
            billDetailMoreVS.addAll(this.batchGetBillDetail(billIds, command.getBillType(), command.getSupCpUnitId()));
        }

        List<BillDetailMoreV> detailMoreVS = billDetailMoreVS.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(detailMoreVS) && EnvConst.NIANHUAWAN.equals(EnvData.config)) {
            List<SpaceDetails> spaceDetails = spaceClient.getDetails(detailMoreVS.stream().filter(t -> StringUtils.isNotBlank(t.getRoomId()))
                    .map(t -> Long.parseLong(t.getRoomId())).distinct().collect(Collectors.toList()));
            for (BillDetailMoreV detailMoreV : detailMoreVS) {
                spaceDetails.stream().filter(t -> t.getId().equals(Long.parseLong(detailMoreV.getRoomId()))).findFirst().ifPresent(t -> detailMoreV.setTypeNameFlag(t.getTypeNameFlag()));
            }
        }
        return detailMoreVS;
    }

    /**
     * 批量获取账单信息
     *
     * @return
     */
    public List<BillDetailMoreV> batchGetBillDetail(List<Long> billIds, Integer billType,String supCpUnitId) {
        List<BillDetailMoreV> billDetailMoreVList = billFacade.getAllDetails(billIds, BillTypeEnum.valueOfByCode(billType), supCpUnitId);
        if (CollectionUtils.isEmpty(billDetailMoreVList)) {
            throw BizException.throw400("该账单不存在");
        }
        return billDetailMoreVList;
    }

    /**
     * 批量获取账单信息
     *
     * @return
     */
    public List<BillDetailMoreV> batchGetGatherDetailBillDetail(List<GatherDetail> gatherDetails,String supCpUnitId) {
        List<BillDetailMoreV> billDetailMoreVList = billFacade.batchGetGatherDetailBillDetail(gatherDetails, supCpUnitId);
        if (CollectionUtils.isEmpty(billDetailMoreVList)) {
            throw BizException.throw400("该账单不存在");
        }
        return billDetailMoreVList;
    }



    /**
     * 获取收款单明细(额外收集账单ids)
     * @param command
     * @return
     */
    public List<GatherDetail> getGatherDetails(AddInvoiceCommand command){
        List<GatherDetail> gatherDetails = null;
        InvoiceGatherTypeEnum invoiceGatherTypeEnum = Optional.ofNullable(command.getGatherBillType())
                .map(InvoiceGatherTypeEnum::valueOfByCode)
                .orElse(null);
        if(Optional.ofNullable(invoiceGatherTypeEnum).isPresent()){
            switch (invoiceGatherTypeEnum){
                case 收款单:
                    gatherDetails = gatherDetailRepository.queryGatherDetailList(command.getGatherBillIds(), command.getSupCpUnitId());
                    break;
                case 收款明细:
                    gatherDetails = gatherDetailRepository.queryByIdList(command.getGatherDetailBillIds(), command.getSupCpUnitId());
                    break;
            }
        }
        if(CollectionUtils.isEmpty(gatherDetails)){
            gatherDetails = gatherDetailRepository.queryByRecBillIdList(command.getBillIds(), command.getSupCpUnitId());
        }
        //判断相同账单多次缴费信息合并
        /*if(CollectionUtil.isNotEmpty(gatherDetails)){
            Map<Long, GatherDetail> idDetailMap = new HashMap<>();
            for (GatherDetail detail : gatherDetails) {
                Long recBillId = detail.getRecBillId();
                if (idDetailMap.containsKey(recBillId)) {
                    // 如果Map中已经存在相同id的数据，进行金额累加
                    GatherDetail existingDetail = idDetailMap.get(recBillId);
                    existingDetail.setRecPayAmount(existingDetail.getRecPayAmount() + detail.getRecPayAmount());
                    existingDetail.setDeductionAmount(existingDetail.getDeductionAmount() + detail.getDeductionAmount());
                    existingDetail.setPayAmount(existingDetail.getPayAmount() + detail.getPayAmount());
                    existingDetail.setCarriedAmount(existingDetail.getCarriedAmount() + detail.getCarriedAmount());
                    existingDetail.setRefundAmount(existingDetail.getRefundAmount() + detail.getRefundAmount());
                } else {
                    // 如果Map中不存在相同id的数据，直接放入Map
                    idDetailMap.put(recBillId, detail);
                }
            }
        }*/
        return gatherDetails;
    }

    /**
     * 发票全部红冲根据发票主表id
     *
     * @param invoiceReceiptId
     * @param invoiceReceiptNo 红冲发票号码
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean invoiceBatchRed(Long invoiceReceiptId, String invoiceReceiptNo) {
        //根据蓝票获取对应的红票信息
        List<InvoiceInfoDto> redInvoiceInfoBefore = invoiceRepository.getRedInvoiceInfo(invoiceReceiptId);
        // 构造红票信息
        InvoiceRedA invoiceRedA = getInvoiceBlueAggregate(invoiceReceiptId);
        if (invoiceRedA == null) {
            return true;
        }
        redValid(invoiceRedA);
//        if (invoiceRedA.getInvoiceReceiptRedE().getBillType() == BillTypeEnum.默认.getCode()) {
//            throw BizException.throw400("暂不支持此类账单红冲");
//        }
        //判断是否有拆分
        if (CollectionUtils.isEmpty(invoiceRedA.getInvoiceChildRedEList())) {
            Integer source = invoiceRedA.getInvoiceReceiptRedE().getSource();
            invoiceRedA.redByInvoiceReceiptId(invoiceReceiptId, redInvoiceInfoBefore);
            log.info("invoiceRedA is :{}", JSONObject.toJSONString(invoiceRedA));
            if (source == 2 && StringUtils.isNotBlank(invoiceReceiptNo)) {
                // 判断发票号是否重复
                int count = invoiceReceiptRepository.queryByRedInvoiceReceipt(invoiceReceiptNo);
                if (count > 0) {
                    throw BizException.throw400("发票号重复："+invoiceReceiptNo);
                }
                invoiceRedA.getInvoiceReceiptRedE().setInvoiceReceiptNo(invoiceReceiptNo);
                invoiceRedA.getInvoiceReceiptRedE().setState(InvoiceReceiptStateEnum.开票成功.getCode());
                invoiceRedA.getInvoiceRedE().setInvoiceNo(invoiceReceiptNo);
                invoiceRedA.setBlueSource(source);
            }
            String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
            saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
        } else {
            for (InvoiceChildE invoiceChildE : invoiceRedA.getInvoiceChildRedEList()) {
                invoiceRedA.redByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE);
                String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
                saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
            }
        }
        return true;
    }

    /**
     * 已认领流水的收款单，发票不能红冲
     * @param invoiceRedA
     */
    public void redValid(InvoiceRedA invoiceRedA) {
        if (EnvConst.LINGANG.equals(EnvData.config)) {
            List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList = invoiceRedA.getInvoiceReceiptDetailRedEList();
            List<Long> gatherBillIds = invoiceReceiptDetailRedEList.stream()
                    .map(InvoiceReceiptDetailE::getGatherBillId).filter(Objects::nonNull).collect(
                            Collectors.toList());
            if (CollectionUtils.isNotEmpty(gatherBillIds)) {
                List<FlowClaimDetailE> recGatherIdFlowClaimRecord = flowClaimRecordDomainService
                        .getRecGatherIdFlowClaimRecord(gatherBillIds,
                                invoiceRedA.getInvoiceReceiptRedE().getCommunityId());
                Assert.validate(() -> CollectionUtils.isEmpty(recGatherIdFlowClaimRecord), ()->BizException.throw400("无法冲销，已有部分收款单已认领流水。请您先解除认领"));
            }
        }
    }

    /**
     * 发票全部红冲根据账单id
     *
     * @param billId
     * @param invoiceReceiptId
     * @return
     */
    public Boolean invoicePartRedByBillId(Long billId, Long invoiceReceiptId) {
        InvoiceRedA invoiceRedA = getInvoiceBlueAggregate(invoiceReceiptId);
        if (invoiceRedA == null) {
            return true;
        }
        List<InvoiceInfoDto> redInvoiceInfoBefore = invoiceRepository.getRedInvoiceInfo(invoiceReceiptId);
        //根据账单id获取蓝票里面的开票金额
        List<InvoiceReceiptDetailE> invoiceReceiptDetailBlueEList = invoiceRedA.getInvoiceReceiptDetailRedEList();
        InvoiceReceiptDetailE detailBlueEList = invoiceReceiptDetailBlueEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId)).get(billId).get(0);
        Long blueInvoiceAmount = detailBlueEList.getInvoiceAmount();

        //根据账单id获取账单已经红冲的金额
        List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList = getInvoiceReceiptRedByBillId(invoiceReceiptId, billId);
        Long redInvoiceAmount = 0L;
        if (CollectionUtils.isNotEmpty(invoiceReceiptDetailRedEList)) {
            List<InvoiceReceiptDetailE> detailRedEList = invoiceReceiptDetailRedEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillId)).get(billId);
            redInvoiceAmount = detailRedEList.stream().mapToLong(InvoiceReceiptDetailE::getInvoiceAmount).sum();
        }
        //两者像减得到当次账单部分红冲的金额
        Long thisRedInvoiceAmount = blueInvoiceAmount + redInvoiceAmount;
        if (thisRedInvoiceAmount <= 0) {
            return true;
        }

        if (CollectionUtils.isEmpty(invoiceRedA.getInvoiceChildRedEList())) {
            if (InvoiceLineEnum.全电普票.getCode().equals(invoiceRedA.getInvoiceReceiptRedE().getType()) ||
                    InvoiceLineEnum.全电专票.getCode().equals(invoiceRedA.getInvoiceReceiptRedE().getType())) {
                invoiceRedA.redByInvoiceReceiptId(invoiceReceiptId, redInvoiceInfoBefore, null, null);
            } else {
                //根据账单id获取账单已经红冲的金额
                invoiceRedA.redByBillId(invoiceReceiptId, billId, thisRedInvoiceAmount, redInvoiceInfoBefore);
            }
            String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
            saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
        } else {
            for (InvoiceChildE invoiceChildE : invoiceRedA.getInvoiceChildRedEList()) {
                invoiceRedA.redBillIdByInvoiceChild(invoiceReceiptId, invoiceChildE, redInvoiceInfoBefore);
                String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
                saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
            }
        }
        return true;
    }

    /**
     * 根据账单id获取红票票据主表数据
     *
     * @param billId
     * @return
     */
    private List<InvoiceReceiptDetailE> getInvoiceReceiptRedByBillId(Long invoiceReceiptId, Long billId) {
        //根据蓝票找到红票
        List<InvoiceInfoDto> invoiceInfoDtoList = invoiceRepository.getRedInvoiceInfo(invoiceReceiptId, billId);
        if (CollectionUtils.isNotEmpty(invoiceInfoDtoList)) {
            List<InvoiceReceiptDetailE> detailEList = Lists.newArrayList();
            for (InvoiceInfoDto invoiceInfoDto : invoiceInfoDtoList) {
                InvoiceReceiptDetailE invoiceReceiptDetailE = invoiceReceiptDetailRepository.getByBillId(invoiceInfoDto.getInvoiceReceiptId(), billId).get(0);
                detailEList.add(invoiceReceiptDetailE);
            }
            return detailEList;
        }
        return null;
    }

    /**
     * 发票部分红冲,根据账单id和红冲金额
     *
     * @return
     */
    public Long invoicePartRedByBillIdAndRedAmount(Long billId, Long invoiceReceiptId, Long redAmount) {
        //根据蓝票获取对应的红票信息
        List<InvoiceInfoDto> redInvoiceInfoBefore = invoiceRepository.getRedInvoiceInfo(invoiceReceiptId);
        InvoiceRedA invoiceRedA = getInvoiceBlueAggregate(invoiceReceiptId);
        // 返回null说明为红票，返回应红冲金额继续循环
        if (invoiceRedA == null) {
            return redAmount;
        }

        //已经红冲了总金额（负数）
        Long hasRedAmountSum = redInvoiceInfoBefore.stream().mapToLong(InvoiceInfoDto::getPriceTaxAmount).sum();
        Long canRedAmount = invoiceRedA.getInvoiceReceiptRedE().getPriceTaxAmount() + hasRedAmountSum;

        Long secondRedAmount = 0L;
        if (redAmount > canRedAmount) {
            //如果红冲金额大于可以红冲的总金额，就需要循环进入下一张票进行红冲
            //下一次的红冲金额，返回给上游方便计算
            secondRedAmount = redAmount - canRedAmount ;
            //当此的实际红冲金额
            redAmount = canRedAmount;
        } else {
            // 全电普票先接入全额红冲，目前如果红冲金额小于可以红冲的金额，直接全部红冲
            redAmount = canRedAmount;
        }
        if (CollectionUtils.isEmpty(invoiceRedA.getInvoiceChildRedEList())) {
            invoiceRedA.redByInvoiceReceiptId(invoiceReceiptId, redInvoiceInfoBefore, null, null);
            String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
            saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
        } else {
            for (InvoiceChildE invoiceChildE : invoiceRedA.getInvoiceChildRedEList()) {
                //当前附属表可以红冲的金额
                Long canRedInvoiceAmount = invoiceChildE.getInvoiceAmount() + invoiceChildE.getRedInvoiceAmount();
                if (canRedInvoiceAmount == 0) {
                    continue;
                } else if (canRedInvoiceAmount - redAmount > 0) {
                    //此时红冲金额为 redamount
                    invoiceRedA.redByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE, redAmount);
                    String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
                    saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
                    break;
                } else {
                    //此时的红冲金额为 canRedInvoiceAmount
                    invoiceRedA.redByInvoiceChild(invoiceReceiptId, redInvoiceInfoBefore, invoiceChildE, canRedInvoiceAmount);
                    String invoiceSerialNum = redInvoiceByInvoiceRedAggregate(invoiceRedA);
                    saveInvoiceRepository(invoiceSerialNum, invoiceRedA);
                    redAmount = redAmount - canRedInvoiceAmount;
                }
            }
        }
        return secondRedAmount;
    }

    /**
     * 保存发票数据库资源
     *
     * @param invoiceSerialNum
     * @param invoiceRedA
     */
    private void saveInvoiceRepository(String invoiceSerialNum, InvoiceRedA invoiceRedA) {
        invoiceRedA.addInvoiceSerialNum(invoiceSerialNum);
        //重新生成红票开票时间
        invoiceRedA.getInvoiceReceiptRedE().generateInvoiceTime(curIdentityInfo());
        invoiceReceiptRepository.save(invoiceRedA.getInvoiceReceiptRedE());
        invoiceRepository.save(invoiceRedA.getInvoiceRedE());
        invoiceRedA.getInvoiceReceiptDetailRedEList().forEach(a -> a.setPath(Global.ac.getBean(
                SpacePermissionAppService.class).getSpacePath(a.getRoomId())));
        invoiceReceiptDetailRepository.saveBatch(invoiceRedA.getInvoiceReceiptDetailRedEList());
        if (CollectionUtils.isNotEmpty(invoiceRedA.getInvoiceChildRedEList())) {
            invoiceChildRepository.updateBatchById(invoiceRedA.getInvoiceChildRedEList());
        }
    }

    /**
     * 根据聚合对象红冲发票
     *
     * @param invoiceRedA
     */
    private String redInvoiceByInvoiceRedAggregate(InvoiceRedA invoiceRedA) {
        InvoiceReceiptE invoiceReceiptRedE = invoiceRedA.getInvoiceReceiptRedE();
        InvoiceE invoiceRedE = invoiceRedA.getInvoiceRedE();
        List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList = invoiceRedA.getInvoiceReceiptDetailRedEList();
        Integer invoiceType = invoiceReceiptRedE.getType();
        if(invoiceRedA.getBlueSource() != null && invoiceRedA.getBlueSource() == 2){
            log.info("红冲发票,不走线上");
            billFacade.handleBillStateFinishInvoice(invoiceReceiptDetailRedEList, true, invoiceReceiptRedE.getCommunityId());
            invoiceReceiptRepository.setInvoiceReceiptState(invoiceRedE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.已红冲);
            return null;
        }
        switch (InvoiceLineEnum.valueOfByCode(invoiceType)) {
            case 增值税电子发票:
                //获取对应的诺诺小区映射
                String nuonuoCommunityId = spaceFacade.getNuonuoCommunityId(invoiceReceiptRedE.getCommunityId());
                //根据费项ids获取税目编码
                Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = getTaxIteamMapByChargeId(invoiceRedA.getInvoiceReceiptDetailRedEList());
                String invoiceSerialNum;
                invoiceSerialNum = invoiceExternalService.nuonuoBillingNew(invoiceRedA.getInvoiceReceiptRedE(),
                        invoiceRedA.getInvoiceRedE(), invoiceRedA.getInvoiceReceiptDetailRedEList(), invoiceRedA.getInvoiceDetailDtoList(), nuonuoCommunityId, taxIteamMapByChargeId, invoiceReceiptRedE.getTenantId(), InvoiceTypeEnum.红票);
                // 原蓝票发票记录设置红冲中状态
                invoiceReceiptRepository.setInvoiceReceiptState(invoiceRedE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.红冲中);
                billFacade.invoiceBatch(invoiceReceiptDetailRedEList, invoiceReceiptRedE.getCommunityId());
                return invoiceSerialNum;

            case 全电普票:
            case 全电专票:
                // 发起红字确认单申请
//                ElectronInvoiceRedApplyF redApplyF = invoiceExternalService.generateRedApply(invoiceRedE, invoiceReceiptRedE.getTenantId(), String.valueOf(invoiceRedA.getInvoiceRedApplyE().getId()));
                String applyNo = invoiceExternalService.electronInvoiceRedApply(invoiceRedA);
                // 是否在这里加字段好一些，给apply信息添加字段
                if(EnvConst.LINGANG.equals(EnvData.config)){
                    invoiceRedA.getInvoiceRedApplyE().setInvoiceSerialNum(applyNo);
                    applyNo = null;
                }
                invoiceRedApplyRepository.save(invoiceRedA.getInvoiceRedApplyE());
                // 原蓝票发票记录设置红冲中状态
                invoiceReceiptRepository.setInvoiceReceiptState(invoiceRedE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.红字确认单申请中);
                billFacade.invoiceBatch(invoiceReceiptDetailRedEList, invoiceReceiptRedE.getCommunityId());
                return applyNo;
            default:
                if ( 2 == InvoiceLineEnum.valueOfByCode(invoiceType).getCode() && EnvConst.FANGYUAN.equals(EnvData.config)){
                    String billInfoNo = invoiceExternalService.invoiceRedApply(invoiceRedA.getInvoiceReceiptRedE(),
                            invoiceRedA.getInvoiceRedE(), invoiceRedA.getInvoiceReceiptDetailRedEList(), invoiceRedA.getInvoiceDetailDtoList(), spaceFacade.getNuonuoCommunityId(invoiceReceiptRedE.getCommunityId()), getTaxIteamMapByChargeId(invoiceRedA.getInvoiceReceiptDetailRedEList()), invoiceReceiptRedE.getTenantId(), InvoiceTypeEnum.红票);
                    invoiceSerialNum = invoiceExternalService.paperInvoices(invoiceRedA.getInvoiceReceiptRedE(),
                            invoiceRedA.getInvoiceRedE(), invoiceRedA.getInvoiceReceiptDetailRedEList(), invoiceRedA.getInvoiceDetailDtoList(), spaceFacade.getNuonuoCommunityId(invoiceReceiptRedE.getCommunityId()), getTaxIteamMapByChargeId(invoiceRedA.getInvoiceReceiptDetailRedEList()), invoiceReceiptRedE.getTenantId(), InvoiceTypeEnum.红票, billInfoNo);
                    invoiceReceiptRepository.setInvoiceReceiptState(invoiceRedE.getBlueInvoiceReceiptId(), InvoiceReceiptStateEnum.红冲中);
                    billFacade.invoiceBatch(invoiceReceiptDetailRedEList, invoiceReceiptRedE.getCommunityId());
                    return invoiceSerialNum;
                } else{
                    //其他非线上发票直接红冲对应金额
                    billFacade.invoiceVoidBatch(invoiceRedA.getInvoiceReceiptDetailRedEList(), invoiceReceiptRedE.getCommunityId());
                    return null;
                }

        }
    }

    /**
     * 根据发票主表id
     *
     * @param invoiceReceiptId
     * @return
     */
    private InvoiceRedA getInvoiceBlueAggregate(Long invoiceReceiptId) {

        //1.获取发票主表的信息
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        checkInvoiceReceipt(invoiceReceiptE.getType());
        //2.获取发票表的信息
        InvoiceE invoiceE = invoiceRepository.getByInvoiceReceiptId(invoiceReceiptE.getId());
        if (InvoiceTypeEnum.红票.getCode().equals(invoiceE.getInvoiceType())) {
            return null;
        }
        //3.获取发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(Lists.newArrayList(invoiceReceiptE.getId()));
        //4.获取发票子表的信息
        List<InvoiceChildE> invoiceChildEList = invoiceChildRepository.invoiceChildList(invoiceReceiptId);
        return new InvoiceRedA(invoiceReceiptE, invoiceE, invoiceReceiptDetailES, invoiceChildEList);
    }


    /**
     * 根据费项id获取税目编码
     *
     * @param invoiceReceiptDetailES
     * @return
     */
    private Map<Long, List<TaxChargeItemD>> getTaxIteamMapByChargeId(List<InvoiceReceiptDetailE> invoiceReceiptDetailES) {
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
        if (taxIteamMapByChargeId == null) {
            throw BizException.throw400("该批次费项未配置对应的税目编码");
        }
        return taxIteamMapByChargeId;
    }

    /**
     * 校检发票类型
     *
     * @param invoiceType
     */
    private void checkInvoiceReceipt(Integer invoiceType) {
        if (!invoiceType.equals(InvoiceLineEnum.增值税电子发票.getCode()) && !invoiceType.equals(InvoiceLineEnum.全电普票.getCode()) &&
                !invoiceType.equals(InvoiceLineEnum.全电专票.getCode()) && (!invoiceType.equals(InvoiceLineEnum.增值税专用发票.getCode())) && !EnvConst.FANGYUAN.equals(EnvData.config)) {
            throw BizException.throw400("您选择的" + InvoiceLineEnum.valueOfByCode(invoiceType).getDes() + "，暂不支持红冲");
        }
    }

    /**
     * 根据账单id列表查询票据信息
     *
     * @param billIds
     * @return
     */
    public List<InvoiceBillDetailDto> getInvoiceReceiptByBillIds(List<Long> billIds, List<Long> recIds) {
        return invoiceReceiptDetailRepository.getByBillAndInvoice(billIds, recIds);
    }


    /**
     * 获取发票代码
     * 1.校检发票号码是否被使用
     * 2.得到相应的发票代码
     *
     * @param invoiceNo
     * @param invoiceType
     * @return
     */
    private String getInvoiceCode(String invoiceNo, Integer invoiceType) {
        if (StringUtils.isNotBlank(invoiceNo)) {
            InvoiceE invoiceE = invoiceRepository.getByInvoiceNo(invoiceNo);
            if (null != invoiceE) {
                throw BizException.throw400("该发票号已被使用");
            }
            InvoiceBookE invoiceBookE = invoiceBookRepository.getInvoiceReceiptCode(invoiceNo, invoiceType);
            if (invoiceBookE == null) {
                throw BizException.throw400("该发票不存在");
            }
            return invoiceBookE.getInvoiceCode();
        }
        return null;
    }

    /**
     * 开票失败的处理
     *
     * @param invoiceE
     * @param invoicingState
     */
    public void updateInvoiceState(InvoiceE invoiceE, Integer invoicingState) {
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceE.getInvoiceReceiptId());
        invoiceReceiptE.setState(invoicingState);
        invoiceReceiptRepository.updateById(invoiceReceiptE);

        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.getBillIdsByInvoiceReceiptId(invoiceE.getInvoiceReceiptId());
        handleBillStateFinishInvoice(Global.mapperFacade.mapAsList(invoiceReceiptDetailES, AddInvoiceReceiptDetailCommand.class), false);
    }


    /**
     * 根据诺诺的结果更新开票结果信息
     *
     * @param invoiceE
     * @param invoiceReceiptE
     * @param queryInvoiceResultRVList
     * @param invoiceState
     */
    public void updateInvoiceInfo(InvoiceE invoiceE, InvoiceReceiptE invoiceReceiptE, List<QueryInvoiceResultV> queryInvoiceResultRVList, Integer invoiceState) {
        if (queryInvoiceResultRVList.size() == 1) {
            String invoiceUrl = pdfFileUpload(invoiceReceiptE.getId(),invoiceE.getTenantId(), FileUtil.getMultipartFile(queryInvoiceResultRVList.get(0).getPdfUrl()));
            invoiceRepository.updateInvoiceInfo(invoiceE, invoiceReceiptE, invoiceState, queryInvoiceResultRVList, invoiceUrl);
        } else {
            //由于税额限额度，保存子表数据
            List<InvoiceChildE> invoiceChildEList = Lists.newArrayList();
            List<String> invoiceUrlList = Lists.newArrayList();
            for (QueryInvoiceResultV invoiceResultRV : queryInvoiceResultRVList) {
                InvoiceChildE invoiceChildE = new InvoiceChildE();
                invoiceChildE.setInvoiceReceiptId(invoiceReceiptE.getId());
                invoiceChildE.setInvoiceCode(invoiceResultRV.getInvoiceCode());
                invoiceChildE.setInvoiceNo(invoiceResultRV.getInvoiceNo());
                invoiceChildE.setNuonuoUrl(invoiceResultRV.getPdfUrl());
                String invoiceUrl = pdfFileUpload(invoiceChildE.getId(),invoiceE.getTenantId(), FileUtil.getMultipartFile(invoiceResultRV.getPdfUrl()));
                invoiceUrlList.add(invoiceUrl);
                invoiceChildE.setInvoiceUrl(invoiceUrl);
                invoiceChildE.setState(invoiceState);
                invoiceChildE.setFailReason(invoiceResultRV.getFailCause());
                invoiceChildE.setInvoiceAmount(new BigDecimal(invoiceResultRV.getOrderAmount()).multiply(BigDecimal.valueOf(100)).longValue());
                invoiceChildE.setThridReturnParameter(JSON.toJSONString(invoiceResultRV));
                invoiceChildEList.add(invoiceChildE);
            }
            invoiceChildRepository.saveBatch(invoiceChildEList);
            String invoiceUrlListStr = StringUtils.join(invoiceUrlList, ",");
            invoiceRepository.updateInvoiceInfo(invoiceE, invoiceReceiptE, invoiceState, queryInvoiceResultRVList, invoiceUrlListStr);
        }
//        autoBindBill(invoiceReceiptE);
    }

    /**
     * 自动关联账单
     * @param invoiceReceiptE
     * @return
     */
    public boolean autoBindBill(InvoiceReceiptE invoiceReceiptE){
        if (!InvoiceReceiptStateEnum.开票成功.equalsByCode(invoiceReceiptE.getState())){
            return false;
        }
        //1.查询关联的账单信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetails = invoiceReceiptDetailRepository.getByInvoiceReceipt(invoiceReceiptE.getId());
        if (CollectionUtils.isEmpty(invoiceReceiptDetails)){
            //如果为空，则没有配置
            //根据业务单号配置业务信息
            //查询收款单必传项目ID
            if(StringUtils.isBlank(invoiceReceiptE.getCommunityId())) {
                throw new IllegalArgumentException("项目ID不存在,业务异常!");
            }
            List<GatherBill> gatherBills = gatherBillRepository.getByOutBusId(invoiceReceiptE.getInvRecUnitId(), invoiceReceiptE.getCommunityId());
            Long priceTaxAmount = invoiceReceiptE.getPriceTaxAmount();
            long billInvoiceAmount = 0; //账单开票金额
            Long remPriceTaxAmount = invoiceReceiptE.getPriceTaxAmount();
            List<InvoiceReceiptDetailE> invoiceReceiptDetailES = new ArrayList<>();
            List<GatherBill> invoiceGatherBills = new ArrayList<>();
            for (GatherBill gatherBill : gatherBills) {
                //可开票金额
                long invoicableAmount = gatherBill.invoicableAmount();
                if (invoicableAmount <= 0){
                    continue;
                }
                billInvoiceAmount = (invoicableAmount <= remPriceTaxAmount) ? invoicableAmount : remPriceTaxAmount;
                remPriceTaxAmount -= billInvoiceAmount;
                InvoiceReceiptDetailE invoiceReceiptDetailE = new InvoiceReceiptDetailE();
                invoiceReceiptDetailE.setInvoiceReceiptId(invoiceReceiptE.getId());
                invoiceReceiptDetailE.setTaxRate(null == gatherBill.getTaxRate()? "0.00000" :gatherBill.getTaxRate().toString());
                invoiceReceiptDetailE.setWithTaxFlag(1);
                invoiceReceiptDetailE.setBillId(gatherBill.getId());
                invoiceReceiptDetailE.setBillNo(gatherBill.getBillNo());
                invoiceReceiptDetailE.setBillType(BillTypeEnum.收款单.getCode());
                invoiceReceiptDetailE.setBillStartTime(gatherBill.getStartTime());
                invoiceReceiptDetailE.setBillEndTime(gatherBill.getEndTime());
                invoiceReceiptDetailE.setPriceTaxAmount(priceTaxAmount);
                invoiceReceiptDetailE.setInvoiceAmount(billInvoiceAmount);
                invoiceReceiptDetailE.setSettleAmount(gatherBill.getTotalAmount());
                invoiceReceiptDetailES.add(invoiceReceiptDetailE);
                gatherBill.invoice(billInvoiceAmount,true);
                invoiceGatherBills.add(gatherBill);
                if (remPriceTaxAmount <= 0){
                    break;
                }
            }
            gatherBillRepository.updateBatchById(invoiceGatherBills);
            invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailES);
        }
        return true;
    }

    /**
     * 将pdf上传到服务器
     */
    public String pdfFileUpload(Long invoiceReceiptId, String tenantId, MultipartFile multipartFile) {
        if (null != multipartFile) {
            FormalF formalFBuilder = FormalF.builder()
                    .clazz(QueryInvoiceResultRV.class)
                    .serverName("发票")
                    .tenantId(tenantId)
                    .fileOperationType(OperationEnum.Local)
                    .businessId(String.valueOf(invoiceReceiptId))
                    .build();
            FileVo fileVo = fileStorage.formalSaveWithInfo(multipartFile, formalFBuilder);
            return fileVo.getFileKey();
        }
        return null;
    }


    /**
     * 根据票据类型查询账单ids
     *
     * @param billIds
     * @param compare
     * @param values
     * @return
     */
    public List<InvoiceReceiptDetailV> getBillIdsByType(List<Long> billIds, Integer compare, JSONArray values) {
        return invoiceReceiptDetailRepository.getBillIdsByType(billIds, compare, values);
    }

    /**
     * 根据票据税率和账单ids查询账单ids
     *
     * @param billIds
     * @param compare
     * @param values
     * @return
     */
    public List<InvoiceReceiptDetailE> getBillIdsByBillIdsAndTaxRate(List<Long> billIds, Integer compare, JSONArray values) {
        return invoiceReceiptDetailRepository.getBillIdsByBillIdsAndTaxRate(billIds, compare, values);
    }

    /**
     * 查询账单对应红冲的票据
     *
     * @param invoiceReceiptIds
     * @param billId
     */
    public List<InvoiceBillDto> listPartRedInvoices(List<Long> invoiceReceiptIds, Long billId) {
        return invoiceReceiptDetailRepository.listPartRedInvoices(invoiceReceiptIds, billId);
    }


    /**
     * 根据账单id获取发票主表id
     *
     * @param billId
     */
    public Long getInvoiceReceiptIdByBillId(Long billId) {
        List<InvoiceReceiptDetailE> list = invoiceReceiptDetailRepository.list(
                new LambdaQueryWrapper<InvoiceReceiptDetailE>().eq(InvoiceReceiptDetailE::getBillId, billId));
        if(CollectionUtils.isEmpty(list)){
            return 0L;
        }
        return list.get(0).getInvoiceReceiptId();
    }





    /**
     * 录入进项发票
     *
     * @param command
     * @param billIds
     * @param voucher
     * @return
     */
    public Boolean enterInvoice(EnterInvoiceCommand command, List<Long> billIds, String voucher) {
        AddInvoiceCommand addInvoiceCommand = Global.mapperFacade.map(command, AddInvoiceCommand.class);
        addInvoiceCommand.setBillIds(billIds);
        List<BillDetailMoreV> billDetailMoreVList = this.getBillDetailMoreVList(addInvoiceCommand);
        if (CollectionUtils.isEmpty(billDetailMoreVList)) {
            throw BizException.throw400(ErrMsgEnum.BILL_NOT_FOUND.getErrMsg());
        }

        //录入进项发票到进项发票系统
        thirdSystemDomainService.lingshuitongVoucherInvoice(voucher, command.getInvoiceCode(), command.getInvoiceNo(), command.getType());

        //构建进项发票
        EnterInvoiceA enterInvoiceA = new EnterInvoiceA(command, billDetailMoreVList);
        Boolean res = billFacade.handleBillStateFinishInvoice(enterInvoiceA.getInvoiceReceiptDetailEList(), true, command.getSupCpUnitId());
        if (res) {
            invoiceReceiptRepository.save(enterInvoiceA.getInvoiceReceiptE());
            invoiceRepository.save(enterInvoiceA.getInvoiceE());
            enterInvoiceA.getInvoiceReceiptDetailEList().forEach(a->a.setPath(Global.ac.getBean(
                    SpacePermissionAppService.class).getSpacePath(a.getRoomId())));
            invoiceReceiptDetailRepository.saveBatch(enterInvoiceA.getInvoiceReceiptDetailEList());
        }
        return res;
    }

    /**
     * 收票
     * @param invoiceAS
     */
    public void collectBatchInvoice(List<InvoiceA> invoiceAS) {
        List<InvoiceReceiptE> invoiceReceipts = new ArrayList<>();
        List<InvoiceE> invoices = new ArrayList<>();
        List<InvoiceReceiptDetailE> invoiceReceiptDetails = new ArrayList<>();
        InvoiceE invoice;
        InvoiceReceiptE invoiceReceipt;
        List<InvoiceReceiptDetailE> invoiceReceiptDetailsList;
        for (InvoiceA invoiceA : invoiceAS) {
            invoiceReceipt = invoiceA.getInvoiceReceipt();
            if (Objects.nonNull(invoiceReceipt)){
                invoiceReceipts.add(invoiceReceipt);
            }
            invoice = invoiceA.getInvoice();
            if (Objects.nonNull(invoice)){
                invoices.add(invoice);
            }
            invoiceReceiptDetailsList = invoiceA.getInvoiceReceiptDetails();
            if (CollectionUtils.isNotEmpty(invoiceReceiptDetailsList)){
                invoiceReceiptDetails.addAll(invoiceReceiptDetailsList);
            }
        }
        invoiceReceiptDetails.forEach(a->a.setPath(Global.ac.getBean(
                SpacePermissionAppService.class).getSpacePath(a.getRoomId())));
        invoiceReceiptRepository.saveBatch(invoiceReceipts);
        invoiceRepository.saveBatch(invoices);
        invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetails);
    }

    /**
     * 收票并同步发票至进项发票系统
     * @param invoiceAS 发票信息
     * @param voucherType 凭证类型
     * @param voucherNo 凭证编码
     * @param syncSystem 同步系统 101灵税通（进项发票系统）
     */
    public boolean collectBatchAndSyncInvoice(List<InvoiceA> invoiceAS, String voucherType, String voucherNo, Integer syncSystem){
        collectBatchInvoice(invoiceAS);
        if (syncSystem.intValue() == 101){ //同步票据至进项发票系统
            try {
                syncInvoiceToLingShuiTong(invoiceAS, voucherType, voucherNo);
            }catch (Exception e){
                log.error("收票并同步发票至进项发票系统异常:", e);
                return false;
            }
        }
        return true;
    }


    /**
     * 同步凭证至灵税通（进项发票系统）
     * @param invoiceAS 发票信息
     * @param voucherType 凭证类型
     * @param voucherNo 凭证编码
     */
    public void syncInvoiceToLingShuiTong(List<InvoiceA> invoiceAS, String voucherType, String voucherNo){
        LingshuitongContentRF lingshuitongContentRF = new LingshuitongContentRF();
        lingshuitongContentRF.setTenantId(ApiData.API.getTenantId().get());
        lingshuitongContentRF.setVoucherType(voucherType);
        lingshuitongContentRF.setInvoices(invoiceAS.stream().map(item -> {
            InvoiceE invoice = item.getInvoice();
            InvoiceReceiptE invoiceReceipt = item.getInvoiceReceipt();
            LingshuitongInvoiceRF invoiceRF = new LingshuitongInvoiceRF();
            invoiceRF.setInvoiceCode(invoice.getInvoiceCode());
            invoiceRF.setInvoiceNo(invoice.getInvoiceNo());
            invoiceRF.setInvoiceKind(invoiceReceipt.getTypeName());
            invoiceRF.setVoucherNumber(voucherNo);
            return invoiceRF;
        }).collect(Collectors.toList()));

       externalClient.invoiceVoucherInvoice(lingshuitongContentRF);
    }

    /**
     * 根据票据id获取
     * @param invoiceReceiptIds
     */
    public List<InvoiceReceiptDetailE> queryDetailByIds(List<Long> invoiceReceiptIds) {
        return invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(invoiceReceiptIds);
    }


    /**
     * 根据发票收据id获取发票对象
     *
     * @param invoiceReceiptIds
     * @return
     */
    public List<InvoiceE> getInvoiceByInvoiceReceiptId(List<Long> invoiceReceiptIds) {
        LambdaQueryWrapper<InvoiceE> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(InvoiceE::getInvoiceReceiptId, invoiceReceiptIds);
       return invoiceRepository.list(wrapper);
    }

    public InvoiceReceiptE getInvoiceReceipt(Long invoiceReceiptId) {
        return invoiceReceiptRepository.getById(invoiceReceiptId);
    }

    /**
     * 开具蓝票（无校检）
     *
     * @param form
     * @return
     */
    public Long invoiceBatchBlue(InvoiceBatchBlueF form) {
        //构造蓝票(无校检)
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA(form);
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();

        invoiceE.setInvoiceSerialNum(invoiceExternalService.nuonuoBillingNew(invoiceReceiptE, invoiceE,
                invoiceReceiptDetailES, null, null, null, ApiData.API.getTenantId().get(), InvoiceTypeEnum.蓝票));

        invoiceReceiptRepository.save(invoiceReceiptE);
        invoiceRepository.save(invoiceE);
        //过滤没有账单的明细
        List<InvoiceReceiptDetailE> receiptDetailES = invoiceReceiptDetailES.stream().filter(item -> Objects.nonNull(item.getBillId())).collect(Collectors.toList());
        receiptDetailES.forEach(a->a.setPath(Global.ac.getBean(
                SpacePermissionAppService.class).getSpacePath(a.getRoomId())));
        if (CollectionUtils.isNotEmpty(receiptDetailES)){
            invoiceReceiptDetailRepository.saveBatch(invoiceReceiptDetailES);
        }
        return invoiceReceiptE.getId();
    }

    /**
     * 根据票据主表id获取票据详情列表
     * @param list
     */
    public List<InvoiceReceiptDetailE> listByInvoiceReceiptId(List<Long> list) {
        return invoiceReceiptDetailRepository.queryByInvoiceReceiptIds(list);
    }

    /**
     * 发送发票信息
     * @param sendInvoiceCommand
     */
    public List<InvoiceSendDto> sendInvoiceByBillId(SendInvoiceCommand sendInvoiceCommand) {
        List<InvoiceSendDto> result = new ArrayList<>();
        //根据收款单id获取账单id
        List<Long> billIds = gatherDetailRepository.getBillIds(sendInvoiceCommand.getBillIds(), BillTypeEnum.valueOfByCode(sendInvoiceCommand.getBillType()));
        //查询票据信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceReceiptDetailRepository.listByBillIds(billIds);
        List<Long> invoiceReceiptDetailIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getInvoiceReceiptId).distinct().collect(Collectors.toList());
        List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.listByIds(invoiceReceiptDetailIds);
        List<InvoiceReceiptE> invoiceEList = new ArrayList<>();
        List<InvoiceReceiptE> receiptEList = new ArrayList<>();
        List<Long> invoiceRelevanceIds = new ArrayList<>();
        List<Long> receiptRelevanceIds = new ArrayList<>();
        for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptES) {
            switch (InvoiceLineEnum.valueOfByCode(invoiceReceiptE.getType())) {
                case 增值税电子发票:
                case 增值税电子专票:
                case 全电普票:
                case 全电专票:
                    invoiceRelevanceIds.add(invoiceReceiptE.getId());
                    invoiceEList.add(invoiceReceiptE);
                    break;
                case 电子收据:
                    receiptRelevanceIds.add(invoiceReceiptE.getId());
                    receiptEList.add(invoiceReceiptE);
                    break;
            }
        }
        List<InvoiceMessageDto> invoiceMessages = invoiceRepository.getInvoiceMessages(invoiceRelevanceIds).stream()
                .filter(m -> StringUtils.isNotBlank(m.getNuonuoUrl())).collect(Collectors.toList());
        List<ReceiptMessageDto> receiptMessages = receiptRepository.getReceiptMessages(receiptRelevanceIds).stream()
                .filter(m -> StringUtils.isNotBlank(m.getReceiptUrl())).collect(Collectors.toList());
        log.info("发送发票查询信息：{}", JSON.toJSONString(invoiceMessages));
        List<Bill> billList = BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(sendInvoiceCommand.getBillType())).listByIds(billIds);
        if (CollectionUtils.isNotEmpty(invoiceMessages)) {
            sendInvoiceInfo(invoiceMessages, sendInvoiceCommand, result, invoiceEList, billList);
        } else if (CollectionUtils.isNotEmpty(receiptMessages)) {
            sendReceiptInfo(receiptMessages, sendInvoiceCommand, result, billList);
        } else {
            throw BizException.throw400("未找到账单对应的电子发票或收据");
        }
        return result;
    }


    public void sendReceiptInfo(List<ReceiptMessageDto> receiptMessages, SendInvoiceCommand sendInvoiceCommand, List<InvoiceSendDto> result, List<Bill> billList) {
        String statutoryBodyName = billList.get(0).getStatutoryBodyName();
        int receiptNum = 0;
        Set<String> urlSet = new HashSet<>();
        for (ReceiptMessageDto invoiceMessage : receiptMessages) {
            urlSet.add(invoiceMessage.getReceiptUrl());
            receiptNum++;
        }
        String receiptStr = String.join("、", urlSet);
        String tenantId = getTenantId().get();
        OrgTenantRv orgTenantRv = orgClient.tenantGetById(tenantId);
        String shortName = orgTenantRv.getShortName() == null ? "" : orgTenantRv.getShortName();
        String roomName = billList.stream().map(Bill::getRoomName).distinct().filter(Objects::nonNull).collect(Collectors.joining("、"));
        String communityName = billList.stream().map(Bill::getCommunityName).distinct().filter(Objects::nonNull).collect(Collectors.joining("、"));
        String subject = TextContentUtil.getEmailSubject(TextContentEnum.收据补发, new String[]{String.valueOf(receiptNum), statutoryBodyName});
        String content = TextContentUtil.getEmailContent(TextContentEnum.收据补发,
                new String[] {roomName, shortName, String.valueOf(receiptNum), receiptStr, communityName});
        for (Integer mode : sendInvoiceCommand.getPushModes()) {
            switch (PushModeEnum.valueOfByCode(mode)) {
                case 邮箱:
                    try {
                        msgFacade.sendEmail(sendInvoiceCommand.getEmail(), subject, content, null);
                    } catch (Exception e) {
                        log.error("邮箱发送收据信息失败", e);
                        result.add(new InvoiceSendDto(mode, false));
                    }
                    break;
                case 手机:
                    if (EnvData.msgFlag) {
                        try {
                            CommunityShortRV communityInfo = spaceClient.getCommunityInfo(billList.get(0).getCommunityId());
                            String contactsPhone = communityInfo == null ? "" : communityInfo.getContactsPhone();
                            msgFacade.smsReceipt(sendInvoiceCommand.getBuyerPhone(), shortName, roomName, receiptStr, contactsPhone);
                        } catch (Exception e) {
                            log.error("短信发送收据信息失败", e);
                            result.add(new InvoiceSendDto(mode, false));
                        }
                    } else {
                        log.info("InvoiceDomainService.sendReceiptInfo:{}", "不发送短信收据");
                    }
                    break;
            }
        }
    }

    /**
     * 发送发票信息
     * @param invoiceMessages
     * @param sendInvoiceCommand
     * @param result
     */
    public void sendInvoiceInfo(List<InvoiceMessageDto> invoiceMessages, SendInvoiceCommand sendInvoiceCommand,
                                List<InvoiceSendDto> result, List<InvoiceReceiptE> invoiceEList, List<Bill> billList) {
        StringBuilder invoiceStr = new StringBuilder();
        int invoiceNum = 0;
        Set<String> urlSet = new HashSet<>();
        Set<String> invoiceNos = new HashSet<>();
        String invRecUnitName = invoiceEList.get(0).getInvRecUnitName();
        for (InvoiceMessageDto invoiceMessage : invoiceMessages) {
            urlSet.add(invoiceMessage.getNuonuoUrl());
            invoiceNum++;
            invoiceNos.add(invoiceMessage.getInvoiceNo());
            invoiceStr.append("发票代码：").append(invoiceMessage.getInvoiceCode())
                    .append("  发票号码：").append(invoiceMessage.getInvoiceNo())
                    .append("，您可以点击“电子普通发票下载”（").append(invoiceMessage.getNuonuoUrl()).append("）获取该发票文件，");
        }
        String invoiceUrls = String.join("，", urlSet);
        String roomName = billList.stream().map(Bill::getRoomName).distinct().filter(Objects::nonNull).collect(Collectors.joining("、"));
        String tenantId = getTenantId().get();
        OrgTenantRv orgTenantRv = orgClient.tenantGetById(tenantId);
        String shortName = orgTenantRv.getShortName() == null ? "" : orgTenantRv.getShortName();
        String customerName = invoiceEList.stream().map(InvoiceReceiptE::getCustomerName).filter(Objects::nonNull).collect(Collectors.joining("、"));
        String communityName = invoiceEList.stream().map(InvoiceReceiptE::getCommunityName).filter(Objects::nonNull).collect(Collectors.joining("、"));
        CommunityShortRV communityInfo = spaceClient.getCommunityInfo(invoiceEList.get(0).getCommunityId());
        String contactsPhone = communityInfo == null ? "" : communityInfo.getContactsPhone();
        String invoiceNo = String.join(",", invoiceNos);
        String subject = TextContentUtil.getEmailSubject(TextContentEnum.发票补发, new String[]{String.valueOf(invoiceNum), invRecUnitName, invoiceNo});
        String content = TextContentUtil.getEmailContent(TextContentEnum.发票补发,
                new String[] {customerName, shortName, String.valueOf(invoiceNum), invoiceStr.toString(), communityName});
        for (Integer mode : sendInvoiceCommand.getPushModes()) {
            switch (PushModeEnum.valueOfByCode(mode)) {
                case 邮箱:
                    try {
                        msgFacade.sendEmail(sendInvoiceCommand.getEmail(), subject, content, null);
                    } catch (Exception e) {
                        log.error("邮箱发送发票信息失败", e);
                        result.add(new InvoiceSendDto(mode, false));
                    }
                    break;
                case 手机:
                    if (EnvData.msgFlag) {
                        try {
                            msgFacade.smsInvoice(sendInvoiceCommand.getBuyerPhone(), shortName,
                                    roomName, invoiceUrls, contactsPhone);
                        } catch (Exception e) {
                            log.error("短信发送发票信息失败", e);
                            result.add(new InvoiceSendDto(mode, false));
                        }
                    } else {
                        log.info("InvoiceDomainService.sendInvoiceInfo:{}", "不发送短信收据");
                    }
                    break;
            }
        }
    }


    public List<InvoiceA> getListByIds(List<Long> invoiceIds) {
        List<InvoiceA> invoiceAList = new ArrayList<>();
        List<InvoiceReceiptE> invoiceReceiptES = invoiceReceiptRepository.listByIds(invoiceIds);
        if (CollectionUtils.isNotEmpty(invoiceReceiptES)){
            for (InvoiceReceiptE invoiceReceiptE : invoiceReceiptES) {
                invoiceAList.add(new InvoiceA(invoiceReceiptE, invoiceRepository.getByInvoiceReceiptId(invoiceReceiptE.getId()), null));
            }
        }
        return invoiceAList;
    }

    public InvoiceE getByInvoiceSerialNum(String invoiceSerialNum) {
        QueryWrapper<InvoiceE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invoice_serial_num", invoiceSerialNum);
        return invoiceRepository.getOne(queryWrapper);
    }

    public InvoiceBlueA getByInvoiceReceiptId(Long invoiceReceiptId) {
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA();
        InvoiceE invoiceE = invoiceRepository.getByInvoiceReceiptId(invoiceReceiptId);
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        invoiceBlueA.setInvoiceE(invoiceE);
        invoiceBlueA.setInvoiceReceiptE(invoiceReceiptE);
        return invoiceBlueA;
    }

    @Transactional
    public Boolean deleteInvoice(Long invoiceReceiptId) {
        InvoiceE invoiceE = invoiceRepository.getByInvoiceReceiptId(invoiceReceiptId);
        if (Objects.isNull(invoiceE)) {
            throw BizException.throw300("发票不存在");
        }
        InvoiceReceiptE invoiceReceiptE = invoiceReceiptRepository.getById(invoiceReceiptId);
        if (!InvoiceReceiptSourceEnum.系统导入.equalsByCode(invoiceReceiptE.getSource())) {
            throw BizException.throw300("只允许删除导入的发票");
        }
        if (!SysSourceEnum.未知系统.equalsByCode(invoiceReceiptE.getSysSource())) {
            throw BizException.throw300("已认领的发票不支持删除");
        }
        invoiceRepository.deletePhysically(invoiceReceiptId);
        invoiceReceiptRepository.deletePhysically(invoiceReceiptId);
        invoiceReceiptDetailRepository.deletePhysically(invoiceReceiptId);
        return true;
    }

    public String getInvoicePrintF(InvoicePrintF form) {
        String tenantId = Optional.ofNullable(ThreadLocalUtil.curIdentityInfo()).map(IdentityInfo::getTenantId).get();
        return invoiceExternalService.invoicesPrints(form, tenantId);
    }

    /**
     * 查询老的没有绑定收款单信息的发票的项目id
     * @return
     */
    public List<String> findOldCommunityToRefresh() {
        return invoiceReceiptDetailRepository.findOldCommunityToRefresh();
    }


    public InvoicePreviewV getInvoicePreviewV(AddInvoiceCommand command, List<BillDetailMoreV> billDetailMoreVList) {
        InvoicePreviewV invoicePreviewV = Global.mapperFacade.map(command, InvoicePreviewV.class);
        ampFinanceFacade.queryByGoodsList(billDetailMoreVList);
        //通过账单的法定单位获取法定单位税号作为销售方信息
        OrgFinanceRv orgFinanceRv = accountOrgFacade.getOrgFinance(billDetailMoreVList.get(0).getStatutoryBodyId());
        //构造蓝票(包含校检)
        InvoiceBlueA invoiceBlueA = new InvoiceBlueA(billDetailMoreVList, command, orgFinanceRv);
        invoicePreviewV.setSalerName(StringUtils.isBlank(command.getInvRecUnitName()) ? orgFinanceRv.getNameCn() : command.getInvRecUnitName());
        invoicePreviewV.setSalerTaxNum(StringUtils.isBlank(command.getSalerTaxNum()) ? orgFinanceRv.getTaxpayerNo() : command.getSalerTaxNum());
        invoicePreviewV.setSalerTel(StringUtils.isBlank(command.getSalerTel()) ? orgFinanceRv.getMobile() : command.getSalerTel());
        invoicePreviewV.setSalerAddress(StringUtils.isBlank(command.getSalerAddress()) ? orgFinanceRv.getAddress() : command.getSalerAddress());
        invoicePreviewV.setSalerAccount(command.getSalerAccount());
        invoicePreviewV.setRemark(StringUtils.isEmpty(invoicePreviewV.getRemark()) ? invoiceBlueA.getInvoiceReceiptE().getRemark() : invoicePreviewV.getRemark());
        //开票之前添加销方名称
        invoiceExternalService.preInvoice(invoiceBlueA);
        InvoiceReceiptE invoiceReceiptE = invoiceBlueA.getInvoiceReceiptE();
        InvoiceE invoiceE = invoiceBlueA.getInvoiceE();
        //发票明细表信息
        List<InvoiceReceiptDetailE> invoiceReceiptDetailES = invoiceBlueA.getInvoiceReceiptDetailEList();
        // 封装商品明细
        //补齐税目
        List<Long> chargeItemIds = invoiceReceiptDetailES.stream().map(InvoiceReceiptDetailE::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = ampFinanceFacade.queryByChargeIdList(chargeItemIds);
        ErrorAssertUtils.isFalseThrow400(Objects.isNull(taxIteamMapByChargeId),"该批次费项未配置对应的税目编码");
        Map<String, List<InvoiceReceiptDetailE>> invoiceReceiptDetailGroupMap = invoiceReceiptDetailES.stream().collect(
                Collectors.groupingBy(
                        detail -> detail.getChargeItemId() + "-" + detail.getTaxRate()
                ));

        List<InvoicePreviewDetailV> detailList = new ArrayList<>();
        for (Map.Entry<String, List<InvoiceReceiptDetailE>> stringListEntry : invoiceReceiptDetailGroupMap.entrySet()) {
            InvoicePreviewDetailV invoicePreviewDetailV =  getInvoicePreviewDetailV(invoiceE, stringListEntry.getValue(), taxIteamMapByChargeId);
            detailList.add(invoicePreviewDetailV);
        }
        BigDecimal sumGoodsAmount = new BigDecimal("0");
        BigDecimal sumTaxAmount = new BigDecimal("0");
        BigDecimal sumTaxIncludedAmount = new BigDecimal("0");
        for (InvoicePreviewDetailV invoicePreviewDetailV : detailList) {
            sumGoodsAmount = sumGoodsAmount.add(new BigDecimal(invoicePreviewDetailV.getGoodsAmount()));
            sumTaxAmount = sumTaxAmount.add(new BigDecimal(invoicePreviewDetailV.getTaxAmount()));
        }
        sumTaxIncludedAmount = sumTaxIncludedAmount.add(sumTaxAmount).add(sumGoodsAmount);
        invoicePreviewV.setSumGoodsAmount(sumGoodsAmount.toString());
        invoicePreviewV.setSumTaxAmount(sumTaxAmount.toString());
        invoicePreviewV.setSumTaxIncludedAmount(sumTaxIncludedAmount.toString());
        invoicePreviewV.setChineseAmount(ChineseNumber.number2CNMontrayUnit(String.valueOf(sumTaxIncludedAmount)));
        invoicePreviewV.setDetailList(detailList);
        invoicePreviewV.setInvoiceDate(DateTimeUtil.formatCNDate(LocalDate.now()));
        return invoicePreviewV;
    }

    private  InvoicePreviewDetailV getInvoicePreviewDetailV(InvoiceE invoiceE,
                                                                  List<InvoiceReceiptDetailE> invoiceReceiptDetailES,
                                                                  Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId) {
        InvoicePreviewDetailV invoicePreviewDetailV = new InvoicePreviewDetailV();

        BigDecimal taxExcludedAmountSum = BigDecimal.ZERO;
        BigDecimal taxSum = BigDecimal.ZERO;
        BigDecimal invoiceAmountSum = BigDecimal.ZERO;
        Integer freeTax = invoiceE.getFreeTax();
        for (InvoiceReceiptDetailE invoiceReceiptDetailE : invoiceReceiptDetailES) {
            String unit = billMethodUnitMap.get(invoiceReceiptDetailE.getUnit());
            if (StringUtils.isNotBlank(unit)) {
                invoicePreviewDetailV.setGoodsUnit(unit);
            }
            invoicePreviewDetailV.setGoodsCount("1");
            invoicePreviewDetailV.setTaxRate(invoiceReceiptDetailE.getTaxRate());
            BigDecimal detailTaxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate());
            //零税率标识  1,免税;2,不征税;3,普通零税率；
            if (StringUtils.isBlank(invoiceReceiptDetailE.getTaxRate()) || invoiceReceiptDetailE.getTaxRate().equals("0.00000")) {
                //暂定普通零税率
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                taxExcludedAmountSum = taxExcludedAmountSum.add(invoiceAmountSum);
            } else {
                BigDecimal invoiceAmount = BigDecimal.valueOf(invoiceReceiptDetailE.getInvoiceAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
                BigDecimal oneAddTaxrate = new BigDecimal(1).add(detailTaxRate);

                //含税金额
                invoiceAmountSum = invoiceAmountSum.add(invoiceAmount);
                //不含税金额 = 含税金额/(1+税率)
                BigDecimal taxExcludedAmount = invoiceAmount.divide(oneAddTaxrate, 2, RoundingMode.HALF_EVEN);
                taxExcludedAmountSum = taxExcludedAmountSum.add(taxExcludedAmount);

                //税额=(数量*含税单价)*税率/(1+税率）
                BigDecimal denominator = new BigDecimal(1).multiply(invoiceAmount)
                        .multiply(detailTaxRate);
                BigDecimal tax = denominator.divide(oneAddTaxrate, 2, RoundingMode.HALF_EVEN);
                taxSum = taxSum.add(tax);
            }
            String name = taxIteamMapByChargeId.get(invoiceReceiptDetailE.getChargeItemId()).get(0).getTaxItem().getName();
            String chargeItemName = invoiceReceiptDetailE.getChargeItemName();
            invoicePreviewDetailV.setGoodsName("*" + name + "*" + chargeItemName);
        }
        invoicePreviewDetailV.setGoodsCount("1");
        BigDecimal oneAddTaxrate = new BigDecimal(1).add(new BigDecimal(invoicePreviewDetailV.getTaxRate()));
        invoicePreviewDetailV.setUnitPrice(invoiceAmountSum.divide(oneAddTaxrate, 8, BigDecimal.ROUND_HALF_EVEN).toString());
        if (freeTax != null && freeTax == 1) {
            invoicePreviewDetailV.setGoodsAmount(invoicePreviewDetailV.getGoodsAmount());
        }else {
            //不含税金额 = 含税金额/(1+税率)
            BigDecimal taxExcludedAmount = invoiceAmountSum.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
            invoicePreviewDetailV.setGoodsAmount(taxExcludedAmount.toString());
        }

        if (freeTax != null && freeTax == 1) {
            invoicePreviewDetailV.setTaxAmount("0.00");
        } else {
            //税额=(数量*含税单价)*税率/(1+税率）
            BigDecimal denominator = new BigDecimal(1).multiply(invoiceAmountSum).multiply(new BigDecimal(invoicePreviewDetailV.getTaxRate()));
            BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
            invoicePreviewDetailV.setTaxAmount(tax.toString());
        }
        return invoicePreviewDetailV;
    }

    public List<InvoiceDto> queryQuotaListByCommunityId(String communityId) {
        return invoiceRepository.queryQuotaListByCommunityId(communityId);
    }


    /**
     * 计费方式与发票单位对应参数映射，计费方式参考枚举 BillMethodEnum
     */
    private static final Map<String, String> billMethodUnitMap =
            Map.of("5","度",
                    "6", "立方米（方）",
                    "7", "吨");

    public void batchDownloadZip(PageF<SearchF<?>> form, HttpServletResponse response) {
        String fileName = "大有秋发票" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
        Page<InvoiceDto> invoiceDtoPage = invoiceRepository.queryPage(form);
        List<InvoiceDto> invoices = invoiceDtoPage.getRecords();
        log.info("批量下载大有秋发票，查询数量：{}",invoices.size());
        long start = System.currentTimeMillis();
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

            for (InvoiceDto invoice : invoices) {
                String url = invoice.getInvoiceUrl();
                String name = invoice.getInvoiceNo();
                log.info("url is :{},name is: {}",url,name);
                if (StringUtils.isBlank(url)) {
                    continue;
                }

                ZipUtils.doWrite(fileHost + url, name, zipOut);
            }
            long timeConsume = System.currentTimeMillis() - start;
            log.info("批量下载大有秋收据，下载完成，耗时：{}毫秒",timeConsume);
            zipOut.finish(); // 确保所有数据都已写入响应中并关闭流

        } catch (IOException e) {
            // 处理ZIP创建或写入错误
            e.printStackTrace();
        }
    }

    public List<InvoiceDto> queryInvoiceDetailByGatherBillIds(List<Long> gatherBillIds) {
        return invoiceReceiptRepository.queryInvoiceDetailByGatherBillIds(gatherBillIds);
    }
    /**
     * 更新票据费用报账推送状态（独立新事务）
     * @param invoiceReceiptE
     * @param pushState
     * @param errMsg
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateExpenseReportPushStateWithNewTransaction(InvoiceReceiptE invoiceReceiptE, KingDeePushStateEnum pushState, String errMsg) {
        updateExpenseReportPushState(invoiceReceiptE, pushState, errMsg);
    }

    /**
     * 更新票据费用报账推送状态
     * @param invoiceReceiptE
     * @param pushState
     * @param errMsg
     */
    public void updateExpenseReportPushState(InvoiceReceiptE invoiceReceiptE, KingDeePushStateEnum pushState, String errMsg) {
        invoiceReceiptE.setExtendFieldTwo(pushState.getCode());
        invoiceReceiptE.setExtendFieldThree(errMsg);
        invoiceReceiptRepository.updateById(invoiceReceiptE);
    }

}
