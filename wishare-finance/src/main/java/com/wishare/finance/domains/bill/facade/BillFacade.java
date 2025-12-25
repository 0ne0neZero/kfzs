package com.wishare.finance.domains.bill.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wishare.finance.apis.common.FinanceCommonUtils;
import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.fo.AdvanceBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.ApproveBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveGatherBillF;
import com.wishare.finance.apps.model.bill.fo.ApproveReceivableBillF;
import com.wishare.finance.apps.model.bill.fo.BatchAddBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.BatchDelBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.BillApplyBatchF;
import com.wishare.finance.apps.model.bill.fo.BillApplyF;
import com.wishare.finance.apps.model.bill.fo.BillInferenceF;
import com.wishare.finance.apps.model.bill.fo.FinishInvoiceF;
import com.wishare.finance.apps.model.bill.fo.InvoiceVoidBatchF;
import com.wishare.finance.apps.model.bill.fo.ListBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.PayableBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.ReceivableBillInvalidF;
import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.fo.TemporaryChargeBillInvalidF;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.AdvanceBillAppService;
import com.wishare.finance.apps.service.bill.BillInferenceAppService;
import com.wishare.finance.apps.service.bill.BillRefundAppService;
import com.wishare.finance.apps.service.bill.BillSettleAppService;
import com.wishare.finance.apps.service.bill.GatherBillAppService;
import com.wishare.finance.apps.service.bill.PayBillAppService;
import com.wishare.finance.apps.service.bill.PayableBillAppService;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.apps.service.bill.TemporaryChargeBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillInvalidBatchCommand;
import com.wishare.finance.domains.bill.command.BillInvalidCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.mapper.*;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.BillOjv;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.easyexcel.BillData;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账单防腐层
 *
 * @Author dxclay
 * @Date 2022/10/14
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillFacade {

    private final BillSettleAppService billSettleAppService;

    private final BillRefundAppService billRefundAppService;

    private final AdvanceBillAppService advanceBillAppService;
    private final AdvanceBillMapper advanceBillMapper;

    private final ReceivableBillAppService receivableBillAppService;

    private final TemporaryChargeBillAppService temporaryChargeBillAppService;

    private final PayableBillAppService payableBillAppService;
    private final PayableBillMapper payableBillMapper;
    private final PayBillMapper payBillMapper;
    private final PayDetailMapper payDetailMapper;

    private final PayBillAppService payBillAppService;

    private final GatherBillAppService gatherBillAppService;
    private final GatherBillMapper gatherBillMapper;
    private final GatherDetailMapper gatherDetailMapper;
    private final ReceivableBillMapper receivableBillMapper;

    private final BillInferenceAppService billInferenceAppService;


    /**
     * @param billId
     * @param billType
     * @return
     */
    public boolean handReversal(Long billId, BillTypeEnum billType, String supCpUnitId) {
        switch (billType) {
            case 预收账单:
                return advanceBillAppService.handReversal(billId,null);
            case 应收账单:
                return receivableBillAppService.handReversal(billId,supCpUnitId);
            case 临时收费账单:
                return temporaryChargeBillAppService.handReversal(billId,null);
        }
        return false;
    }


//    /**
//     * 根据账单ids 修改账单信息为开票中 [invoiceState = 1]
//     * advance_bill || receivable_bill
//     * @param billIds  账单id
//     * @param billType 账单类型
//     * @param billIdsMap key-billId,value-开票状态
//     */
//    public Boolean invoiceBatch(List<Long> billIds, BillTypeEnum billType,String supCpUnitId,Map<Long,Integer> billIdsMap) {
//        Boolean res = false;
//        switch (billType) {
//            case 预收账单:
//                res = advanceBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMap);
//                break;
//            case 应收账单:
//                res = receivableBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMap);
//                break;
//            case 临时收费账单:
//                res = temporaryChargeBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMap);
//                break;
//            default:
//                break;
//        }
//        return res;
//    }


    /**
     * 根据账单ids 修改账单信息为开票中 [invoiceState = 1]
     * advance_bill || receivable_bill  (gather_bill && gather_detail)
     * @param invoiceReceiptDetailRedEList  账单id
     * @param billIdsMap 存在billIdsMap表明要修改账单状态为原状态
     */
    public Boolean invoiceBatch(List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList,  String supCpUnitId,Map<String, Map<Long, Integer>> billIdsMap) {
        //目标状态 如果为false 默认修改开票中 true 为 修改提供的状态
        boolean targetFlag = org.springframework.util.CollectionUtils.isEmpty(billIdsMap)?false:true;
        Boolean res = false;

        Map<Integer, Set<Long>> result = invoiceReceiptDetailRedEList.stream()
                .collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillType,
                        Collectors.mapping(InvoiceReceiptDetailE::getBillId, Collectors.toSet())));
        //
        Map<Long, Integer> billIdsMapTmp = null;
        for (Map.Entry<Integer, Set<Long>> entry : result.entrySet()) {
            Integer typeKey = entry.getKey();
            Set<Long> billIdset = entry.getValue();
            List<Long> billIds = new ArrayList<>(billIdset);
            if(!org.springframework.util.CollectionUtils.isEmpty(billIdsMap)){
                billIdsMapTmp = billIdsMap.get("billIdsMap");
            }
            //修改[advance_bill || receivable_bill]
            if(BillTypeEnum.预收账单.equalsByCode(typeKey)){
                res = advanceBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMapTmp);
            } else if (BillTypeEnum.应收账单.equalsByCode(typeKey)) {
                res = receivableBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMapTmp);
            }else if (BillTypeEnum.临时收费账单.equalsByCode(typeKey)) {
                res = temporaryChargeBillAppService.invoiceBatch(billIds,supCpUnitId,billIdsMapTmp);
            }
        }
        List<Long> gatherBillIds = invoiceReceiptDetailRedEList.stream().map(InvoiceReceiptDetailE::getGatherBillId).distinct().collect(Collectors.toList());
        if(!ObjectUtils.allNull(gatherBillIds)){
            if(!org.springframework.util.CollectionUtils.isEmpty(billIdsMap)){
                billIdsMapTmp = billIdsMap.get("gatherBillIdsMap");

            }
            //修改收款单表[gather_bill]
            if((!targetFlag)||(targetFlag&&(!org.springframework.util.CollectionUtils.isEmpty(billIdsMapTmp)))){
                log.info("invoiceBatch gather_bill gatherBillIds[{}][{}]", JSONObject.toJSON(gatherBillIds),JSONObject.toJSON(billIdsMapTmp));
                gatherBillAppService.gatherBillInvoiceBatch(gatherBillIds,supCpUnitId,billIdsMapTmp);
            }
            List<Long> gatherDetailIds = invoiceReceiptDetailRedEList.stream().map(InvoiceReceiptDetailE::getGatherDetailId).collect(Collectors.toList());
            if(!org.springframework.util.CollectionUtils.isEmpty(billIdsMap)){
                billIdsMapTmp = billIdsMap.get("gatherDetailBillIdsMap");
            }
            //修改收款单详情表[gather_detail]
            if((!targetFlag)||(targetFlag&&(!org.springframework.util.CollectionUtils.isEmpty(billIdsMapTmp)))){
                log.info("invoiceBatch gather_detail gatherDetailIds[{}]billIdsMapTmp[{}]",JSONObject.toJSON(gatherDetailIds),JSONObject.toJSON(billIdsMapTmp));
                gatherBillAppService.gatherDetailInvoiceBatch(gatherDetailIds,supCpUnitId,billIdsMapTmp);
            }
        }
        return res;
    }



    /**
     * 根据账单ids 修改账单信息为开票中 [invoiceState = 1]
     * advance_bill || receivable_bill  (gather_bill && gather_detail)
     * @param invoiceReceiptDetailRedEList  账单id
     */
    public Boolean invoiceBatch(List<InvoiceReceiptDetailE> invoiceReceiptDetailRedEList,  String supCpUnitId) {
        return this.invoiceBatch(invoiceReceiptDetailRedEList,supCpUnitId,null);
    }


    /**
     * 获取账单信息
     *
     * @param billIds  账单id
     * @param billType 账单类型
     * @return List
     */
    public List<BillOjv> getBillInfo(List<Long> billIds, Integer billType,String supCpUnitId) {
        List<BillOjv> billOjvList = Lists.newArrayList();
        switch (BillTypeEnum.valueOfByCode(billType)) {
            case 应收账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new RuntimeException("查询应收账单时，必须传入项目id");
                }
                List<ReceivableBillMoreInfoDto> receivableBillMoreInfos = receivableBillAppService.receivableBillInfo(billIds,supCpUnitId);
                billOjvList = Global.mapperFacade.mapAsList(receivableBillMoreInfos, BillOjv.class);
                break;
            case 预收账单:
                List<AdvanceBillDetailV> advanceBillDetails = advanceBillAppService.queryByIdList(billIds, AdvanceBillDetailV.class, null);
                billOjvList = Global.mapperFacade.mapAsList(advanceBillDetails, BillOjv.class);
                break;
            case 临时收费账单:
                List<TempChargeBillMoreInfoDto> tempChargeBillMoreInfos = temporaryChargeBillAppService.tempChargeBillInfo(billIds, supCpUnitId);
                billOjvList = Global.mapperFacade.mapAsList(tempChargeBillMoreInfos, BillOjv.class);
                break;
            case 付款单:
                List<PayBillV> payBillVS = payBillAppService.queryByIdList(billIds);
                billOjvList = Global.mapperFacade.mapAsList(payBillVS, BillOjv.class);
                billOjvList.forEach(item -> {
                    item.setSettleState(BillSettleStateEnum.已结算.getCode());
                    item.setType(BillTypeEnum.付款单.getCode());
                    item.setActualPayAmount(item.getTotalAmount());
                });
                break;
            case 应付账单:
                List<PayableBillDetailV> payableBillPageV = payableBillAppService.queryByIdList(billIds,PayableBillDetailV.class, null);
                billOjvList = Global.mapperFacade.mapAsList(payableBillPageV, BillOjv.class);
                billOjvList.forEach(item -> {
                    item.setSettleState(BillSettleStateEnum.已结算.getCode());
                    item.setType(BillTypeEnum.应付账单.getCode());
                    item.setActualPayAmount(item.getTotalAmount());
                });
                break;
            case 收款单:
                List<GatherBillV> gatherBillDetailVS = gatherBillAppService.queryByIdList(billIds, supCpUnitId);
                billOjvList = Global.mapperFacade.mapAsList(gatherBillDetailVS, BillOjv.class);
                billOjvList.forEach(item -> {
                    item.setSettleState(BillSettleStateEnum.已结算.getCode());
                    item.setType(BillTypeEnum.收款单.getCode());
                    item.setActualPayAmount(item.getTotalAmount());
                });
                break;
            default:
                break;
        }
        return billOjvList;
    }


    /**
     * 获取账单信息
     *
     * @param billIds  账单id
     * @param billType 账单类型
     * @return List
     */
    public List<BillData> getBillInfoExport(List<Long> billIds, Integer billType, String supCpUnitId) {
        List<BillData> billOjvList = Lists.newArrayList();
        switch (BillTypeEnum.valueOfByCode(billType)) {
            case 应收账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new RuntimeException("查询应收账单时，必须传入项目id");
                }
                List<BillData> qq = getReceivableBill(billIds, supCpUnitId);
                billOjvList.addAll(qq);
                break;
            case 临时收费账单:
                List<BillData> ff = getReceivableBill(billIds, supCpUnitId);
                billOjvList.addAll(ff);
                break;

            case 预收账单:
                //advance_bill
                List<BillData> data = getAdvanceBill(billIds);
                billOjvList.addAll(data);
                break;
            case 付款单:
                //pay_bill
                List<BillData> aa = getPayBill(billIds,supCpUnitId);
                billOjvList.addAll(aa);
                break;
            case 应付账单:
                //payable_bill
                List<BillData> tm = getPayableBill(billIds);
                billOjvList.addAll(tm);
                break;
            case 收款单:
                //gather_bill
                List<BillData> dd = getGatherBill(billIds, supCpUnitId);
                billOjvList.addAll(dd);
                break;
            default:
                break;
        }
        return billOjvList;
    }

    private List<BillData> getReceivableBill(List<Long> billIds,String supCpUnitId){

        List<ReceivableBill> list = receivableBillMapper.selectList(Wrappers.<ReceivableBill>lambdaQuery()
                .select(ReceivableBill::getBillNo, ReceivableBill::getTotalAmount,
                        ReceivableBill::getId,ReceivableBill::getInvoiceAmount,ReceivableBill::getBillType,
                        ReceivableBill::getReceivableAmount,ReceivableBill::getOverdueAmount,ReceivableBill::getDiscountAmount,ReceivableBill::getRefundAmount,ReceivableBill::getCarriedAmount,ReceivableBill::getReverseAmount,ReceivableBill::getSettleAmount,
                        ReceivableBill::getChargeItemName, ReceivableBill::getState,ReceivableBill::getSettleState)
                .in(ReceivableBill::getId, billIds)
                .eq(ReceivableBill::getSupCpUnitId,supCpUnitId)
        );
        ArrayList<BillData> tm = new ArrayList<>(list.size());
        list.forEach(in->{
            BillData build = BillData.builder()
                    .billType(in.getBillType()).billId(in.getId()).communityId(supCpUnitId)
                    .billNo(in.getBillNo())
                    .billTypeStr(BillTypeEnum.valueOfByCodeNoExe(in.getBillType()))
                    .chargeItemName(in.getChargeItemName())
                    .totalAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .actualPayAmountStr(FinanceCommonUtils.F2Y(getRemainingSettleAmount(in)))
                    .invoiceAmountStr(FinanceCommonUtils.F2Y(in.getInvoiceAmount()))
                    .settleStateStr(BillSettleStateEnum.codeToName(in.getSettleState()))
                    .stateStr(BillStateEnum.codeToName(in.getState()))
                    .build();
            tm.add(build);
        });
        return tm;
    }
    /**
     * 获取剩余可支付金额
     * @return  可支付金额 = 应收金额 + 违约金金额 - 实收减免金额 + 退款金额 + 结算金额（上次结算金额）-收款金额
     * 来自{@linkplain Bill#getActualPayAmount()}
     */
    public Long getRemainingSettleAmount(ReceivableBill d){

//        settleAmount - refundAmount - carriedAmount - reverseAmount
        return d.getSettleAmount()- d.getRefundAmount()-d.getCarriedAmount()-d.getReverseAmount();

    }
    public Long getRemainingSettleAmount(AdvanceBill d){

//        settleAmount - refundAmount - carriedAmount - reverseAmount
        return d.getSettleAmount()- d.getRefundAmount()-d.getCarriedAmount()-d.getReverseAmount();

    }

    private List<BillData> getGatherBill(List<Long> billIds,String supCpUnitId){



        List<GatherBill> list = gatherBillMapper.selectList(Wrappers.<GatherBill>lambdaQuery()
                .select(GatherBill::getId,GatherBill::getSupCpUnitId,GatherBill::getBillNo, GatherBill::getTotalAmount,  GatherBill::getInvoiceAmount, GatherBill::getState,
                        GatherBill::getRefundAmount,GatherBill::getCarriedAmount)
                .in(GatherBill::getId, billIds)
                .eq(GatherBill::getSupCpUnitId,supCpUnitId)
        );

        Map<String, String> collect = gatherDetailMapper.selectList(Wrappers.<GatherDetail>lambdaQuery().select(GatherDetail::getGatherBillNo, GatherDetail::getChargeItemName).in(GatherDetail::getGatherBillId, billIds)
                        .eq(GatherDetail::getSupCpUnitId,supCpUnitId)
                )
                .stream().collect(Collectors.toMap(
                node -> Optional.ofNullable(node.getGatherBillNo()).orElse(""),
                node -> Optional.ofNullable(node.getChargeItemName()).orElse(""),
                (oldVal, newVal) -> oldVal)
        );
        ArrayList<BillData> tm = new ArrayList<>(list.size());
        list.forEach(in->{
            BillData build = BillData.builder().billNo(in.getBillNo())
                    .billType(BillTypeEnum.收款单.getCode()).billId(in.getId()).communityId(in.getSupCpUnitId())
                    .billTypeStr(BillTypeEnum.收款单.getValue())
                    .chargeItemName(collect.get(in.getBillNo()))
                    .totalAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .actualPayAmountStr(FinanceCommonUtils.F2Y(in.getActualSettleAmount()))
                    .invoiceAmountStr(FinanceCommonUtils.F2Y(in.getInvoiceAmount()))
                    .settleStateStr(BillSettleStateEnum.已结算.getValue())
                    .stateStr(BillStateEnum.codeToName(in.getState()))
                    .build();
            tm.add(build);
        });
        return tm;
    }
    private List<BillData> getAdvanceBill(List<Long> billIds){

        List<AdvanceBill> list = advanceBillMapper.selectList(Wrappers.<AdvanceBill>lambdaQuery()
                .select(AdvanceBill::getBillNo,AdvanceBill::getId, AdvanceBill::getTotalAmount, AdvanceBill::getInvoiceAmount, AdvanceBill::getChargeItemName, AdvanceBill::getState,AdvanceBill::getSettleState,
                        AdvanceBill::getSettleAmount,AdvanceBill::getRefundAmount,AdvanceBill::getCarriedAmount,AdvanceBill::getReverseAmount,AdvanceBill::getSupCpUnitId
                )
                .in(AdvanceBill::getId, billIds));
        ArrayList<BillData> tm = new ArrayList<>(list.size());
        list.forEach(in->{
            BillData build = BillData.builder().billNo(in.getBillNo())
                    .billType(BillTypeEnum.预收账单.getCode()).billId(in.getId()).communityId(in.getSupCpUnitId())
                    .billTypeStr(BillTypeEnum.预收账单.getValue())
                    .chargeItemName(in.getChargeItemName())
                    .totalAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .actualPayAmountStr(FinanceCommonUtils.F2Y(getRemainingSettleAmount(in)))
                    .invoiceAmountStr(FinanceCommonUtils.F2Y(in.getInvoiceAmount()))
                    .settleStateStr(BillSettleStateEnum.codeToName(in.getSettleState()))
                    .stateStr(BillStateEnum.codeToName(in.getState()))
                    .build();
            tm.add(build);
        });
        return tm;
    }
    private List<BillData> getPayBill(List<Long> billIds,String supCpUnitId){
        List<PayBill> list = payBillMapper.selectList(Wrappers.<PayBill>lambdaQuery()
                .select(PayBill::getBillNo,PayBill::getId, PayBill::getTotalAmount,  PayBill::getInvoiceAmount, PayBill::getState

                )
                .in(PayBill::getId, billIds));
        Map<String, String> collect = payDetailMapper.selectList(Wrappers.<PayDetail>lambdaQuery()
                .select(PayDetail::getPayBillNo, PayDetail::getChargeItemName)
                .in(PayDetail::getPayBillId, billIds).eq(PayDetail::getSupCpUnitId,supCpUnitId)
                )
                .stream().collect(Collectors.toMap(
                node -> Optional.ofNullable(node.getPayBillNo()).orElse(""),
                node -> Optional.ofNullable(node.getChargeItemName()).orElse(""),
                (oldVal, newVal) -> oldVal)
        );

        ArrayList<BillData> tm = new ArrayList<>(list.size());
        list.forEach(in->{
            BillData build = BillData.builder().billNo(in.getBillNo())
                    .billType(BillTypeEnum.付款单.getCode()).billId(in.getId()).communityId(supCpUnitId)
                    .billTypeStr(BillTypeEnum.付款单.getValue())
                    .chargeItemName(collect.get(in.getBillNo()))
                    .totalAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .actualPayAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .invoiceAmountStr(FinanceCommonUtils.F2Y(in.getInvoiceAmount()))
                    .settleStateStr(BillSettleStateEnum.已结算.getValue())
                    .stateStr(BillStateEnum.codeToName(in.getState()))
                    .build();
            tm.add(build);
        });
        return tm;
    }

    private List<BillData> getPayableBill(List<Long> billIds){
        List<PayableBill> list = payableBillMapper.selectList(Wrappers.<PayableBill>lambdaQuery()
                .select(PayableBill::getId,PayableBill::getBillNo, PayableBill::getTotalAmount, PayableBill::getActualPayAmount, PayableBill::getInvoiceAmount,
                        PayableBill::getSettleAmount,PayableBill::getRefundAmount,PayableBill::getCarriedAmount,PayableBill::getReverseAmount,
                        PayableBill::getChargeItemName, PayableBill::getState,PayableBill::getSupCpUnitId)
                .in(PayableBill::getId, billIds));
        ArrayList<BillData> tm = new ArrayList<>(list.size());
        list.forEach(in->{
            BillData build = BillData.builder().billNo(in.getBillNo())
                    .billType(BillTypeEnum.应付账单.getCode()).billId(in.getId()).communityId(in.getSupCpUnitId())
                    .billTypeStr(BillTypeEnum.应付账单.getValue())
                    .chargeItemName(in.getChargeItemName())
                    .totalAmountStr(FinanceCommonUtils.F2Y(in.getTotalAmount()))
                    .actualPayAmountStr(FinanceCommonUtils.F2Y(in.getActualPayAmount()))
                    .invoiceAmountStr(FinanceCommonUtils.F2Y(in.getInvoiceAmount()))
                    .settleStateStr(BillSettleStateEnum.已结算.getValue())
                    .stateStr(BillStateEnum.codeToName(in.getState()))
                    .build();
            tm.add(build);
        });
        return tm;
    }
    /**
     * 根据账单ids获取结算记录
     *
     * @param billIds      账单id
     * @param billTypeEnum 账单类型
     * @return List
     */
    public List<BillSettleV> getBillSettle(List<Long> billIds, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return billSettleAppService.getSettleByIds(billIds, supCpUnitId);
    }

    /**
     * 账单冲销
     *
     * @param billId       账单id
     * @param billTypeEnum 账单类型
     */
    public Boolean reverse(Long billId, BillTypeEnum billTypeEnum, String extField1,String supCpUnitId) {
        Boolean res = false;
        switch (billTypeEnum) {
            case 应收账单:
                res = receivableBillAppService.reverse(billId, extField1,supCpUnitId);
                break;
            case 预收账单:
                res = advanceBillAppService.reverse(billId, extField1);
                break;
            case 临时收费账单:
                res = temporaryChargeBillAppService.reverse(billId, extField1,supCpUnitId);
                break;
            case 应付账单:
                res = payableBillAppService.reverse(billId, extField1);
                break;
        }
        return res;
    }

    /**
     * @param billId
     * @param billType
     * @return
     */
    public boolean updateBill(Long billId, BillTypeEnum billType, String supCpUnitId) {
        switch (billType) {
            case 预收账单:
                return advanceBillAppService.handReversal(billId,null);
            case 应收账单:
                return receivableBillAppService.handReversal(billId,supCpUnitId);
            case 临时收费账单:
                return temporaryChargeBillAppService.handReversal(billId,null);
        }
        return false;
    }

    /**
     * 回滚账单冲销
     *
     * @param billId       账单id
     * @param billTypeEnum 账单类型
     * @return Boolean
     */
    public Boolean robackReverse(Long billId, BillTypeEnum billTypeEnum, String supCpUnitId) {
        Boolean res = false;
        switch (billTypeEnum) {
            case 应收账单:
                res = receivableBillAppService.robackReverse(billId,supCpUnitId);
                break;
            case 预收账单:
                res = advanceBillAppService.robackReverse(billId,supCpUnitId);
                break;
            case 临时收费账单:
                res = temporaryChargeBillAppService.robackReverse(billId,supCpUnitId);
                break;
        }
        return res;
    }

    /**
     * 调用财务中心作废,红冲开票金额
     *
     * @return Boolean
     */
    /*public Boolean invoiceVoidBatch(BillTypeEnum billTypeEnum, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,String supCpUnitId) {
        List<InvoiceVoidBatchF> invoiceVoidBatchRFList = Lists.newArrayList();
        invoiceReceiptDetailES.forEach(invoiceReceiptDetailE -> {
            InvoiceVoidBatchF invoiceVoidBatchRF = new InvoiceVoidBatchF();
            invoiceVoidBatchRF.setBillId(invoiceReceiptDetailE.getBillId());
            invoiceVoidBatchRF.setInvoiceAmount(invoiceReceiptDetailE.getInvoiceAmount());
            invoiceVoidBatchRF.setInvoiceReceiptId(invoiceReceiptDetailE.getInvoiceReceiptId());
            invoiceVoidBatchRF.setSupCpUnitId(supCpUnitId);
            invoiceVoidBatchRFList.add(invoiceVoidBatchRF);
        });
        Boolean res = false;
        switch (billTypeEnum) {
            case 应收账单:
                res = receivableBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList, supCpUnitId);
                break;
            case 临时收费账单:
                res = temporaryChargeBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList, supCpUnitId);
                break;
            case 预收账单:
                res = advanceBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList);
                break;
        }
        return res;
    }*/

    /**
     * 调用财务中心作废,红冲开票金额
     *
     * @return Boolean
     */
    public Boolean invoiceVoidBatch(BillTypeEnum billTypeEnum, List<InvoiceReceiptDetailE> invoiceReceiptDetailES,String supCpUnitId) {
        List<InvoiceVoidBatchF> invoiceVoidBatchRFList = Lists.newArrayList();
        invoiceReceiptDetailES.forEach(invoiceReceiptDetailE -> {
            InvoiceVoidBatchF invoiceVoidBatchRF = new InvoiceVoidBatchF();
            invoiceVoidBatchRF.setBillId(invoiceReceiptDetailE.getBillId());
            invoiceVoidBatchRF.setInvoiceAmount(invoiceReceiptDetailE.getInvoiceAmount());
            invoiceVoidBatchRF.setInvoiceReceiptId(invoiceReceiptDetailE.getInvoiceReceiptId());
            invoiceVoidBatchRF.setSupCpUnitId(supCpUnitId);
            invoiceVoidBatchRF.setGatherBillId(invoiceReceiptDetailE.getGatherBillId());
            invoiceVoidBatchRF.setGatherDetailId(invoiceReceiptDetailE.getGatherDetailId());
            invoiceVoidBatchRF.setBillType(invoiceVoidBatchRF.getBillType());
            invoiceVoidBatchRFList.add(invoiceVoidBatchRF);
        });
        Boolean res = false;
        switch (billTypeEnum) {
            case 应收账单:
                res = receivableBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList);
                break;
            case 临时收费账单:
                res = temporaryChargeBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList);
                break;
            case 预收账单:
                res = advanceBillAppService.invoiceVoidBatch(invoiceVoidBatchRFList);
                break;
        }
        return res;
    }

    /**
     * 调用财务中心作废,红冲开票金额
     *
     * @return Boolean
     */
    public Boolean invoiceVoidBatch(List<InvoiceReceiptDetailE> invoiceReceiptDetailES,String supCpUnitId) {
        AtomicReference<Boolean> res = new AtomicReference<>(false);
        Map<Integer, List<InvoiceReceiptDetailE>> invoiceReceiptDetailMap = invoiceReceiptDetailES.stream().collect(Collectors.groupingBy(InvoiceReceiptDetailE::getBillType));
        invoiceReceiptDetailMap.forEach((billType, details) -> {
            res.set(invoiceVoidBatch(BillTypeEnum.valueOfByCode(billType), details, supCpUnitId));
        });
        return res.get();
    }


    /**
     * 调用财务中心完成开票
     * * 修改 收款单表[gather_bill] 开票金额、开票状态
     * * 修改 账单表 开票状态、挂账状态、开票金额
     *
     * @param invoiceReceiptDetailES
     * @param success
     */
    public Boolean handleBillStateFinishInvoice(List<InvoiceReceiptDetailE> invoiceReceiptDetailES, Boolean success,String supCpUnitId) {
        List<FinishInvoiceF> finishInvoiceFList = Lists.newArrayList();
        invoiceReceiptDetailES.forEach(invoiceReceiptDetailE -> {
            FinishInvoiceF finishInvoiceF = new FinishInvoiceF();
            finishInvoiceF.setBillId(invoiceReceiptDetailE.getBillId());
            //开票金额
            finishInvoiceF.setInvoiceAmount(invoiceReceiptDetailE.getInvoiceAmount());
            //票据id
            finishInvoiceF.setInvoiceReceiptId(invoiceReceiptDetailE.getInvoiceReceiptId());
            //开票状态:true 成功 false 失败
            finishInvoiceF.setSuccess(success);
            finishInvoiceF.setSupCpUnitId(supCpUnitId);
            finishInvoiceF.setGatherDetailId(invoiceReceiptDetailE.getGatherDetailId());
            finishInvoiceF.setGatherBillId(invoiceReceiptDetailE.getGatherBillId());
            finishInvoiceF.setBillType(invoiceReceiptDetailE.getBillType());
            finishInvoiceFList.add(finishInvoiceF);
        });
        boolean res = false;
        Map<Integer, List<FinishInvoiceF>> finishMaps = finishInvoiceFList.stream().collect(Collectors.groupingBy(FinishInvoiceF::getBillType));
        for(Map.Entry<Integer, List<FinishInvoiceF>> finishMap : finishMaps.entrySet()){
            switch (BillTypeEnum.valueOfByCode(finishMap.getKey())){
                case 应收账单:
                    res = receivableBillAppService.finishInvoiceBatch(finishMap.getValue());
                    break;
                case 临时收费账单:
                    res = temporaryChargeBillAppService.finishInvoiceBatch(finishMap.getValue());
                    break;
                case 预收账单:
                    res = advanceBillAppService.finishInvoiceBatch(finishMap.getValue());
                    break;
                case 默认:
                    break;
            }
        }
        return res;
    }

    /**
     * 根据账单ids和结算方式获取账单ids
     *
     * @param form
     * @return
     */
    public List<Long> listBillIdsByIdsAndChannel(SettleChannelAndIdsF form) {
        return billSettleAppService.listBillIdsByIdsAndChannel(form);
    }

    /**
     * 单个账单获取账单推凭信息
     *
     * @param billId
     * @param billType
     * @param actionEventCode
     * @return
     */
    public List<BillInferenceV> getBillInferenceInfo(Long billId, BillTypeEnum billType, int actionEventCode, String supCpUnitId) {
        BillInferenceF billInferenceF = new BillInferenceF();
        billInferenceF.setBillId(billId);
        billInferenceF.setActionEventCode(actionEventCode);
        billInferenceF.setSupCpUnitId(supCpUnitId);
        List<BillInferenceV> list = new ArrayList<>();
        switch (billType) {
            case 应收账单:
                if(StringUtils.isBlank(billInferenceF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("必须传入supCpUnitId !");
                }
                list = receivableBillAppService.listInferenceInfo(billInferenceF);
                break;
            case 预收账单:
                list = advanceBillAppService.listInferenceInfo(billInferenceF);
                break;
            case 收款单:
                if(StringUtils.isBlank(billInferenceF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("必须传入supCpUnitId !");
                }
                list = gatherBillAppService.listInferenceInfo(billInferenceF);
                break;
            case 应付账单:
                list = payableBillAppService.listInferenceInfo(billInferenceF);
                break;
            case 付款单:
                list = payBillAppService.listInferenceInfo(billInferenceF);
                break;
            case 临时收费账单:
                list = temporaryChargeBillAppService.listInferenceInfo(billInferenceF);
                break;
        }
        return list;
    }

    /**
     * 批量账单获取账单推凭信息
     *
     * @param billIds
     * @param billType
     * @param actionEventCode
     * @return
     */
    public List<BillInferenceV> getBillInferenceInfoByIds(List<Long> billIds, BillTypeEnum billType, int actionEventCode, String supCpUnitId) {
        BatchBillInferenceF billInferenceF = new BatchBillInferenceF();
        billInferenceF.setBillIds(billIds);
        billInferenceF.setActionEventCode(actionEventCode);
        billInferenceF.setSupCpUnitId(supCpUnitId);
        List<BillInferenceV> list = new ArrayList<>();
        switch (billType) {
            case 应收账单:
                list = receivableBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
            case 预收账单:
                list = advanceBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
            case 收款单:
                list = gatherBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
            case 应付账单:
                list = payableBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
            case 付款单:
                list = payBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
            case 临时收费账单:
                list = temporaryChargeBillAppService.listInferenceInfoByIds(billInferenceF);
                break;
        }
        return list;
    }

    /**
     * 插入账单推凭状态
     *
     * @param id
     * @param concatId
     * @param billType
     * @param eventType
     */
    public List<Long> insertInference(Long id, Long concatId, Integer billType, int eventType) {
        return batchInsertInference(Collections.singletonList(id), Collections.singletonList(concatId), billType, eventType);
    }

    /**
     * 获取账单推凭信息
     *
     * @param eventType
     * @param fieldList
     * @param billType
     * @param pageNum
     * @return
     */
    public PageV<BillInferenceV> listBillInferenceInfo(Integer eventType, List<Field> fieldList, BillTypeEnum billType, long pageNum) {
        ListBillInferenceF form = new ListBillInferenceF();
        form.setEventType(eventType);
        form.setFieldList(fieldList);
        form.setPageNum(pageNum);
        switch (billType) {
            case 应收账单:
                return receivableBillAppService.pageBillInferenceInfo(form, billType);
            case 应付账单:
                return payableBillAppService.pageBillInferenceInfo(form, billType);
            case 预收账单:
                return advanceBillAppService.pageBillInferenceInfo(form, billType);
            case 收款单:
                return gatherBillAppService.pageBillInferenceInfo(form, billType);
            case 付款单:
                return payBillAppService.pageBillInferenceInfo(form, billType);
            case 临时收费账单:
                return temporaryChargeBillAppService.pageBillInferenceInfo(form, billType);
            default:
                return null;
        }
    }

    /**
     * 批量插入账单推凭状态
     *
     * @param billIds
     * @param concatIds
     * @param billType
     * @param eventType
     */
    public List<Long> batchInsertInference(List<Long> billIds, List<Long> concatIds, Integer billType, int eventType) {
        BatchAddBillInferenceF form = new BatchAddBillInferenceF();
        form.setBillIds(billIds);
        form.setConcatIds(concatIds);
        form.setBillType(billType);
        form.setEventType(eventType);
        // 批量插入数据
        return billInferenceAppService.batchInsertInference(form);
    }

    /**
     * 分页查询账单
     *
     * @param queryF   queryF
     * @param billType billType
     * @return PageV
     */
    public PageV<BillPageInfoV> getPage(PageF<SearchF<?>> queryF, BillTypeEnum billType) {
        List<BillPageInfoV> billPageList = new ArrayList<>();
        long total = 0;
        switch (billType) {
            case 应收账单:
                PageQueryUtils.validQueryContainsFieldAndValue(queryF, "b." + BillSharedingColumn.应收账单.getColumnName());
                PageV<ReceivableBillPageV> receivableBillPageV = receivableBillAppService.getPage(queryF, ReceivableBillPageV.class);
                billPageList = Global.mapperFacade.mapAsList(receivableBillPageV.getRecords(), BillPageInfoV.class);
                total = receivableBillPageV.getTotal();
                break;
            case 临时收费账单:
                PageV<TemporaryChargeBillPageV> temporaryBillPageV = temporaryChargeBillAppService.getPage(queryF, TemporaryChargeBillPageV.class);
                billPageList = Global.mapperFacade.mapAsList(temporaryBillPageV.getRecords(), BillPageInfoV.class);
                total = temporaryBillPageV.getTotal();
                break;
            case 预收账单:
                PageV<AdvanceBillPageV> advanceBillPageV = advanceBillAppService.getPage(queryF, AdvanceBillPageV.class);
                billPageList = Global.mapperFacade.mapAsList(advanceBillPageV.getRecords(), BillPageInfoV.class);
                total = advanceBillPageV.getTotal();
                break;
            case 应付账单:
                PageV<PayableBillPageV> payableBillPageV = payableBillAppService.getPage(queryF, PayableBillPageV.class);
                billPageList = Global.mapperFacade.mapAsList(payableBillPageV.getRecords(), BillPageInfoV.class);
                total = payableBillPageV.getTotal();
                break;
            default:
                break;
        }
        return PageV.of(queryF, total, billPageList);
    }


    /**
     * 获取账单详细信息
     *
     * @return
     */
    public List<BillDetailMoreV> getAlldetailList(List<Long> billIds, BillTypeEnum billTypeEnum,String supCpUnitId) {
        List<BillDetailMoreV> detailMoreVList = Lists.newArrayList();
        for (Long billId : billIds) {
            BillDetailMoreV billDetailMoreV = this.getAllDetail(billId, billTypeEnum,supCpUnitId);
            Optional.ofNullable(billDetailMoreV).ifPresent(v -> {
                detailMoreVList.add(billDetailMoreV);
            });

        }
        return detailMoreVList;
    }

    /**
     * 获取账单详细信息
     *
     * @return
     */
    public List<BillDetailMoreV> getAlldetailList(List<BillDetailQueryDto> billDetailQueryDtos, String supCpUnitId) {
        List<BillDetailMoreV> detailMoreVList = Lists.newArrayList();
        for (BillDetailQueryDto queryDto : billDetailQueryDtos) {
            BillDetailMoreV billDetailMoreV = this.getAllDetail(queryDto.getBillId(),
                    BillTypeEnum.valueOfByCode(queryDto.getBillType()),supCpUnitId);
            Optional.ofNullable(billDetailMoreV).ifPresent(v -> {
                detailMoreVList.add(billDetailMoreV);
            });
        }
        return detailMoreVList;
    }

    /**
     * 获取账单详细信息
     *
     * @param billId   账单id
     * @param billType 账单类型
     * @param supCpUnitId 项目id
     * @return BillDetailMoreVo
     */
    public BillDetailMoreV getAllDetail(Long billId, BillTypeEnum billType,String supCpUnitId) {
        switch (billType) {
            case 应收账单:
            case 临时收费账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("必传supCpUnitId字段!");
                }
                ReceivableBillAllDetailV receivableBillDetail = receivableBillAppService.getAllDetail(billId, ReceivableBillAllDetailV.class,supCpUnitId);
                receivableBillDetail.billInvoiceAmount();
                return Global.mapperFacade.map(receivableBillDetail, BillDetailMoreV.class);
            case 预收账单:
                //查询预收里面会查收款，所以也要加校验
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("必传supCpUnitId字段!");
                }
                AdvanceBillAllDetailV advanceBillDetail = advanceBillAppService.getAllDetail(billId, AdvanceBillAllDetailV.class,supCpUnitId);
                advanceBillDetail.billInvoiceAmount();
                return Global.mapperFacade.map(advanceBillDetail, BillDetailMoreV.class);
            case 应付账单:
                PayableBillAllDetailV payableBillDetail = payableBillAppService.getAllDetail(billId, PayableBillAllDetailV.class,supCpUnitId);
                return Global.mapperFacade.map(payableBillDetail, BillDetailMoreV.class);
            default:
                return null;
        }
    }

    /**
     * 临时账单id
     * @param billIds
     * @param billType
     * @return
     */
    public List<BillDetailMoreV> getAllDetails(List<Long> billIds, BillTypeEnum billType,String supCpUnitId) {
        //临时性代码，后续优化
        List<BillDetailMoreV> billDetailMoreVS = new ArrayList<>();
        BillDetailMoreV billDetailMoreV = new BillDetailMoreV();
        for (Long billId : billIds) {
            switch (billType) {
                case 应收账单:
                case 临时收费账单:
                    ReceivableBillAllDetailV receivableBillDetail = receivableBillAppService.getAllDetail(billId, ReceivableBillAllDetailV.class,supCpUnitId);
                    receivableBillDetail.billInvoiceAmount();
                    billDetailMoreV = Global.mapperFacade.map(receivableBillDetail, BillDetailMoreV.class);
                    billDetailMoreV.setReceiptBillId(billId);
                    billDetailMoreV.setReceiptBillInvoiceState(receivableBillDetail.getInvoiceState());
                    billDetailMoreVS.add(billDetailMoreV);
                    break;
                case 预收账单:
                    AdvanceBillAllDetailV advanceBillDetail = advanceBillAppService.getAllDetail(billId, AdvanceBillAllDetailV.class,supCpUnitId);
                    advanceBillDetail.setBillType(String.valueOf(billType.getCode()));
                    advanceBillDetail.billInvoiceAmount();
                    billDetailMoreV = Global.mapperFacade.map(advanceBillDetail, BillDetailMoreV.class);
                    billDetailMoreV.setReceiptBillId(billId);
                    billDetailMoreV.setReceiptBillInvoiceState(advanceBillDetail.getInvoiceState());
                    billDetailMoreVS.add(billDetailMoreV);
                    break;
                case 应付账单:
                    PayableBillAllDetailV payableBillDetail = payableBillAppService.getAllDetail(billId, PayableBillAllDetailV.class,supCpUnitId);
                    payableBillDetail.setBillType(String.valueOf(billType.getCode()));
                    billDetailMoreVS.add(Global.mapperFacade.map(payableBillDetail, BillDetailMoreV.class));
                    break;
                case 收款单:
                    GatherBillDetailV gatherBillDetailV = gatherBillAppService.queryDetailById(billId, supCpUnitId);
                    if (Objects.nonNull(gatherBillDetailV)){
                        for (GatherDetailV gatherDetail : gatherBillDetailV.getGatherDetails()) {
                            if (Objects.nonNull(gatherDetail.getRecBillId())){
                                ReceivableBillAllDetailV receivableBillAllDetailV = receivableBillAppService.getAllDetail(gatherDetail.getRecBillId(), ReceivableBillAllDetailV.class,supCpUnitId);
                                billDetailMoreVS.add(Global.mapperFacade.map(receivableBillAllDetailV, BillDetailMoreV.class));
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return billDetailMoreVS;
    }

    /**
     * 获取开票详情
     *
     * @param gatherDetailList
     * @return
     */
    public List<BillDetailMoreV> batchGetGatherDetailBillDetail(List<GatherDetail> gatherDetailList, String supCpUnitId) {
        List<BillDetailMoreV> billDetailMoreVS = new ArrayList<>();
        List<BillDetailMoreV> billDetailMoreVSTemp;
        Map<Long, List<BillDetailMoreV>> billMap;
        Map<Integer, List<GatherDetail>> gatherDetailMaps = gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherType));
        for (Map.Entry<Integer, List<GatherDetail>> gatherDetailMap : gatherDetailMaps.entrySet()) {
            List<Long> billIds = gatherDetailMap.getValue().stream().map(GatherDetail::getRecBillId).distinct().collect(Collectors.toList());
            switch (GatherTypeEnum.valueOfByCode(gatherDetailMap.getKey())) {
                case 应收:
                    List<ReceivableBillAllDetailV> receivableBillAllDetailVS = billIds.stream()
                            .filter(Objects::nonNull)
                            .map(billId -> {
                                ReceivableBillAllDetailV allDetail = receivableBillAppService.getAllDetail(billId, ReceivableBillAllDetailV.class, supCpUnitId);
                                allDetail.billInvoiceAmount();
                                return allDetail;
                            }).collect(Collectors.toList());
                    log.info("batchGetGatherDetailBillDetail receivableBillAllDetailVS：{}",JSONObject.toJSONString(receivableBillAllDetailVS));
                    billDetailMoreVSTemp = Global.mapperFacade.mapAsList(receivableBillAllDetailVS,BillDetailMoreV.class);
                    billMap = billDetailMoreVSTemp.stream().collect(Collectors.groupingBy(BillDetailMoreV::getBillId));
                    for (GatherDetail gatherDetail : gatherDetailMap.getValue()) {
                        BillDetailMoreV detailMoreV = createBillDetailMoreV(gatherDetail, billMap.get(gatherDetail.getRecBillId()).get(0));
                        if (detailMoreV.getCanInvoiceAmount() > 0L) {
                            billDetailMoreVS.add(detailMoreV);
                        }
                    }
                    break;
                case 预收:
                    List<AdvanceBillAllDetailV> advanceBillAllDetailVS = billIds.stream()
                            .filter(Objects::nonNull)
                            .map(billId -> advanceBillAppService.getAllDetail(billId, AdvanceBillAllDetailV.class, supCpUnitId)).collect(Collectors.toList());
                    billDetailMoreVSTemp = Global.mapperFacade.mapAsList(advanceBillAllDetailVS,BillDetailMoreV.class);
                    billMap = billDetailMoreVSTemp.stream().collect(Collectors.groupingBy(BillDetailMoreV::getBillId));
                    for (GatherDetail gatherDetail : gatherDetailMap.getValue()) {
                        BillDetailMoreV detailMoreV = createBillDetailMoreV(gatherDetail, billMap.get(gatherDetail.getRecBillId()).get(0));
                        detailMoreV.setBillType(String.valueOf(BillTypeEnum.预收账单.getCode()));
                        if (detailMoreV.getCanInvoiceAmount() > 0L) {
                            billDetailMoreVS.add(detailMoreV);
                        }
                    }
                    break;
            }
        }
        return billDetailMoreVS;
    }
    private BillDetailMoreV createBillDetailMoreV(GatherDetail gatherDetail, BillDetailMoreV billDetailMoreV) {
        log.info("createBillDetailMoreV gatherDetail：{}", JSONObject.toJSONString(gatherDetail));
        log.info("createBillDetailMoreV billDetailMoreV：{}", JSONObject.toJSONString(billDetailMoreV));
        BillDetailMoreV detailMoreV = Global.mapperFacade.map(gatherDetail, BillDetailMoreV.class);
        detailMoreV.setReceivableAmount(gatherDetail.getRecPayAmount());
        detailMoreV.setDeductibleAmount(billDetailMoreV.getDeductibleAmount());
        detailMoreV.setCanInvoiceAmount(gatherDetail.getCanInvoiceAmount());
        detailMoreV.setTotalAmount(gatherDetail.getPayAmount());
        detailMoreV.setSettleAmount(gatherDetail.getRemainingCarriedAmount());
        detailMoreV.setBillId(gatherDetail.getRecBillId());
        detailMoreV.setId(gatherDetail.getRecBillId());
        detailMoreV.setGatherDetailId(gatherDetail.getId());
        detailMoreV.setBillNo(gatherDetail.getRecBillNo());
        detailMoreV.setGatherDetailId(gatherDetail.getId());
        detailMoreV.setActualPayAmount(gatherDetail.getCanRefundAmount());
        detailMoreV.setStartTime(billDetailMoreV.getStartTime());
        detailMoreV.setEndTime(billDetailMoreV.getEndTime());
        detailMoreV.setBillMethod(billDetailMoreV.getBillMethod());
        detailMoreV.setUnitPrice(billDetailMoreV.getUnitPrice());
        detailMoreV.setTaxRate(billDetailMoreV.getTaxRate());
        detailMoreV.setBillType(billDetailMoreV.getBillType());
        detailMoreV.setSettleState(billDetailMoreV.getSettleState());
        detailMoreV.setRoomId(billDetailMoreV.getRoomId());
        detailMoreV.setRoomName(billDetailMoreV.getRoomName());
        detailMoreV.setCommunityId(billDetailMoreV.getCommunityId());
        detailMoreV.setCommunityName(billDetailMoreV.getCommunityName());
        detailMoreV.setStatutoryBodyId(billDetailMoreV.getStatutoryBodyId());
        detailMoreV.setStatutoryBodyName(billDetailMoreV.getStatutoryBodyName());
        detailMoreV.setBillSettleDtos(billDetailMoreV.getBillSettleDtos());
        detailMoreV.setInvoiceState(billDetailMoreV.getInvoiceState());
        detailMoreV.setReceiptBillId(gatherDetail.getRecBillId());
        detailMoreV.setReceiptBillInvoiceState(billDetailMoreV.getInvoiceState());
        detailMoreV.setReceiptGatherDetailBillId(gatherDetail.getId());
        detailMoreV.setReceiptGatherDetailInvoiceState(gatherDetail.getInvoiceState());
        detailMoreV.setOverdue(billDetailMoreV.getOverdue());
        detailMoreV.setPayerId(billDetailMoreV.getPayerId());
        detailMoreV.setSysSource(billDetailMoreV.getSysSource());
        detailMoreV.setSource(billDetailMoreV.getSource());
        detailMoreV.setCostCenterId(billDetailMoreV.getCostCenterId());
        detailMoreV.setCostCenterName(billDetailMoreV.getCostCenterName());
        return detailMoreV;
    }


    /**
     * 批量结算
     */
    public Boolean batchSettle(List<AddBillSettleF> addBillSettleRFList, BillTypeEnum billTypeEnum) {
        Boolean res = null;
        switch (billTypeEnum) {
            case 应收账单:
                addBillSettleRFList.forEach(v -> {
                    if(StringUtils.isBlank(v.getSupCpUnitId())) {
                        throw new IllegalArgumentException("处理应收账单时，每笔数据都需要传入上级收费单元ID(supCpUnitId)字段!");
                    }
                });
                res = receivableBillAppService.settleBatch(addBillSettleRFList) != null;
                break;
            case 应付账单:
                res = payableBillAppService.settleBatch(addBillSettleRFList) != null;
                break;
        }
        return res;
    }

    /**
     * 发起审核申请
     *
     * @param billApplyF  发起审核命令
     * @param billType 账单类型
     * @return Boolean
     */
    public Boolean apply(BillApplyF billApplyF, BillTypeEnum billType) {
        switch (billType) {
            case 应收账单:
                return Objects.nonNull(receivableBillAppService.apply(billApplyF));
            case 临时收费账单:
                return Objects.nonNull(temporaryChargeBillAppService.apply(billApplyF));
            case 预收账单:
                return Objects.nonNull(advanceBillAppService.apply(billApplyF));
            case 应付账单:
                Long applyFlag = payableBillAppService.apply(billApplyF);
                if (Objects.nonNull(applyFlag)) {
                    // TODO 应付单目前没有审核页面，直接调用审核接口，中台审核页面加上之后删除
                    ApproveBillF approveBillRF = new ApproveBillF();
                    approveBillRF.setBillId(billApplyF.getBillId());
                    approveBillRF.setApproveState(BillApproveStateEnum.已审核.getCode());
                    approveBillRF.setOperateType(billApplyF.getApproveOperateType());
                    return payableBillAppService.approve(approveBillRF);
                }
                return false;
            case 收款单:
                Long applyId = gatherBillAppService.apply(billApplyF);
                if (applyId != null) {
                    return true;
                }
            default:
                return false;
        }
    }

    /**
     * 账单作废
     *
     * @param command  账单作废命令
     * @param billType 账单类型
     * @return Boolean
     */
    public Boolean invalid(BillInvalidCommand command, BillTypeEnum billType) {

        switch (billType) {
            case 应收账单:
                ReceivableBillInvalidF billInvalidF = Global.mapperFacade.map(command, ReceivableBillInvalidF.class);
                return receivableBillAppService.invalid(billInvalidF);
            case 临时收费账单:
                TemporaryChargeBillInvalidF temporaryChargeBillInvalidF = Global.mapperFacade.map(command, TemporaryChargeBillInvalidF.class);
                return temporaryChargeBillAppService.invalid(temporaryChargeBillInvalidF);
            case 预收账单:
                AdvanceBillInvalidF advanceBillInvalidF = Global.mapperFacade.map(command, AdvanceBillInvalidF.class);
                return advanceBillAppService.invalid(advanceBillInvalidF);
            case 应付账单:
                PayableBillInvalidF payableBillInvalidF = Global.mapperFacade.map(command, PayableBillInvalidF.class);
                return payableBillAppService.invalid(payableBillInvalidF);
            default:
                return false;
        }
    }

    /**
     * 账单批量作废
     *
     * @param command  批量作废命令
     * @param billType 账单类型
     * @return BillBatchResultDto
     */
    public BillBatchResultDto batchInvalid(BillInvalidBatchCommand command, BillTypeEnum billType, String supCpUnitId) {
        switch (billType) {
            case 应收账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("上级收费单元ID不能为空!");
                }
                BillBatchResultDto receivableBillBatchResultRV = receivableBillAppService.invalidBatch(command.getBillIdList(), supCpUnitId);
                return Global.mapperFacade.map(receivableBillBatchResultRV, BillBatchResultDto.class);
            case 临时收费账单:
                if(StringUtils.isBlank(supCpUnitId)) {
                    throw new IllegalArgumentException("上级收费单元ID不能为空!");
                }
                BillBatchResultDto tempBillBatchResultRV = temporaryChargeBillAppService.invalidBatch(command.getBillIdList(), supCpUnitId);
                return Global.mapperFacade.map(tempBillBatchResultRV, BillBatchResultDto.class);
            case 预收账单:
                BillBatchResultDto advanceBatchResultRV = advanceBillAppService.invalidBatch(command.getBillIdList(), supCpUnitId);
                return Global.mapperFacade.map(advanceBatchResultRV, BillBatchResultDto.class);
            case 应付账单:
                BillBatchResultDto payableBatchResultRV = payableBillAppService.invalidBatch(command.getBillIdList(), supCpUnitId);
                return Global.mapperFacade.map(payableBatchResultRV, BillBatchResultDto.class);
            default:
                return null;
        }
    }

    /**
     * 审核账单
     *
     * @param approveReceivableBillF
     */
    public Boolean approve(ApproveReceivableBillF approveReceivableBillF) {
        Boolean approve = null;
        switch (BillTypeEnum.valueOfByCode(approveReceivableBillF.getBillType())) {
            case 应收账单:
                if(StringUtils.isBlank(approveReceivableBillF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("处理应收账单时，必须传上级收费单元ID(supCpUnitId)字段!");
                }
                approve = receivableBillAppService.approve(approveReceivableBillF);
                break;
            case 预收账单:
                approve = advanceBillAppService.approve(approveReceivableBillF);
                break;
            case 临时收费账单:
                approve = temporaryChargeBillAppService.approve(approveReceivableBillF);
                break;
            case 应付账单:
                approve = payableBillAppService.approve(approveReceivableBillF);
                break;
            case 收款单:
                if(StringUtils.isBlank(approveReceivableBillF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("处理收款账单时，必须传上级收费单元ID(supCpUnitId)字段!");
                }
                ApproveGatherBillF approveGatherBillF = Global.mapperFacade.map(approveReceivableBillF, ApproveGatherBillF.class);
                approve = gatherBillAppService.approve(approveGatherBillF);
                break;
            default:
                break;
        }
        return approve;
    }

    /**
     * 账单批量申请
     *
     * @param billApplyBatchF  批量申请命令
     * @param billType 账单类型
     * @return Boolean
     */
    public Boolean applyBatch(BillApplyBatchF billApplyBatchF, BillTypeEnum billType) {
        switch (billType) {
            case 应收账单:
                if(StringUtils.isBlank(billApplyBatchF.getSupCpUnitId())) {
                    throw new IllegalArgumentException("处理应收账单必须传入上级收费单元ID(supCpUnitId)字段!");
                }
                return receivableBillAppService.applyBatch(billApplyBatchF);
            case 临时收费账单:
                return temporaryChargeBillAppService.applyBatch(billApplyBatchF);
            case 预收账单:
                return advanceBillAppService.applyBatch(billApplyBatchF);
            case 应付账单:
                return payableBillAppService.applyBatch(billApplyBatchF);
            default:
                return false;
        }
    }

    /**
     * 相同法定单位，收费对象，账单来源，项目/成本中心(对于含项目或成本中心的账单)
     *
     * @param billOjvs
     */
    public void checkBillDetail(List<BillDetailMoreV> billOjvs, AddInvoiceCommand command) {

        //校检账单中法定单位是否相同
        List<Long> statutoryBodyIds = billOjvs.stream().map(BillDetailMoreV::getStatutoryBodyId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(statutoryBodyIds) && statutoryBodyIds.size() != 1) {
            throw BizException.throw400("该批次账单法定单位不一致");
        }

        //校检账单中收费对象是否相同
        if (!EnvConst.FANGYUAN.equals(EnvData.config)) {
            List<String> payerIds = billOjvs.stream().map(BillDetailMoreV::getPayerId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(payerIds) && payerIds.size() != 1) {
                throw BizException.throw400("该批次账单收费对象不一致");
            }
        }

        //校检账单中账单来源是否相同
        List<Integer> sourceIds = billOjvs.stream().map(BillDetailMoreV::getSysSource).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sourceIds) && sourceIds.size() != 1) {
            throw BizException.throw400("该批次账单账单来源不一致");
        }

        //校检账单中项目是否相同
        List<String> communityIds = billOjvs.stream().map(BillDetailMoreV::getCommunityId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(communityIds) && communityIds.size() != 1) {
            throw BizException.throw400("该批次账单账单项目不一致");
        }

        //校检账单中成本中心是否相同
        List<Long> costCenterIds = billOjvs.stream().map(BillDetailMoreV::getCostCenterId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(costCenterIds) && costCenterIds.size() != 1) {
            throw BizException.throw400("该批次账单账单成本中心不一致");
        }

        //校检账单中是否存在零税率
        if(Objects.nonNull(command)){
            List<BillDetailMoreV> detailMoreVS = billOjvs.stream().filter(billDetailMoreV -> BigDecimal.ZERO.compareTo(billDetailMoreV.getTaxRate()) == 0).collect(Collectors.toList());
            if (InvoiceLineEnum.全电专票.getCode() == command.getType()) {
                if(1 == command.getFreeTax() || CollectionUtils.isNotEmpty(detailMoreVS)){
                    throw BizException.throw400("全电专票不允许免税及零税率开票");
                }
            }
        }

        //校检是否存在已经开票账单
        checkBillOjvsInvoice(billOjvs);
    }

    /**
     * 校检是否存在已经开票账单
     *
     * @param billDetailMoreVList
     */
    private void checkBillOjvsInvoice(List<BillDetailMoreV> billDetailMoreVList) {
        for (BillDetailMoreV billDetailMoreV : billDetailMoreVList) {
            //'开票状态：0未开票，1开票中，2部分开票，3已开票
            if (billDetailMoreV.getInvoiceState().equals(BillInvoiceStateEnum.已开票.getCode())) {
                throw BizException.throw400(ErrMsgEnum.BILL_INVOICED.getErrMsg());
            }
            if (billDetailMoreV.getInvoiceState().equals(BillInvoiceStateEnum.开票中.getCode())) {
                throw BizException.throw400(ErrMsgEnum.BILL_INVOICING.getErrMsg());
            }
        }

    }

    /**
     * 获取账单推凭信息
     *
     * @param eventType
     * @param fieldList
     * @param billType
     * @param pageNum
     * @return
     */
    public PageV<BillInferenceV> listBillInferenceInfoByDetail(Integer eventType, List<Field> fieldList, BillTypeEnum billType, long pageNum) {
        ListBillInferenceF form = new ListBillInferenceF();
        form.setEventType(eventType);
        form.setFieldList(fieldList);
        form.setPageNum(pageNum);
        switch (billType) {
            case 收款单:
                return gatherBillAppService.pageBillInferenceInfo(form, billType);
            case 付款单:
                return payBillAppService.pageBillInferenceInfo(form, billType);
            default:
                return null;
        }
    }

    /**
     * 获取账单退款记录
     *
     * @param id
     * @return
     */
    public List<BillRefundDto> listBillRefund(Long id) {
        return billRefundAppService.getByBillId(id);
    }

    /**
     * 修改退款记录的推凭状态
     *
     * @param inferRefundIds
     */
    public void batchUpdateRefundInferenceState(Set<Long> inferRefundIds) {
        billRefundAppService.batchUpdateRefundInferenceState(new ArrayList<>(inferRefundIds));
    }

    /**
     * 删除推凭失败的数据
     *
     * @param inferIds
     * @param eventTypeEnum
     * @param billTypeEnum
     * @param concatIds
     */
    public void delBillInferencesByIds(List<Long> inferIds, EventTypeEnum eventTypeEnum, BillTypeEnum billTypeEnum, List<Long> concatIds) {
        BatchDelBillInferenceF batchDelBillInferenceF = new BatchDelBillInferenceF();
        batchDelBillInferenceF.setInferIds(inferIds);
        batchDelBillInferenceF.setEventType(eventTypeEnum.getEvent());
        batchDelBillInferenceF.setBillType(billTypeEnum.getCode());
        batchDelBillInferenceF.setConcatIds(concatIds);
        billInferenceAppService.batchDeleteInference(batchDelBillInferenceF);
    }

    /**
     * 重新计算设置开票状态
     * @param billId
     * @param billType
     */
    public void reSetBillInvoiceState(Long billId, BillTypeEnum billType, String supCpUnitId) {
        switch (billType) {
            case 应收账单:
                receivableBillAppService.reSetBillInvoiceState(billId, supCpUnitId);
                break;
            case 临时收费账单:
                temporaryChargeBillAppService.reSetBillInvoiceState(billId, supCpUnitId);
                break;
            case 预收账单:
                advanceBillAppService.reSetBillInvoiceState(billId, supCpUnitId);
                break;
            case 默认:
                break;
        }
    }

    /**
     * 账单减免批量申请
     *
     * @param billApplyBatchF  批量申请命令
     * @param billType 账单类型
     * @return Boolean
     */
    public ApplyBatchDeductionV applyBatchDeduction(BillApplyBatchF billApplyBatchF, BillTypeEnum billType) {
        switch (billType) {
            case 应收账单:
                return receivableBillAppService.applyBatchDeduction(billApplyBatchF);
            case 临时收费账单:
                return temporaryChargeBillAppService.applyBatchDeduction(billApplyBatchF);
            case 预收账单:
                return advanceBillAppService.applyBatchDeduction(billApplyBatchF);
            case 应付账单:
                return payableBillAppService.applyBatchDeduction(billApplyBatchF);
            default:
                return new ApplyBatchDeductionV();
        }
    }

}
