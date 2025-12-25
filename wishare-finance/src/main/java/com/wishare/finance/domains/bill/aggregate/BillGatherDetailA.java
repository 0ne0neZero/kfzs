package com.wishare.finance.domains.bill.aggregate;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceDetailAndReceiptV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.CarryoverTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.PaySourceEnum;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.repository.InvoiceReceiptDetailRepository;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description: 账单收款明细聚合
 */
@Getter
@Slf4j
public class BillGatherDetailA<B extends Bill> {

    /**
     * 账单信息
     */
    private B bill;

    /**
     * 收款信息
     */
    private GatherBill gatherBill;

    /**
     * 收款详情信息
     */
    private GatherDetail gatherDetail;

    /**
     * 抵扣预收账单
     */
    private AdvanceBill updateAdvanceBill;

    /**
     * 抵扣或收费溢出预收账单
     */
    private AdvanceBill saveAdvanceBill;

    /**
     *  转预收
     */
    private BillCarryoverA<B, AdvanceBill> carryoverAdvanceBillCarryoverA;

    /**
     * 账单抵扣
     */
    private BillCarryoverA<AdvanceBill, B> discountBillCarryoverA;

    /**
     * 结转类型
     */
    private CarryoverTypeEnum carryoverTypeEnum;


    public BillGatherDetailA() {
    }


    public BillGatherDetailA(B bill) {
        this.bill = bill;
    }

    public BillGatherDetailA(B bill, GatherBill gatherBill) {
        this.bill = bill;
        this.gatherBill = gatherBill;
    }

    public BillGatherDetailA(B bill, GatherBill gatherBill, GatherDetail gatherDetail) {
        this.bill = bill;
        this.gatherBill = gatherBill;
        this.gatherDetail = gatherDetail;
    }

    /**
     * 抵扣收款
     *
     */
    public void discountBillCarryover(AddBillSettleCommand command, AdvanceBill advanceBill, BillCarryoverDetailRepository billCarryoverDetailRepository) {
        this.updateAdvanceBill = advanceBill;
        this.carryoverTypeEnum = CarryoverTypeEnum.抵扣;
        BillCarryoverE billCarryoverE = generalBillCarryover(command);
        BillCarryoverA<AdvanceBill, B> advanceBillBBillCarryoverA =
                new BillCarryoverA<>(advanceBill, null, billCarryoverE, Lists.newArrayList(this.bill));
        this.discountBillCarryoverA = advanceBillBBillCarryoverA;
        advanceBillBBillCarryoverA.setGatherBill(this.gatherBill);
        //结转
        advanceBillBBillCarryoverA.carryover();
        this.gatherDetail = CollectionUtils.isEmpty(advanceBillBBillCarryoverA.getTargetSettles())?null:advanceBillBBillCarryoverA.getTargetSettles().get(0);
        this.bill.setPayTime(Optional.ofNullable(command.getSettleTime()).orElse(LocalDateTime.now()));
        this.bill.refresh();
    }


    /**
     * 构建转预收结转
     *
     * @return BillCarryoverE
     */
    private BillCarryoverE generalCarryoverAdvanceBillCarryover(AddBillSettleCommand addBillSettleCommand,
                                                                long overFlowAmount) {
        BillCarryoverE billCarryoverE = new BillCarryoverE();
        billCarryoverE.setCarryoverAmount(overFlowAmount);
        billCarryoverE.setCarriedBillId(this.bill.getId());
        billCarryoverE.setCarriedBillNo(this.bill.getBillNo());
        billCarryoverE.setCarryoverType(2);
        billCarryoverE.setApproveTime(LocalDateTime.now());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        CarryoverDetail carryoverDetail = new CarryoverDetail();
        carryoverDetail.setCarryoverAmount(overFlowAmount);
        carryoverDetail.setChargeStartTime(this.bill.getStartTime());
        carryoverDetail.setChargeEndTime(this.bill.getEndTime());
        carryoverDetail.setChargeItemId(this.bill.getChargeItemId());
        carryoverDetail.setChargeItemName(this.bill.getChargeItemName());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        billCarryoverE.setCarryoverDetail(Lists.newArrayList(carryoverDetail));
        billCarryoverE.setAdvanceCarried(1);
        billCarryoverE.setState(2);
        billCarryoverE.setRemark(StringUtils.isBlank(addBillSettleCommand.getRemark()) ? "" :
                addBillSettleCommand.getRemark());
        billCarryoverE.setBillType(this.bill.getType());
        return billCarryoverE;
    }

