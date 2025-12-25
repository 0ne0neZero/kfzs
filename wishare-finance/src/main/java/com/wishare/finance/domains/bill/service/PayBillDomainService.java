package com.wishare.finance.domains.bill.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BatchBillInferenceF;
import com.wishare.finance.apps.model.bill.vo.BillCarryoverV;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.PayBillDetailV;
import com.wishare.finance.apps.model.bill.vo.PayBillV;
import com.wishare.finance.domains.bill.command.AddPayBillCommand;
import com.wishare.finance.domains.bill.command.AddPayDetailCommand;
import com.wishare.finance.domains.bill.command.BillUnitaryEnterCommand;
import com.wishare.finance.domains.bill.command.StatisticsBillTotalQuery;
import com.wishare.finance.domains.bill.consts.enums.BillActionEventEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillCarriedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.BillUnitaryEnterResultDto;
import com.wishare.finance.domains.bill.dto.PayBillDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.PayBill;
import com.wishare.finance.domains.bill.entity.PayDetail;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.repository.PayBillRepository;
import com.wishare.finance.domains.bill.repository.PayDetailRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.AssisteAccountE;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportPayBillData;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应付账单领域
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayBillDomainService {

    private final PayBillRepository payBillRepository;

    private final BillCarryoverRepository carryoverRepository;

    private final PayDetailRepository payDetailRepository;

    /**
     * 新增付款单
     *
     * @param command 新增付款单命令
     * @return PayBillDetailV
     */
    @Transactional
    public PayBillV addBill(AddPayBillCommand command) {
        PayBill payBill = Global.mapperFacade.map(command, PayBill.class);
        initPayBill(payBill);
        PayDetail payDetail = Global.mapperFacade.map(command.getAddPayDetailCommands().get(0), PayDetail.class);
        payDetail.setPayBillId(payBill.getId());
        payDetail.setPayBillNo(payBill.getBillNo());

        payBillRepository.save(payBill);
        payDetailRepository.save(payDetail);
        BizLog.normal(String.valueOf(payBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        return Global.mapperFacade.map(payBill, PayBillV.class);
    }

    /**
     * 批量新增付款单
     *
     * @param commandList 付款单参数
     * @return List
     */
    @Transactional
    public List<PayBillV> addBatch(List<AddPayBillCommand> commandList) {
        List<PayBill> payBillList = Lists.newArrayList();
        List<PayDetail> payDetailList = Lists.newArrayList();

        commandList.forEach(command ->{
            PayBill payBill = Global.mapperFacade.map(command, PayBill.class);
            initPayBill(payBill);
            List<AddPayDetailCommand> addPayDetailCommands = command.getAddPayDetailCommands();
            for (AddPayDetailCommand addPayDetailCommand : addPayDetailCommands) {
                PayDetail payDetail = Global.mapperFacade.map(addPayDetailCommand, PayDetail.class);
                payDetail.setPayBillId(payBill.getId());
                payDetail.setPayBillNo(payBill.getBillNo());
                payDetailList.add(payDetail);
            }
            payBillList.add(payBill);
        });
        payBillRepository.saveBatch(payBillList);
        payDetailRepository.saveBatch(payDetailList);
        BizLog.normalBatch(payBillList.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList()),
                LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content());
        return Global.mapperFacade.mapAsList(payBillList, PayBillV.class);
    }

    /**
     * 分页查询已审核付款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<PayBillV> getApprovedPage(PageF<SearchF<?>> queryF) {
        queryF.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
        queryF.getConditions().getFields().removeIf(a-> "b.room_id".equals(a.getName()));
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        //添加正常账单条件
        queryWrapper.ne("b.approved_state", BillApproveStateEnum.待审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        normalBillCondition(queryWrapper);
        IPage<PayBillDto> payBillPage = payBillRepository.queryPage(Page.of(queryF.getPageNum(),queryF.getPageSize()), queryWrapper);
        return PageV.of(payBillPage.getCurrent(), payBillPage.getSize(), payBillPage.getTotal(), Global.mapperFacade.mapAsList(payBillPage.getRecords(), PayBillV.class));
    }

    /**
     * 分页查询付款单列表
     *
     * @param queryF 分页入参
     * @return PageV
     */
    public PageV<PayBillV> getPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        IPage<PayBillDto> payBillPage = payBillRepository.queryPage(Page.of(queryF.getPageNum(),queryF.getPageSize()), queryWrapper);
        return PageV.of(payBillPage.getCurrent(), payBillPage.getSize(), payBillPage.getTotal(), Global.mapperFacade.mapAsList(payBillPage.getRecords(), PayBillV.class));
    }

    /**
     * 分页查询未审核付款单列表
     *
     * @param queryF 查询条件
     * @return PageV
     */
    public PageV<PayBillV> queryNotApprovedPage(PageF<SearchF<?>> queryF) {
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        //添加正常账单条件
        queryWrapper.eq("b.approved_state", BillApproveStateEnum.待审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        normalBillCondition(queryWrapper);
        IPage<PayBillDto> payBillPage = payBillRepository.queryPage(Page.of(queryF.getPageNum(),queryF.getPageSize()), queryWrapper);
        return PageV.of(payBillPage.getCurrent(), payBillPage.getSize(), payBillPage.getTotal(), Global.mapperFacade.mapAsList(payBillPage.getRecords(), PayBillV.class));
    }

    /**
     * 初始化付款单
     *
     * @param payBill 付款单
     */
    private void initPayBill(PayBill payBill) {
        payBill.generateIdentifier();
        payBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        if (Objects.isNull(payBill.getPayTime())) {
            payBill.setPayTime(LocalDateTime.now());
        }
    }

    /**
     * 查询付款单信息
     *
     * @param payBillId 付款单id
     * @return PayBillDetailV
     */
    public PayBillDto queryById(Long payBillId) {
        PayBillDto payBillDto = payBillRepository.queryById(payBillId);
        if(Objects.nonNull(payBillDto)){
            List<PayDetail> payDetails = payDetailRepository.queryByPayBillId(payBillId);
            payBillDto.setPayDetails(payDetails);
        }
        return payBillDto;
    }

    /**
     * 根据id集合获取付款单
     * @param payBillIdList 付款单id集合
     * @return List
     */
    public List<PayBillV> queryByIdList(List<Long> payBillIdList) {
        List<PayBillDto> payBillDtoList = payBillRepository.queryByIdList(payBillIdList);
        if(CollectionUtils.isNotEmpty(payBillDtoList)){
            List<PayDetail> payDetailList = payDetailRepository.queryByPayBillIdList(payBillIdList);
            Map<Long, List<PayDetail>> collect = payDetailList.stream().collect(Collectors.groupingBy(PayDetail::getPayBillId));
            for (PayBillDto payBillDto : payBillDtoList) {
                payBillDto.setPayDetails(collect.get(payBillDto.getId()));
            }
        }
        return Global.mapperFacade.mapAsList(payBillDtoList, PayBillV.class);
    }

    /**
     * 查询付款单详情(包含明细)
     *
     * @param payBillId 付款单id
     * @return PayBillDetailV
     */
    public PayBillDetailV queryDetailById(Long payBillId) {
        PayBillDto payBillDto = queryById(payBillId);
        PayBillDetailV payBillDetailV = Global.mapperFacade.map(payBillDto, PayBillDetailV.class);
        if(Objects.nonNull(payBillDto)){
            payBillDetailV.setCarryovers(Global.mapperFacade.mapAsList(carryoverRepository.listByCarriedBillId(payBillId), BillCarryoverV.class));
        }
        return payBillDetailV;
    }

    /**
     * 删除付款单
     *
     * @param payBillId 付款单id
     * @return Boolean
     */
    public Boolean delete(Long payBillId) {
        return payBillRepository.removeById(payBillId);
    }

    /**
     * 统计付款单信息
     *
     * @param queryF 统计条件
     * @return BillTotalDto
     */
    public BillTotalDto queryTotal(StatisticsBillTotalQuery queryF) {
        QueryWrapper<?> wrapper;
        //添加逻辑删除
        if (CollectionUtils.isNotEmpty(queryF.getBillIds())){
            wrapper = new QueryWrapper<>().in("b.id", queryF.getBillIds());
        }else {
            PageF<SearchF<?>> query = queryF.getQuery();
            query.getConditions().getFields().removeIf(a-> "b.community_id".equals(a.getName()));
            query.getConditions().getFields().removeIf(a-> "b.room_id".equals(a.getName()));
            wrapper = query.getConditions().getQueryModel();
        }
        //无效账单条件
        Integer billInvalid = queryF.getBillInvalid();
        if(Objects.nonNull(billInvalid) && billInvalid == 1){
            invalidBillCondition(wrapper);
        }else{
            normalBillCondition(wrapper);
        }
        wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        wrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode());
        return payBillRepository.queryTotal(wrapper);
    }

    /**
     * 导出收款单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        //获取导出账单
        QueryWrapper<?> queryWrapper = queryF.getConditions().getQueryModel();
        queryWrapper.ne("b.approved_state", BillApproveStateEnum.待审核.getCode());
        queryWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        normalBillCondition(queryWrapper);
        String fileName = "付款单";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportPayBillData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportPayBillData> resultList;
            while(pageNumber <= totalPage){
                IPage<PayBillDto> payBillDtoPage = payBillRepository.queryPage(Page.of(pageNumber, pageSize), queryWrapper);
                resultList = Global.mapperFacade.mapAsList(payBillDtoPage.getRecords(), ExportPayBillData.class);
                if(CollectionUtils.isEmpty(resultList)){
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet,writeTable);
                totalPage = payBillDtoPage.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                .ne("b.reversed", BillReverseStateEnum.已冲销.getCode())
                .ne("b.carried_state", BillCarriedStateEnum.已结转.getCode())
                .ne("b.refund_state", BillRefundStateEnum.已退款.getCode());
    }

    /**
     * 获取账单推凭信息
     * @param page
     * @param eventType
     * @return
     */
    public Page<BillInferenceV> pageBillInferenceInfo(PageF<SearchF<BillInferenceV>> page, Integer eventType) {
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
        queryModel.eq("b.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("bd.deleted", DataDeletedEnum.NORMAL.getCode());
        if (EventTypeEnum.收款结算.getEvent() == eventType) {
            queryModel.eq("bd.inference_state", 0);
        } else if (EventTypeEnum.冲销作废.getEvent() == eventType){
            queryModel.eq("bd.inference_state", 1);
            return payBillRepository.pageBillInferenceOffInfo(page, queryModel);
        }
        return payBillRepository.pageBillInferenceInfo(page, queryModel);
    }


    /**
     * 修改详情的推凭状态
     * @param concatIds
     * @param state
     */
    public void batchUpdateDetailInferenceSate(List<Long> concatIds, int state) {
        payDetailRepository.batchUpdateDetailInferenceSate(concatIds, state);
    }

    /**
     * 获取账单推凭信息
     * @param billInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfo(BillInferenceF billInferenceF) {
        if (BillActionEventEnum.结算.getCode() == billInferenceF.getActionEventCode()) {
            return payDetailRepository.listInferenceInfoByIdAndInfer(billInferenceF.getBillId(), 0, billInferenceF.getSupCpUnitId());
        }
        return payDetailRepository.listInferenceInfoByIdAndInfer(billInferenceF.getBillId(), 1, billInferenceF.getSupCpUnitId());
    }

    /**
     * 获取账单推凭信息
     * @param batchBillInferenceF
     * @return
     */
    public List<BillInferenceV> listInferenceInfoByIds(BatchBillInferenceF batchBillInferenceF) {
        if (BillActionEventEnum.结算.getCode() == batchBillInferenceF.getActionEventCode()) {
            return payDetailRepository.listInferenceInfoByIdsAndInfer(batchBillInferenceF.getBillIds(), 0, batchBillInferenceF.getSupCpUnitId());
        }
        return payDetailRepository.listInferenceInfoByIdsAndInfer(batchBillInferenceF.getBillIds(), 1, batchBillInferenceF.getSupCpUnitId());
    }

    /**
     * 根据账单id获取付款单id
     *
     * @param payableBillId
     */
    public List<Long> getByPayableBillId(Long payableBillId) {
        LambdaQueryWrapper<PayDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PayDetail::getPayBillId, payableBillId);
        List<PayDetail> payDetailList = payDetailRepository.list(wrapper);
        if (CollectionUtils.isNotEmpty(payDetailList)) {
            List<Long> payBillIds = payDetailList.stream().map(PayDetail::getPayBillId).collect(Collectors.toList());
            payBillIds = payBillIds.stream().distinct().collect(Collectors.toList());
            return payBillIds;
        }
        return null;
    }

    /**
     * 入账
     * @param enterCommands
     * @return
     */
    public List<BillUnitaryEnterResultDto> enterBatch(List<BillUnitaryEnterCommand> enterCommands, String supCpUnitId) {
        return new ArrayList<>();
    }
}
