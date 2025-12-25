package com.wishare.finance.apps.event.receipt;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.reconciliation.fo.ListUserInfoF;
import com.wishare.finance.apps.model.reconciliation.fo.PhoneParamF;
import com.wishare.finance.apps.model.reconciliation.vo.PhoneThirdPartyIdV;
import com.wishare.finance.apps.model.reconciliation.vo.UserMobileV;
import com.wishare.finance.apps.pushbill.fo.FundReceiptsBillZJF;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FundReceiptsBillPublisher implements ApplicationEventPublisherAware, IOwlApiBase {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ExternalClient externalClient;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 生成报账单事件发布
     *
     * @param fundReceiptsBillZJF
     */
    public void publishEvent(FundReceiptsBillZJF fundReceiptsBillZJF) {
        log.info("生成报账单publishEvent入口:{},{}", fundReceiptsBillZJF.getIds(), fundReceiptsBillZJF.getSupCpUnitIds());
        String userId = userId();
        //先判断是否具有4A账号
        if (!hasThirdPartyId(userId)) {
            log.error("当前userId:{}没有4A账号", userId);
            throw BizException.throw400("当前账号非4A账号，请申请交建通账号后操作");
        }

        List<Long> ids = fundReceiptsBillZJF.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        CountDownLatch latch = new CountDownLatch(ids.size());
        for (Long id : ids) {
            List<Long> singleIdList = Lists.newArrayList(id);
            FundReceiptsBillEvent event = new FundReceiptsBillEvent(this, singleIdList, fundReceiptsBillZJF.getSupCpUnitIds(), latch);
            applicationEventPublisher.publishEvent(event);
            //[根据流水认领记录生成资金收款单]BillRuleDomainService.executeZjFundReceiptsBill
        }

        try {
            if (!latch.await(30, TimeUnit.MINUTES)) {
                log.error("生成报账单latch.await返回false");
            }
        } catch (Exception e) {
            log.error("生成报账单latch.await异常,msg:{}", e.getMessage(), e);
            throw new BizException(500, "生成报账单异常");
        }
    }

    private boolean hasThirdPartyId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        //查询手机号
        String phone = queryUserMobile(userId);
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        //根据手机号查三方id
        List<String> phones = Lists.newArrayList(phone);
        PhoneParamF phoneParamF = new PhoneParamF();
        phoneParamF.setPhones(phones);
        List<PhoneThirdPartyIdV> infos;
        try {
            infos = externalClient.getUserThirdPartyIdByPhone(phoneParamF);
        } catch (Exception e) {
            log.error("根据手机号查询用户三方id失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "根据手机号查询用户三方id失败", e);
        }
        return !CollectionUtils.isEmpty(infos) && !StringUtils.isBlank(infos.get(0).getThirdPartyId());
    }

    /**
     * 查询员工手机号
     *
     * @param userId
     * @return
     */
    private String queryUserMobile(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        List<String> userIds = Lists.newArrayList(userId);
        //查用户手机号
        ListUserInfoF listUserInfoF = new ListUserInfoF();
        listUserInfoF.setUserIds(userIds);
        listUserInfoF.setGatewaySymbol("saas");
        List<UserMobileV> userMobileVS;
        try {
            log.info("listUserInfoBy入参:{}", JSON.toJSONString(listUserInfoF));
            userMobileVS = userClient.listUserInfoBy(listUserInfoF);
            log.info("listUserInfoBy出参:{}", JSON.toJSONString(userMobileVS));
        } catch (Exception e) {
            log.error("listUserInfoBy获取员工手机号失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "listUserInfoBy获取员工手机号失败", e);
        }
        if (CollectionUtils.isEmpty(userMobileVS)) {
            return null;
        }
        return userMobileVS.get(0).getMobileNum();
    }

}
