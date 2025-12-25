package com.wishare.finance.apps.service.bill;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.model.voucher.vo.VoucherV;
import com.wishare.finance.apps.service.configure.chargeitem.ChargeItemAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BatchSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillDeductionE;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.bill.repository.BillApproveRepository;
import com.wishare.finance.domains.bill.service.AllBillDomainService;
import com.wishare.finance.domains.bill.service.BillDeductionDomainService;
import com.wishare.finance.domains.bill.service.BillDomainServiceOld;
import com.wishare.finance.domains.bill.service.PayBillDomainService;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.BillOjv;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceReceiptDomainService;
import com.wishare.finance.domains.invoicereceipt.service.ReceiptDomainService;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceDetailDto;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.service.FlowDetailDomainService;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.easyexcel.BillData;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 所有账单应用层
 *
 * @author yancao
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AllBillAppService implements ApiBase {

    private final AllBillDomainService allBillDomainService;

    private final ReceiptDomainService receiptDomainService;

    private final InvoiceDomainService invoiceDomainService;

    private final BillDomainServiceOld billDomainServiceOld;

    private final BillFacade billFacade;

    private final VoucherDomainService voucherDomainService;

    private final FlowDetailDomainService flowDetailDomainService;

    private final PayBillDomainService payBillDomainService;

    private final InvoiceReceiptDomainService invoiceReceiptDomainService;

    private final BillDeductionDomainService billDeductionDomainService;

    private final BillApproveRepository approveRepository;

    private final ChargeItemAppService chargeItemAppService;

    /**
     * 分组查询账单
     *
     * @param query 分页参数
     * @return PageV
     */
    public PageV<AllBillGroupDto> groupByRule(AllBillGroupQueryF query) {
        return allBillDomainService.groupByRule(query);
    }

    /**
     * 分组查询账单
     *
     * @param query 分页参数
     * @return PageV
     */
    public PageV<AllBillGroupDto> queryBillByGroup(AllBillGroupQueryF query) {
        return allBillDomainService.queryBillByGroup(query);
    }

    /**
     * 分页查询账单
     *
     * @param queryF 分页信息
     * @return PageV
     */
    public PageV<AllBillPageDto> queryBillByPage(AllBillGroupQueryF queryF) {
        return allBillDomainService.queryBillByPage(queryF);
    }

    /**
     * 根据id集合获取账单信息
     *
     * @param billQueryList 查询信息
     * @return List
     */
    public List<AllBillPageDto> queryBillByIdList(List<AllBillQueryF> billQueryList) {
        return allBillDomainService.queryBillByIdList(billQueryList);
    }

    /**
     * 查询账单数量
     *
     * @param queryF 查询参数
     * @return long
     */
    public Long queryBillCount(AllBillGroupQueryF queryF) {
        return allBillDomainService.queryBillCount(queryF);
    }

    /**
     * 根据外部审批标识查询账单
     *
     * @param outApprovedIdList 外部标识集合
     * @return List
     */
    public List<AllBillPageDto> queryBillByOutApprovedId(List<String> outApprovedIdList, String supCpUnitId) {
        return allBillDomainService.queryBillByOutApprovedId(outApprovedIdList, supCpUnitId);
    }

    /**
     * 查询账单金额
     *
     * @param form
     * @return
     */
    public BillAmountDto queryBillAmount(BillAmountQueryF form) {
        return allBillDomainService.queryBillAmount(form);
    }


    /**
     * 分页查询无效收费账单
     *
     * @param queryF 查询参数
     * @return PageV
     */
    public PageV<AllBillPageVo> invalidChargePage(PageF<SearchF<?>> queryF) {
        return allBillDomainService.invalidChargePage(queryF);
    }

    /**
     * 分页查询无效付费账单
     *
     * @param queryF 查询参数
     * @return PageV
     */
    public PageV<AllBillPageVo> invalidPayPage(PageF<SearchF<?>> queryF) {
        return allBillDomainService.invalidPayPage(queryF);
    }

    /**
     * 统计无效收费账单
     *
     * @param queryF queryF
     * @return BillTotalDto
     */
    public BillTotalDto invalidChargeStatistics(PageF<SearchF<?>> queryF) {
        return allBillDomainService.invalidChargeStatistics(queryF);
    }

    /**
     * 统计无效付费账单
     *
     * @param queryF queryF
     * @return BillTotalDto
     */
    public BillTotalDto invalidPayStatistics(PageF<SearchF<?>> queryF) {
        return allBillDomainService.invalidPayStatistics(queryF);
    }

    /**
     * 分页查询合同流水账单
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<AllBillPageDto> flowContractPage(PageF<SearchF<?>> queryF) {
        return allBillDomainService.flowContractPage(queryF);
    }

    /**
     * 分页查询合同流水账单-新模式
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<AllBillPageDto> flowContractPageNew(PageF<SearchF<?>> queryF) {
        return allBillDomainService.flowContractPageNew(queryF);
    }


    public PageV<FlowBillPageDto> flowPageNewZJ(PageF<SearchF<?>> queryF) {
        return allBillDomainService.flowPageNewZJ(queryF);
    }


    /**
     * 费项分组分页查询账单列表(用于业务信息)
     *
     * @param queryF
     * @return
     */
    public PageV<ChargeBillGroupDto> queryChargeBillByGroup(PageF<SearchF<?>> queryF) {
        Page<ChargeBillGroupDto> billDtoPage = allBillDomainService.queryChargeBillByGroup(queryF);
        return PageV.of(billDtoPage.getCurrent(), billDtoPage.getSize(), billDtoPage.getTotal(), billDtoPage.getRecords());
    }

    /**
     * 统计费项分组分页查询账单列表(用于业务信息)
     *
     * @param queryF
     * @return
     */
    public ChargeBillGroupStatisticsDto statisticsChargeBillByGroup(SearchF<?> queryF) {
        return allBillDomainService.statisticsChargeBillByGroup(queryF);
    }

    /**
     * 根据id集合查询合同流水账单(用于流水认领)
     *
     * @param idList 账单id集合
     * @return PageV
     */
    public List<AllBillPageDto> flowContractIdList(List<Long> idList, String supCpUnitId) {
        return allBillDomainService.flowContractIdList(idList, supCpUnitId);
    }


    public List<FlowBillPageDto>  flowPageZJ(List<Long> idList, String supCpUnitId) {
        return allBillDomainService.flowPageZJ(idList, supCpUnitId);
    }
    /**
     * 统计合同流水账单金额(用于流水认领)
     *
     * @param queryF 查询参数
     * @return FlowContractBillStatisticsDto
     */
    public FlowContractBillStatisticsDto flowContractAmount(PageF<SearchF<?>> queryF) {
        return allBillDomainService.flowContractAmount(queryF);
    }

    /**
     * 根据账单id获取账单详情
     *
     * @param form form
     * @return BillDetailMoreV
     */
    public BillDetailMoreV detailBill(BillDetailF form) {
        BillDetailMoreV allDetail = billDomainServiceOld.getAllDetail(form.getBillId(), form.getBillType(), form.getSupCpUnitId());
        if (Objects.isNull(allDetail)){
            return new BillDetailMoreV();
        }
        //如果是临港环境，需要把费项全路径返回
        Long chargeItemId = allDetail.getChargeItemId();
        if (EnvConst.LINGANG.equals(EnvData.config) && Objects.nonNull(chargeItemId)){
            String itemName = chargeItemAppService.getNameById(chargeItemId);
            if(StringUtils.isNotBlank(itemName)){
                allDetail.setChargeItemName(itemName);
            }
        }
        allDetail.setReceiptVos(handleReceipt(allDetail.getBillId()));
        allDetail.setInvoiceVos(handleInvoice(allDetail.getBillId(), allDetail.getStatutoryBodyName()));
        allDetail.setVoucherVS(handleVoucher(allDetail.getBillId(), BillTypeEnum.valueOfByCode(form.getBillType())));
        allDetail.setBillDeductionDtos(handleDeduction(allDetail.getBillId()));
        if (allDetail.getSysSource() == SysSourceEnum.合同系统.getCode()) {
            //处理收款单的流水明细
            List<BillSettleV> billSettleDtos = allDetail.getBillSettleDtos();
            if (CollectionUtils.isNotEmpty(billSettleDtos)) {
                List<Long> gatherBillIds = billSettleDtos.stream().map(BillSettleV::getId).collect(Collectors.toList());
                List<FlowInvoiceDetailDto> flowInvoiceDetailDtoList = flowDetailDomainService.getListByInvoiceIds(gatherBillIds, FlowClaimDetailStatusEnum.正常);
                /** 收款流水 */
                allDetail.setGatherBillFlows(flowInvoiceDetailDtoList);
            }
            //处理退款记录的流水明细
            List<BillRefundV> billRefundDtos = allDetail.getBillRefundDtos();
            if (CollectionUtils.isNotEmpty(billRefundDtos)) {
                //通过账单id找到对应的付款单明细
                List<Long> payableBillIds = payBillDomainService.getByPayableBillId(allDetail.getId());
                if (CollectionUtils.isNotEmpty(payableBillIds)) {
                    List<FlowInvoiceDetailDto> flowInvoiceDetailDtoList = flowDetailDomainService.getListByInvoiceIds(Lists.newArrayList(payableBillIds), FlowClaimDetailStatusEnum.正常);
                    /** 退款流水 */
                    allDetail.setPayBillFlows(flowInvoiceDetailDtoList);
                }
            }
        }
        return allDetail;
    }

    /**
     * 获取凭证明细
     *
     * @param billId
     * @param billTypeEnum
     * @return
     */
    private List<VoucherV> handleVoucher(Long billId, BillTypeEnum billTypeEnum) {
        return Global.mapperFacade.mapAsList(voucherDomainService.listByBillId(billId, billTypeEnum), VoucherV.class);
    }

    //获取扣款明细
    private List<BillDeductionV> handleDeduction(Long billId){
        List<BillDeductionE> list = billDeductionDomainService.getList(billId);
        if (CollectionUtils.isNotEmpty(list)) {
            List<BillDeductionV> billDeductionVS = Global.mapperFacade.mapAsList(list, BillDeductionV.class);
            return billDeductionVS;
        }
        return null;
    }

    /**
     * 根据账单id和类型获取账单信息
     *
     * @param form
     * @return
     */
    public List<BillOjv> batchBillInfo(List<BillSearchF> form) {
        if(CollectionUtils.isNotEmpty(form)) {
            Map<Integer, List<BillSearchF>> billSearchMap = form.stream().collect(Collectors.groupingBy(BillSearchF::getBillType));
            Iterator<Map.Entry<Integer, List<BillSearchF>>> entries = billSearchMap.entrySet().iterator();
            List<BillOjv> billOjvList = Lists.newArrayList();
            while (entries.hasNext()) {
                Map.Entry<Integer, List<BillSearchF>> entry = entries.next();
                Integer key = entry.getKey();
                List<BillSearchF> billSearchFList = entry.getValue();
                List<Long> billIds = billSearchFList.stream().map(BillSearchF::getBillId).collect(Collectors.toList());
                List<BillOjv> billOjvs = billFacade.getBillInfo(billIds, key,form.get(0).getSupCpUnitId());
                billOjvList.addAll(billOjvs);
            }
            return billOjvList;
        }
        return new ArrayList<>();
    }

    public List<BillData> batchBillInfoExport(List<BillSearchF> form) {
        if (CollUtil.isEmpty(form)){
            return new ArrayList<>();
        }

        Iterator<Map.Entry<Integer, List<BillSearchF>>> entries = form.stream().collect(Collectors.groupingBy(BillSearchF::getBillType))
                .entrySet().iterator();
        List<BillData> billOjvList = Lists.newArrayList();
        while (entries.hasNext()) {
            Map.Entry<Integer, List<BillSearchF>> entry = entries.next();
            List<BillData> billOjvs = billFacade.getBillInfoExport(entry.getValue().stream().map(BillSearchF::getBillId).collect(Collectors.toList())
                    , entry.getKey(),form.get(0).getSupCpUnitId());

            billOjvList.addAll(billOjvs);
        }
        return billOjvList;


    }

    /**
     * 根据账单id查询发票明细
     *
     * @param billId
     * @param statutoryBodyName
     * @return
     */
    private List<InvoiceV> handleInvoice(Long billId, String statutoryBodyName) {
        List<InvoiceReceiptE> invoiceReceiptES = invoiceDomainService.getByBillId(billId);
        if (CollectionUtils.isNotEmpty(invoiceReceiptES)) {
            List<InvoiceV> invoiceVS = Global.mapperFacade.mapAsList(invoiceReceiptES, InvoiceV.class);
            List<Long> invoiceReceiptIds = invoiceVS.stream().map(InvoiceV::getId).collect(Collectors.toList());

            List<InvoiceE> invoiceEList = invoiceDomainService.getInvoiceByInvoiceReceiptId(invoiceReceiptIds);
            Map<Long, List<InvoiceE>> invoiceReceiptMap = invoiceEList.stream().collect(Collectors.groupingBy(InvoiceE::getInvoiceReceiptId));

            List<InvoiceReceiptDetailE> invoiceReceiptDetailEList = invoiceDomainService.queryDetailByIds(invoiceReceiptIds);
            Map<Long, List<InvoiceReceiptDetailE>> invoiceReceiptDetailMap = invoiceReceiptDetailEList.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getInvoiceReceiptId));

            invoiceVS.forEach(invoiceV -> {
                InvoiceE invoiceE = invoiceReceiptMap.get(invoiceV.getId()).get(0);
                InvoiceReceiptDetailE invoiceReceiptDetailE = invoiceReceiptDetailMap.get(invoiceV.getId()).get(0);

                invoiceV.setBuyerName(invoiceE.getBuyerName());
                invoiceV.setSalerName(statutoryBodyName);
                invoiceV.setTaxRate(invoiceReceiptDetailE.getTaxRate());
            });
            return invoiceVS;
        }
        return null;
    }

    /**
     * 获取账单简约信息
     *
     * @param form
     * @return
     */
    public BillSimpleInfoV getBillSimpleInfoV(CanInvoiceInfoF form) {
        return billDomainServiceOld.getBillSimpleInfoV(form.getBillIds(), form.getBillType()
                , form.getSupCpUnitId(), form.getInvoiceType());
    }

    /**
     * 审核账单
     *
     * @param approveReceivableBillF
     * @return
     */
    public Boolean approve(ApproveReceivableBillF approveReceivableBillF) {
        return billFacade.approve(approveReceivableBillF);
    }

    /**
     * 批量审核更改账单操作
     *
     * @param changeBatchApproveReceivableBillF
     * @return
     */
    public Boolean batchApprove(ChangeBatchApproveReceivableBillF changeBatchApproveReceivableBillF) {
        List<Long> billIds = changeBatchApproveReceivableBillF.getBillIds();
        if (!CollectionUtils.isEmpty(billIds)) {
            for (Long item : billIds) {
                ApproveReceivableBillF map =
                        Global.mapperFacade.map(changeBatchApproveReceivableBillF, ApproveReceivableBillF.class);
                map.setBillId(item);
                billFacade.approve(map);
            }
        }
        return true;
    }

    /**
     * 审核账单
     *
     * @param outApproveBillF 外部审核结果
     * @return 执行结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean outApprove(OutApproveBillF outApproveBillF) {
        List<BillApproveE> billApproveES =
                allBillDomainService.outApprove(
                        outApproveBillF.getOutApproveId(),
                        outApproveBillF.getSupCpUnitId()
                );
        ErrorAssertUtil.isTrueThrow402(CollectionUtils.isNotEmpty(billApproveES),
                ErrorMessage.BILL_APPROVE_RECORD_NOT_EXIST);
        // todo 多线程处理
        for (BillApproveE item : billApproveES) {
            ApproveReceivableBillF approveReceivableBillF =
                    Global.mapperFacade.map(outApproveBillF, ApproveReceivableBillF.class);
            approveReceivableBillF.setBillId(item.getBillId());
            approveReceivableBillF.setBillType(item.getBillType());
            approveReceivableBillF.setOperateType(item.getOperateType());
            approveReceivableBillF.setApproveType(Const.State._1);
            billFacade.approve(approveReceivableBillF);
        }
        return true;
    }

    /**
     * 审核批量减免账单
     *
     * @param outApproveDeductionBillF 外部审核结果
     * @return 执行结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean outApproveByDeduction(OutApproveDeductionBillF outApproveDeductionBillF) {

        List<BillApproveE> billApproveES = approveRepository.list(
                new QueryWrapper<BillApproveE>().eq("sup_cp_unit_id", outApproveDeductionBillF.getSupCpUnitId()).
                        in("id", outApproveDeductionBillF.getApproveIds())
                );
        ErrorAssertUtil.isTrueThrow402(CollectionUtils.isNotEmpty(billApproveES),
                ErrorMessage.BILL_APPROVE_RECORD_NOT_EXIST);
        for (BillApproveE item : billApproveES) {
            ApproveReceivableBillF approveReceivableBillF =
                    Global.mapperFacade.map(outApproveDeductionBillF, ApproveReceivableBillF.class);
            approveReceivableBillF.setBillId(item.getBillId());
            approveReceivableBillF.setBillType(item.getBillType());
            approveReceivableBillF.setOperateType(item.getOperateType());
            approveReceivableBillF.setApproveType(Const.State._1);
            approveReceivableBillF.setSupCpUnitId(outApproveDeductionBillF.getSupCpUnitId());
            billFacade.approve(approveReceivableBillF);
        }
        return true;
    }

    /**
     * 反交账
     *
     * @param billId
     * @param billType
     * @return
     */
    @Transactional
    public boolean handReversal(Long billId, Integer billType, String supCpUnitId) {
        return billDomainServiceOld.handReversal(billId, BillTypeEnum.valueOfByCode(billType), supCpUnitId);
    }

    /**
     * 账单冲销
     *
     * @param form
     * @return
     */
    public Boolean reverse(ReverseF form) {
        //1.获取账单信息
        BillDetailMoreV billDetailMoreV = billFacade.getAllDetail(form.getBillId(), BillTypeEnum.valueOfByCode(form.getBillType()), form.getSupCpUnitId());
        if (billDetailMoreV != null) {
            return billFacade.reverse(billDetailMoreV.getBillId(), BillTypeEnum.valueOfByCode(form.getBillType()), form.getExtField1(), form.getSupCpUnitId());
        }
        throw BizException.throw400("未查询到相关账单");
    }

    /**
     * 分页查询账单
     *
     * @param queryF queryF
     * @return PageV
     */
    public PageV<BillPageInfoV> getPage(PageF<SearchF<?>> queryF) {
        return billDomainServiceOld.getPage(queryF);
    }


    /**
     * 批量结算
     *
     * @param form
     * @return
     */
    public Boolean batchSettle(BatchSettleF form) {
        BatchSettleCommand command = Global.mapperFacade.map(form, BatchSettleCommand.class);
        return billDomainServiceOld.batchSettle(command, getIdentityInfo().get());
    }

    /**
     * 发起审核申请
     *
     * @param billApplyF 申请入参
     * @return Boolean
     */
    public Boolean apply(BillApplyF billApplyF) {
        return billDomainServiceOld.apply(billApplyF, billApplyF.getBillType());
    }

    /**
     * 账单批量申请
     *
     * @param billApplyBatchF 批量申请入参
     * @return Boolean
     */
    public Boolean applyBatch(BillApplyBatchF billApplyBatchF) {
        return billDomainServiceOld.applyBatch(billApplyBatchF, billApplyBatchF.getBillType());
    }

    /**
     * 根据账单id查询收据明细
     *
     * @param billId billId
     * @return List
     */
    private List<ReceiptV> handleReceipt(Long billId) {
        List<InvoiceReceiptE> invoiceReceiptList = receiptDomainService.getByBillId(billId);
        if (CollectionUtils.isNotEmpty(invoiceReceiptList)) {
            List<ReceiptV> receiptVS = Global.mapperFacade.mapAsList(invoiceReceiptList, ReceiptV.class);

            return receiptVS;
        }
        return null;
    }

    /**
     * 获取账单的开票金额
     *
     * @param form
     * @return
     */
    public Long invoiceReceiptAmount(InvoiceReceiptAmountF form) {
        Long invoiceReceiptAmount = invoiceReceiptDomainService.invoiceReceiptAmount(form.getBillIds());
        return invoiceReceiptAmount;
    }

    /**
     * 账单减免批量申请
     *
     * @param applyBatchDeductionF 批量减免申请入参
     * @return Boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public ApplyBatchDeductionV applyBatchDeduction(ApplyBatchDeductionF applyBatchDeductionF) {
        List<Long> approveIds = new ArrayList<>();
        List<Long> billAdjustIds = new ArrayList<>();
        ApplyBatchDeductionV applyBatchDeductionV = new ApplyBatchDeductionV();
        for (Map.Entry<Integer, List<Long>> entry : applyBatchDeductionF.getBillIds().entrySet()) {
            if (CollectionUtils.isNotEmpty(entry.getValue())) {
                BillApplyBatchF billApplyBatchF = new BillApplyBatchF();
                billApplyBatchF.setBillIds(entry.getValue());
                billApplyBatchF.setBillType(entry.getKey());
                if (BillTypeEnum.应收账单.getCode() == entry.getKey()) {
                    billApplyBatchF.setDetail(applyBatchDeductionF.getReceivableDetail());
                } else if (BillTypeEnum.临时收费账单.getCode() == entry.getKey()) {
                    billApplyBatchF.setDetail(applyBatchDeductionF.getTemporaryDetail());
                }
                billApplyBatchF.setOutApproveId(applyBatchDeductionF.getOutApproveId());
                billApplyBatchF.setReason(applyBatchDeductionF.getReason());
                billApplyBatchF.setApproveOperateType(applyBatchDeductionF.getApproveOperateType());
                billApplyBatchF.setSupCpUnitId(applyBatchDeductionF.getSupCpUnitId());
                billApplyBatchF.setOperationId(applyBatchDeductionF.getOperationId());
                ApplyBatchDeductionV applyBatchDeduction = billDomainServiceOld.applyBatchDeduction(billApplyBatchF, billApplyBatchF.getBillType());
                approveIds.addAll(applyBatchDeduction.getApproveIds());
                billAdjustIds.addAll(applyBatchDeduction.getBillAdjustIds());
            }
        }
        applyBatchDeductionV.setBillAdjustIds(billAdjustIds);
        applyBatchDeductionV.setApproveIds(approveIds);
        return applyBatchDeductionV;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean outApproveByOperationId(OutApproveOperationF outApproveOperationF) {
        List<BillApproveE> billApproveES = approveRepository.selectBillApproveEList(outApproveOperationF.getOperationId(),outApproveOperationF.getSupCpUnitId());
        ErrorAssertUtil.isTrueThrow402(CollectionUtils.isNotEmpty(billApproveES),
                ErrorMessage.BILL_APPROVE_RECORD_NOT_EXIST);
        for (BillApproveE item : billApproveES) {
            ApproveReceivableBillF approveReceivableBillF =
                    Global.mapperFacade.map(outApproveOperationF, ApproveReceivableBillF.class);
            approveReceivableBillF.setBillId(item.getBillId());
            approveReceivableBillF.setBillType(item.getBillType());
            approveReceivableBillF.setOperateType(item.getOperateType());
            approveReceivableBillF.setApproveType(Const.State._1);
            approveReceivableBillF.setSupCpUnitId(outApproveOperationF.getSupCpUnitId());
            billFacade.approve(approveReceivableBillF);
        }
        return true;
    }


    // 用于支付流水认领接口查询未商户清分、未账票流水对账
    public PageV<AllBillPageDto> channelFlowClaimPage(PageF<SearchF<?>> queryF) {
        return allBillDomainService.channelFlowClaimPage(queryF);
    }


    public List<AllBillGroupDto> getChargePayBillGroupDetail(AllBillGroupQueryF query) {
        return allBillDomainService.getChargePayBillGroupDetail(query);
    }
}