    /**
     * 构建抵扣结转
     *
     * @return BillCarryoverE
     */
    private BillCarryoverE generalBillCarryover(AddBillSettleCommand addBillSettleCommand) {
        BillCarryoverE billCarryoverE = new BillCarryoverE();
        billCarryoverE.setCarriedBillId(addBillSettleCommand.getCarriedBillId());
        billCarryoverE.setCarriedBillNo(this.updateAdvanceBill.getBillNo());
        billCarryoverE.setCarryoverAmount(addBillSettleCommand.getCarriedAmount());
        billCarryoverE.setCarryoverType(1);
        billCarryoverE.setApproveTime(LocalDateTime.now());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        CarryoverDetail carryoverDetail = new CarryoverDetail();
        carryoverDetail.setTargetBillId(this.bill.getId());
        carryoverDetail.setTargetBillNo(this.bill.getBillNo());
        carryoverDetail.setCarryoverAmount(addBillSettleCommand.getCarriedAmount());
        carryoverDetail.setChargeStartTime(this.bill.getStartTime());
        carryoverDetail.setChargeEndTime(this.bill.getEndTime());
        billCarryoverE.setCarryoverTime(LocalDateTime.now());
        billCarryoverE.setCarryoverDetail(Lists.newArrayList(carryoverDetail));
        billCarryoverE.setAdvanceCarried(0);
        billCarryoverE.setState(2);
        billCarryoverE.setCarryRule(addBillSettleCommand.getCarryRule());
        billCarryoverE.setAutoCarryRule(addBillSettleCommand.getAutoCarryRule());
        billCarryoverE.setRemark(StringUtils.isBlank(addBillSettleCommand.getRemark()) ? "" :
                addBillSettleCommand.getRemark());
        billCarryoverE.setBillType(this.bill.getType());
        return billCarryoverE;
    }

