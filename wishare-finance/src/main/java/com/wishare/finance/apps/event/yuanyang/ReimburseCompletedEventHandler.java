package com.wishare.finance.apps.event.yuanyang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wishare.finance.apps.model.yuanyang.vo.BusinessNotifyV;
import com.wishare.finance.apps.model.yuanyang.vo.ReimbursementNotifyV;
import com.wishare.finance.apps.model.yuanyang.vo.ReimbursementVoucherV;
import com.wishare.finance.domains.bill.entity.TransactionOrder;
import com.wishare.finance.domains.bill.event.ReimburseCompletedEvent;
import com.wishare.finance.domains.bill.event.ReimburseNotifyEvent;
import com.wishare.finance.domains.voucher.entity.BusinessBill;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.service.BusinessBillDomainService;
import com.wishare.finance.infrastructure.event.DelayEventMessage;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import org.apache.commons.collections4.CollectionUtils;
import com.wishare.finance.infrastructure.notify.YYNotification;
import com.wishare.finance.infrastructure.utils.WebUtil;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 报销处理完成事件处理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Slf4j
@Component
public class ReimburseCompletedEventHandler implements ApplicationListener<ReimburseCompletedEvent> {

    @Autowired
    private BusinessBillDomainService  businessBillDomainService;

    @Override
    public void onApplicationEvent(ReimburseCompletedEvent event) {
        log.info("报销处理完成事件-参数：" + JSONObject.toJSONString(event));
        //通知远洋已完成处理
        TransactionOrder transactionOrder = event.getTransactionOrder();
        List<TransactionOrder> transactionOrders = event.getTransactionOrders();
        String notifyUrl = "";
        String data = "";
        ThreadLocalUtil.set("IdentityInfo", event.getIdentityInfo()); //添加租户信息
        if (Objects.nonNull(transactionOrder)){
            notifyUrl = transactionOrder.getNotifyUrl();
            if (StringUtils.isBlank(notifyUrl)){
                return;
            }
            ReimbursementNotifyV reimbursementNotifyV = Global.mapperFacade.map(event.getTransactionOrder(), ReimbursementNotifyV.class);
            reimbursementNotifyV.setVoucher(Global.mapperFacade.map(event.getVoucher(), ReimbursementVoucherV.class));
            log.info("reimbursementNotifyV:{}", JSON.toJSONString(reimbursementNotifyV));
            YYNotification notification = YYNotification.create("101", reimbursementNotifyV, "payee", "payer", "voucher", "vouchers", "businessId", "billTypeCode", "voucherIds");
            data = JSONObject.toJSONString(notification);
        } else if (CollectionUtils.isNotEmpty(transactionOrders)) {
            notifyUrl = transactionOrders.get(0).getNotifyUrl();
            if (StringUtils.isBlank(notifyUrl)){
                return;
            }
            List<BusinessNotifyV> businessNotifyVS = Global.mapperFacade.mapAsList(event.getTransactionOrders(), BusinessNotifyV.class);
            List<ReimbursementVoucherV> vouchers = Global.mapperFacade.mapAsList(event.getVouchers(), ReimbursementVoucherV.class);
            Map<Long, ReimbursementVoucherV> voucherMap = vouchers.stream().collect(Collectors.toMap(ReimbursementVoucherV::getId, voucher -> voucher));
            businessNotifyVS.forEach(notify -> {
                List<ReimbursementVoucherV> notifyVouchers = new ArrayList<>();
                List<Long> voucherIds = notify.getVoucherIds();
                if (CollectionUtils.isNotEmpty(voucherIds)) {
                    voucherIds.forEach(id -> notifyVouchers.add(voucherMap.get(id)));
                }
                notify.setVouchers(notifyVouchers);
                notify.setBillTypeCode(event.getBillTypeCode());
                notify.setBusinessId(event.getBusinessId());
            });
            log.info("businessNotifyVS:{}", JSON.toJSONString(businessNotifyVS, SerializerFeature.DisableCircularReferenceDetect));
            YYNotification notification = YYNotification.create("101", businessNotifyVS, "payee", "payer", "voucher", "vouchers", "businessId", "billTypeCode", "voucherIds");
            data = JSONObject.toJSONString(notification, SerializerFeature.DisableCircularReferenceDetect);
        } else {
            // 没有支付的情况
            BusinessBill businessBill = businessBillDomainService.getByBusinessId(event.getBusinessId());
            notifyUrl = businessBill.getNotifyUrl();
            if (StringUtils.isBlank(notifyUrl)){
                return;
            }
            BusinessNotifyV businessNotify = new BusinessNotifyV();
            List<ReimbursementVoucherV> vouchers = Global.mapperFacade.mapAsList(event.getVouchers(), ReimbursementVoucherV.class);
            businessNotify.setVouchers(vouchers);
            businessNotify.setBillTypeCode(event.getBillTypeCode());
            businessNotify.setBusinessId(event.getBusinessId());
            log.info("businessNotify:{}", JSON.toJSONString(businessNotify, SerializerFeature.DisableCircularReferenceDetect));
            YYNotification notification = YYNotification.create("101", businessNotify, "payee", "payer", "voucher", "vouchers", "businessId", "billTypeCode", "voucherIds");
            data = JSONObject.toJSONString(notification, SerializerFeature.DisableCircularReferenceDetect);
        }
        EventLifecycle.apply(new DelayEventMessage.DelayEventMessageBuilder<ReimburseNotifyEvent>()
                .delay(1)
                .payload(new ReimburseNotifyEvent().setNotifyCount(1).setNotifyData(data).setNotifyUrl(notifyUrl).setIdentityInfo(event.getIdentityInfo()))
                .headers("notifyType", "TRANSACTION")
                .build());
    }
}
