package com.wishare.finance.apps.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.event.*;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceReceiptDomainService;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author xujian
 * @date 2022/11/8
 * @Description: 作废
 */
@Slf4j
@Component
public class InvoiceVoucherEventHandler implements Consumer<BillActionEvent> {

    @Autowired
    private BillFacade billFacade;

    @Autowired
    private InvoiceReceiptDomainService invoiceReceiptDomainService;

    @Override
    public void accept(BillActionEvent billActionEvent) {
        log.info("发票账单事件MQ消息监听：{}", JSONObject.toJSONString(billActionEvent));
        String messageKey = "bill_invoice" + billActionEvent.getUniqueMessageKey();
        if (!RedisHelper.setNotExists(messageKey, "1")) {
//            log.info("发票账单事件MQ消息监听-事件已消费，账单id：{}", billActionEvent.getBillId());
            return;
        }
        RedisHelper.expire(messageKey, 60*60L);

        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(billActionEvent.getTenantId());
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

//        log.info("账单id：{},账单类型 ：{}", billActionEvent.getBillId(), billActionEvent.getBillType());
        switch (billActionEvent.getAction()) {
            case REVERSED:
                reversedHandle(billActionEvent.getBillId(), billActionEvent.getBillType(), billActionEvent.getSupCpUnitId());
                break;
            case INVALIDED:
                invalidedHandle(billActionEvent.getBillId());
                break;
            case REFUND:
                refundHandle(billActionEvent.getBillId(), billActionEvent.getDetail());
                break;
            case ADJUSTED:
                adjustedHandle(billActionEvent.getBillId(), billActionEvent.getDetail(), billActionEvent.getSupCpUnitId());
                break;
            case DEDUCTION:
                reductionHandle(billActionEvent.getBillId(), billActionEvent.getDetail());
                break;
            case GATHER_BILL_REVERSED:
                log.info("收款单冲销处理");
                gatherBillReversedHandle(billActionEvent.getGatherBillId(), billActionEvent.getSupCpUnitId());
                break;
        }
    }

    /**
     * 监听处理减免账单
     *
     * @param billId
     * @param detail
     */
    private void reductionHandle(Long billId, Object detail) {
        try {
            BillReductionMqDetail billAdjustMqDetail = JSONObject.parseObject(JSON.toJSONString(detail), BillReductionMqDetail.class);
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(billAdjustMqDetail.getRefundAmount());
            invoiceReceiptDomainService.invalidedInvoiceReceiptByRefund(billId, billRefundMqDetail);
        } catch (Exception e) {
            log.error("减免账单异常:", e);
        }
    }

    /**
     * 监听处理调整账单
     *
     * @param billId
     * @param detail
     */
    private void adjustedHandle(Long billId, Object detail, String supCpUnitId) {
        try {
            BillAdjustMqDetail billAdjustMqDetail = JSONObject.parseObject(JSON.toJSONString(detail), BillAdjustMqDetail.class);
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(billAdjustMqDetail.getRedAmount());
            if (billRefundMqDetail.getRefundAmount() > 0) {
                invoiceReceiptDomainService.invalidedInvoiceReceiptByRefund(billId, billRefundMqDetail);
            } else {
                //  重新计算设置开票状态
                billFacade.reSetBillInvoiceState(billId, BillTypeEnum.valueOfByCode(billAdjustMqDetail.getBillType()), supCpUnitId);
            }
        } catch (Exception e) {
            log.error("调整账单异常:", e);
        }
    }

    /**
     * 监听处理退款账单
     *
     * @param billId
     * @param detail
     */
    private void refundHandle(Long billId, Object detail) {
        try {
            BillRefundMqDetail billRefundMqDetail = JSONObject.parseObject(JSON.toJSONString(detail), BillRefundMqDetail.class);
            if (Objects.nonNull(billRefundMqDetail.getRefundAmount()) && billRefundMqDetail.getRefundAmount() > 0) {
                invoiceReceiptDomainService.invalidedInvoiceReceiptByRefund(billId, billRefundMqDetail);
            }
        } catch (Exception e) {
            log.error("退款账单异常:", e);
        }
    }

    /**
     * 监听处理作废账单
     *
     * @param billId
     */
    private void invalidedHandle(Long billId) {
        try {
            invoiceReceiptDomainService.invalidedInvoiceReceipt(billId);
        } catch (Exception e) {
            log.error("作废账单异常:", e);
        }
    }

    /**
     * 监听处理冲销
     *
     * @param billId
     * @param billType
     */
    private void reversedHandle(Long billId, Integer billType, String supCpUnitId) {
        try {
            invoiceReceiptDomainService.invalidedInvoiceReceipt(billId);
        } catch (Exception e) {
            log.error("冲销异常异常:", e);
            billFacade.robackReverse(billId, BillTypeEnum.valueOfByCode(billType), supCpUnitId);
        }
    }

    /**
     * 监听处理收款单冲销
     *
     * @param gatherBillId
     * @param supCpUnitId
     */
    private void gatherBillReversedHandle(Long gatherBillId, String supCpUnitId) {
        try {
            invoiceReceiptDomainService.invalidInvoiceReceiptByGatherBill(gatherBillId, supCpUnitId);
        } catch (Exception e) {
            log.error("收款单冲销异常异常，收款单id：" + gatherBillId, e);
        }
    }
}