    /**
     * 账单收款
     */
    public boolean gather(AddBillSettleCommand command) {
        gatherDetail = getGatherBillDetail(gatherBill, command);
        bill.setPayTime(Optional.ofNullable(command.getSettleTime()).orElse(LocalDateTime.now()));
        //HXYUN-16963 缴费成功更新原账单收费对象
        bill.setCustomerLabel(command.getPayerLabel());
        bill.setCustomerId(command.getPayerId());
        bill.setCustomerName(command.getPayerName());
        bill.setCustomerType(command.getPayerType());
        bill.setPayerLabel(command.getPayerLabel());
        bill.setPayerId(command.getPayerId());
        bill.setPayerName(command.getPayerName());
        bill.setPayerType(command.getPayerType());
        if (bill instanceof ReceivableBill){
            ((ReceivableBill) bill).setPayerPhone(gatherDetail.getPayerPhone());
        }else if (bill instanceof TemporaryChargeBill){
            ((TemporaryChargeBill) bill).setPayerPhone(gatherDetail.getPayerPhone());
        }else if (bill instanceof AdvanceBill){
            ((AdvanceBill) bill).setPayerPhone(gatherDetail.getPayerPhone());
        }

        Long settleAmount = command.getSettleAmount();
        Long discountAmount = command.getDiscountAmount();
        if (discountAmount != null) {
            settleAmount = settleAmount + command.getDiscountAmount();
        }else {
            discountAmount = 0L;
        }
        // 收费溢出金额
        long overFlowAmount = bill.canOverFlowSettle(settleAmount, discountAmount);
        if (overFlowAmount > 0) {
            log.info("处理收费溢出金额：{}",overFlowAmount);
            this.carryoverTypeEnum = CarryoverTypeEnum.结转预收;
            BillCarryoverE billCarryoverE = generalCarryoverAdvanceBillCarryover(command, overFlowAmount);
            BillCarryoverA<B, AdvanceBill> carryoverAdvanceBillCarryoverA =
                    new BillCarryoverA<>(this.bill, null, billCarryoverE, null);
            carryoverAdvanceBillCarryoverA.setGatherBill(this.gatherBill);
            carryoverAdvanceBillCarryoverA.carryover();
            this.carryoverAdvanceBillCarryoverA = carryoverAdvanceBillCarryoverA;
            this.saveAdvanceBill = carryoverAdvanceBillCarryoverA.getAdvanceBill();
            this.saveAdvanceBill.setPayChannel(command.getSettleChannel());
            //添加管家催收信息字段 2023-06-21 李彪 start
            this.saveAdvanceBill.setExtField1(command.getExtField1());
            this.saveAdvanceBill.setExtField2(command.getExtField2());
            //添加管家催收信息字段 2023-06-21 李彪 end
            if (Objects.nonNull(command.getChargeItemId())) {
                this.saveAdvanceBill.setChargeItemId(command.getChargeItemId());
                this.saveAdvanceBill.setChargeItemName(command.getChargeItemName());
            }
            if (Objects.nonNull(command.getPayerType())) {
                this.saveAdvanceBill.setPayerId(command.getPayerId());
                this.saveAdvanceBill.setPayerName(command.getPayerName());
                this.saveAdvanceBill.setPayerType(command.getPayerType());
                this.saveAdvanceBill.setPayerPhone(command.getPayerPhone());
                this.saveAdvanceBill.setPayerLabel(command.getPayerLabel());
                this.saveAdvanceBill.setCustomerId(command.getPayerId());
                this.saveAdvanceBill.setCustomerName(command.getPayerName());
                this.saveAdvanceBill.setCustomerType(command.getPayerType());
                this.saveAdvanceBill.setCustomerLabel(command.getPayerLabel());
            }
            //D7704 中交定制多缴到预存账户，税率统一都0
            if(EnvConst.ZHONGJIAO.equals(EnvData.config)){
                this.saveAdvanceBill.setTaxRate(new BigDecimal("0.00"));
                this.saveAdvanceBill.setTaxRateId(136157205772010L);
            }
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayeeId(command.getPayeeId());
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayeeName(command.getPayeeName());
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayeePhone(command.getPayeePhone());
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayerPhone(command.getPayerPhone());
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayChannel(gatherDetail.getPayChannel());
            this.carryoverAdvanceBillCarryoverA.getAdvanceBillSettle().setPayWay(gatherDetail.getPayWay());
            gatherDetail.setPayAmount(gatherDetail.getPayAmount() - overFlowAmount);
            this.bill.setSettleAmount(this.bill.getSettleAmount() - overFlowAmount);
            this.bill.setCarriedAmount(this.bill.getCarriedAmount() - overFlowAmount);
//            gatherDetail.setCarriedAmount(overFlowAmount);
//            gatherDetail.setCarriedBillId(saveAdvanceBill.getId());
//            gatherDetail.setCarriedBillNo(saveAdvanceBill.getBillNo());
//            gatherDetail.setCarriedBillType(saveAdvanceBill.getType());
        }
        if (bill instanceof ReceivableBill) {
            ReceivableBill receivableBill = (ReceivableBill) bill;
//            gatherDetail.setChargeStartTime(Objects.isNull(receivableBill.getChargeTime()) ?
//                    receivableBill.getStartTime() : receivableBill.getChargeTime().plusSeconds(1L));
            receivableBill.resetChargeTime();
//            gatherDetail.setChargeEndTime(receivableBill.getChargeTime());
            PayInfo payInfo = new PayInfo(command.getSettleWay(), command.getSettleChannel(), command.getPayAmount(),gatherBill.getPaySource());
            if (CollectionUtils.isNotEmpty(receivableBill.getPayInfos())){
                receivableBill.getPayInfos().add(payInfo);
            }else {
                List<PayInfo> payInfos = new ArrayList<>();
                payInfos.add(payInfo);
                receivableBill.setPayInfos(payInfos);
            }
        }else if (bill instanceof TemporaryChargeBill) {
            TemporaryChargeBill temporaryChargeBill = (TemporaryChargeBill) bill;
//            gatherDetail.setChargeStartTime(Objects.isNull(temporaryChargeBill.getChargeTime()) ?
//                    temporaryChargeBill.getStartTime() : temporaryChargeBill.getChargeTime().plusSeconds(1L));
            temporaryChargeBill.resetChargeTime();
//            gatherDetail.setChargeEndTime(temporaryChargeBill.getChargeTime());
            PayInfo payInfo = new PayInfo(command.getSettleWay(), command.getSettleChannel(), command.getPayAmount(),gatherBill.getPaySource());
            if (CollectionUtils.isNotEmpty(temporaryChargeBill.getPayInfos())){
                temporaryChargeBill.getPayInfos().add(payInfo);
            }else {
                List<PayInfo> payInfos = new ArrayList<>();
                payInfos.add(payInfo);
                temporaryChargeBill.setPayInfos(payInfos);
            }
        }
        this.bill.refresh();
        return true;
    }

