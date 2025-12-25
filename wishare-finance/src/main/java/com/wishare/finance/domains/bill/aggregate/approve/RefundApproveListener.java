package com.wishare.finance.domains.bill.aggregate.approve;

import com.alibaba.fastjson.JSON;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.domains.bill.aggregate.BillRefundA;
import com.wishare.finance.domains.bill.aggregate.PayBillA;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.event.BillRefundMqDetail;
import com.wishare.finance.domains.bill.repository.BillRefundRepository;
import com.wishare.finance.domains.bill.repository.BillRepositoryFactory;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.PayBillRepository;
import com.wishare.finance.domains.bill.repository.PayDetailRepository;
import com.wishare.finance.domains.bill.service.GatherBillDomainService;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 退款审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
@Slf4j
public class RefundApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        //结转资源库
        BillRefundRepository refundRepository = Global.ac.getBean(BillRefundRepository.class);
        BillRefundE billRefundE = refundRepository.getByBillApproveId(billApprove.getId());
        BillRefundA<B> billRefundA = new BillRefundA<>(bill);
        Global.mapperFacade.map(billRefundE, billRefundA);
        boolean refund = billRefundA.refund();
        if (refund) {
            BillRepositoryFactory.getBillRepository(BillTypeEnum.valueOfByCode(bill.getType())).updateById(billRefundA.getBill());
            refundRepository.saveOrUpdate(billRefundA);
            //收款单退款
            gatherBillRefundAmount(bill, billRefundE.getRefundAmount(), billApprove);
            //生成付款单
            savePayBillDetail(bill, billRefundE.getRefundAmount());
            //bpm审核通过回调时，审批人为bpm最后负责人
            //日志记录
            IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_"+bill.getId()), IdentityInfo.class);
            Operator operator = Objects.isNull(identityInfo)?LogContext.getOperator():new Operator(Optional.ofNullable(identityInfo.getTenantId()).orElse("bgm"), Optional.of(identityInfo.getUserName()).orElse("bpm"));
            BizLog.normal(String.valueOf(bill.getId()), operator, LogObject.账单, LogAction.审核通过,
                    new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", false))));
            //发送退款mq
            refundMq(bill, billRefundE.getRefundAmount());
        }
    }

    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        //bpm审核通过回调时，审批人为bpm最后负责人
        //日志记录
        IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_"+bill.getId()), IdentityInfo.class);
        Operator operator = Objects.isNull(identityInfo)?LogContext.getOperator():new Operator(Optional.ofNullable(identityInfo.getTenantId()).orElse("bgm"), Optional.of(identityInfo.getUserName()).orElse("bpm"));
        BizLog.normal(String.valueOf(bill.getId()), operator, LogObject.账单, LogAction.审核拒绝,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", false)))
                        .option(new ContentOption(new PlainTextDataItem("拒绝原因： "+reason, false))));
    }

    /**
     * 收款单退款
     *
     * @param bill
     * @param refundAmount
     */
    private void gatherBillRefundAmount(B bill, Long refundAmount, BillApproveE billApprove) {
        //由于预收单没有收款单,所以无需处理对应的收款单金额
        if (bill instanceof AdvanceBill) {
            return;
        } else {
            List<GatherBill> gatherBills = Global.ac.getBean(GatherBillDomainService.class).refundAmount(bill.getId(), refundAmount, billApprove);
            if (CollectionUtils.isNotEmpty(gatherBills)) {
                GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
                gatherBillRepository.updateBatchById(gatherBills);
            }
        }
    }

    /**
     * 保存付款单和付款明细
     *
     * @param bill
     * @param refundAmount
     */
    private void savePayBillDetail(B bill, Long refundAmount) {
        //插入付款单和付款明细
        PayBillA<B> payBillA = new PayBillA<>(bill);
        payBillA.refund(refundAmount);

        Global.ac.getBean(PayBillRepository.class).save(payBillA.getPayBill());
        Global.ac.getBean(PayDetailRepository.class).save(payBillA.getPayDetail());
    }


    /**
     * 发送退款mq
     * <p>
     * 一笔账单进行退款和结转时，优先去退款/结转未开票的金额。其次若一笔账单开了多笔票，按时间降序顺序进行票据的红冲/作废/回收
     *
     * @param bill         账单
     * @param refundAmount 当前退款金额
     */
    private void refundMq(B bill, Long refundAmount) {
        Long billRefundAmount = bill.getRefundAmount() - refundAmount;//账单退款金额
        Long billInvoiceAmount = bill.getInvoiceAmount();//账单开票金额
        Long billActualPayAmount = bill.getActualPayAmount() + refundAmount;//实收金额
        log.info("发送退款mq：账单id：{},账单已退款金额，{},该次退款金额：{},账单开票金额:{},实收金额{}",
                bill.getId(), bill.getRefundAmount(), refundAmount, billInvoiceAmount, billActualPayAmount);
        if (billActualPayAmount - billInvoiceAmount < refundAmount + billRefundAmount) {
            Long thisRefundAmount = refundAmount - (billActualPayAmount - billInvoiceAmount);
            //发送退款成功mq
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(thisRefundAmount);
            EventLifecycle.push(EventMessage.builder().headers("action", BillAction.REFUND).payload(BillActionEvent.refund(bill.getId(), bill.getType(), billRefundMqDetail, bill.getSupCpUnitId())).build());
        }
    }

}
