package com.wishare.finance.domains.bill.aggregate;

import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.PayTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.PayBill;
import com.wishare.finance.domains.bill.entity.PayDetail;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/12/28
 * @Description: 付款单聚合
 */
@Getter
@Setter
public class PayBillA <B extends Bill> {

    /**
     * 账单信息
     */
    private B bill;

    /**
     * 付款单
     */
    private PayBill payBill;

    /**
     * 付款明细
     */
    private PayDetail payDetail;


    public PayBillA(B bill)  {
        this.bill = bill;
    }

    /**
     * 退款构建付款单和付款明细
     *
     * @param refundAmount
     */
    public void refund(Long refundAmount) {
        payBill = generalPayBill(refundAmount, PayTypeEnum.退款付款.getCode());
        generalPayDetail(payBill);
    }

    /**
     * 构建付款单明细
     */
    private PayDetail generalPayDetail(PayBill payBill) {
        payDetail = new PayDetail();
        payDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.PAY_DETAIL));
        payDetail.setPayBillId(payBill.getId());
        payDetail.setPayBillNo(payBill.getBillNo());
        payDetail.setPayableBillId(bill.getId());
        payDetail.setCostCenterId(bill.getCostCenterId());
        payDetail.setCostCenterName(bill.getCostCenterName());
        payDetail.setChargeItemId(bill.getChargeItemId());
        payDetail.setChargeItemName(bill.getChargeItemName());
        payDetail.setPayChannel(SettleChannelEnum.其他.getCode());
        payDetail.setPayWay(SettleWayEnum.线上.getCode());
        payDetail.setRecPayAmount(payBill.getTotalAmount());
        payDetail.setPayAmount(payBill.getTotalAmount());
        payDetail.setPayerType(bill.getPayerType());
        payDetail.setPayerId(bill.getPayeeId());
        payDetail.setPayerName(bill.getPayeeName());
        payDetail.setPayeeId(bill.getPayerId());
        payDetail.setPayeeName(bill.getPayerName());
        payDetail.setPayTime(LocalDateTime.now());

        payDetail.setSupCpUnitId(bill.getCommunityId());
        payDetail.setSupCpUnitName(bill.getCommunityName());
        payDetail.setCpUnitId(bill.getRoomId());
        payDetail.setCpUnitName(bill.getRoomName());
        return payDetail;
    }

    /**
     * 构建付款单
     */
    private PayBill generalPayBill(Long refundAmount, Integer payType) {
        payBill = Global.mapperFacade.map(bill, PayBill.class);
        payBill.setId(null);
        payBill.setBillNo(null);
        payBill.resetState();
        payBill.setPayType(payType);
        payBill.generateIdentifier();
        payBill.setRefundState(0);
        payBill.setDiscountAmount(0L);
        payBill.setRefundAmount(0L);
        payBill.setCarriedAmount(0L);
        payBill.generateIdentifier();
        payBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        payBill.setPayTime(LocalDateTime.now());
        payBill.setPayeeId(bill.getPayerId());
        payBill.setPayeeName(bill.getPayerName());
        payBill.setPayerId(bill.getPayeeId());
        payBill.setPayerName(bill.getPayeeName());
        payBill.setDescription("退款");
        payBill.setTotalAmount(refundAmount);
        payBill.setPayChannel(SettleChannelEnum.其他.getCode());
        payBill.setPayWay(SettleWayEnum.线上.getCode());
        return payBill;
    }


}