    /**
     * 构建收款明细
     *
     * @param gatherBill
     * @param command
     * @return
     */
    private GatherDetail getGatherBillDetail(GatherBill gatherBill, AddBillSettleCommand command) {
        GatherDetail detail = new GatherDetail();
        detail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_DETAIL));
        detail.setGatherType(GatherDetail.getGatherType(bill).getCode());
        detail.setGatherBillId(gatherBill.getId());
        detail.setGatherBillNo(gatherBill.getBillNo());
        if(BillTypeEnum.应收账单.equalsByCode(bill.getType()) || BillTypeEnum.临时收费账单.equalsByCode(bill.getType())){
            detail.setRecBillId(bill.getId());
            detail.setRecBillNo(bill.getBillNo());
        }
        detail.setCostCenterId(bill.getCostCenterId() );
        detail.setCostCenterName(bill.getCostCenterName());
        detail.setChargeItemId(bill.getChargeItemId());
        detail.setChargeItemName(bill.getChargeItemName());
        detail.setSupCpUnitId(StringUtils.isNotBlank(bill.getCommunityId()) ? bill.getCommunityId() : bill.getSupCpUnitId());
        detail.setSupCpUnitName(bill.getCommunityName());
        detail.setCpUnitId(bill.getRoomId());
        detail.setCpUnitName(bill.getRoomName());
        detail.setPayChannel(command.getSettleChannel());
        detail.setPayWay(command.getSettleWay());
        if (TenantUtil.bf64() && PaySourceEnum.业主端app.getCode().equals(gatherBill.getPaySource())){
            detail.setPayWay(0);
            gatherBill.setPayWay(0);
        }
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            detail.setCarriedBillPayChannel(command.getCarriedBillPayChannel());
        }
        detail.setRecPayAmount(bill.getCurrentSettleAmount(command.getSettleAmount()));
        detail.setPayAmount(command.getSettleAmount());
        detail.setPayerType(command.getPayerType());
        detail.setPayerId(command.getPayerId());
        detail.setPayerName(command.getPayerName());
        detail.setPayeeId(command.getPayeeId());
        detail.setPayeeName(command.getPayeeName());
        detail.setPayTime(Objects.nonNull(command.getSettleTime()) ? command.getSettleTime() : LocalDateTime.now());
        detail.setChargeStartTime(bill.getStartTime());
        detail.setChargeEndTime(bill.getEndTime());
        detail.setPayerPhone(command.getPayerPhone());
        detail.setPayeePhone(command.getPayeePhone());
        detail.setRemark(gatherBill.getRemark());
        //处理未收款开票，收款后票据数据同步
        log.info("构建收款单明细实体,账单开票状态：{}", bill.getInvoiceState());
        if(BillInvoiceStateEnum.已开票.getCode().equals(bill.getInvoiceState())){
            detail.setInvoiceState(bill.getInvoiceState());
            detail.setInvoiceAmount(detail.getRecPayAmount());
            log.info("构建收款单明细实体,收款单明细类型:{},收款单明细开票状态：{}", detail.getGatherType(),detail.getInvoiceState());
            InvoiceReceiptDetailRepository repository = Global.ac.getBean(InvoiceReceiptDetailRepository.class);
            InvoiceDetailAndReceiptV invoiceDetail = repository.queryEmptyInvoiceDetail(bill.getSupCpUnitId(),bill.getId());
            if (Objects.nonNull(invoiceDetail)) {
                if(0L == invoiceDetail.getInvoiceAmount()){
                    return detail;
                }
                if (invoiceDetail.getInvoiceAmount() > detail.getInvoiceAmount()) {
                    InvoiceReceiptDetailE receiptDetailE = Global.mapperFacade.map(invoiceDetail, InvoiceReceiptDetailE.class);
                    receiptDetailE.generateNewIdentifier();
                    receiptDetailE.setGatherDetailId(detail.getId());
                    receiptDetailE.setGatherBillId(detail.getGatherBillId());
                    receiptDetailE.setGatherBillNo(detail.getGatherBillNo());
                    invoiceDetail.setInvoiceAmount(invoiceDetail.getInvoiceAmount() - detail.getInvoiceAmount());
                    invoiceDetail.setPriceTaxAmount(invoiceDetail.getInvoiceAmount());
                    receiptDetailE.setInvoiceAmount(detail.getInvoiceAmount());
                    receiptDetailE.setPriceTaxAmount(receiptDetailE.getInvoiceAmount());
                    repository.save(receiptDetailE);
                    repository.updateById(invoiceDetail);
                } else {
                    invoiceDetail.setGatherDetailId(detail.getId());
                    invoiceDetail.setGatherBillId(detail.getGatherBillId());
                    invoiceDetail.setGatherBillNo(detail.getGatherBillNo());
                    repository.updateById(invoiceDetail);
                }
            }
        }
        return detail;
    }
}
