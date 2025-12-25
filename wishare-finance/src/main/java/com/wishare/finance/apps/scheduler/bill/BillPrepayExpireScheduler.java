package com.wishare.finance.apps.scheduler.bill;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoUpdateF;
import com.wishare.finance.apps.service.bill.prepay.BillPrepayInfoAppService;
import com.wishare.finance.domains.bill.consts.enums.BillPayStateEnum;
import com.wishare.finance.domains.bill.entity.BillPrepayInfoE;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.clients.base.PayClient;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailV;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yyx
 * @project wishare-finance
 * @title BillCarryoverDetailScheduler
 * @date 2023.09.23  09:20
 * @description 账单预支付过期处理定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillPrepayExpireScheduler {

    private final BillPrepayInfoAppService billPrepayInfoAppService;

    private final PayClient payClient;

    private final ChargeClient chargeClient;

    // 每次查询数量
    private static final int PAGE_SIZE = 500;

    @XxlJob("billPrepayInfoSchedulerHandler")
    public void billPrepayExpireScheduler() {
        // 获取当前时间节点
        LocalDateTime localDate = LocalDateTime.now();
        String jobId = String.valueOf(XxlJobHelper.getJobId());
        // 参数封装
        Boolean isNowOpr = StringUtils.isBlank(XxlJobHelper.getJobParam())?Boolean.TRUE: Boolean.valueOf(XxlJobHelper.getJobParam());
        int currentPage = 1;
        PageF<SearchF<?>> queryF = PageF.of(currentPage,
                PAGE_SIZE, new SearchF<>());
        queryF.getConditions().setFields(new ArrayList<>());

        // 执行不同环境回调处理
        if (TenantUtil.bf10()){
            fyEnvPrepay(localDate,queryF);
        }else {
            otherEnvPrepay(localDate, isNowOpr, queryF);
        }

    }

    /**
     * 方圆环境过期处理
     * @param localDate 当前日期
     * @param queryF    查询条件
     */
    private void fyEnvPrepay(LocalDateTime localDate,PageF<SearchF<?>> queryF) {
        // 查询支付中的账单信息
        PageV<BillPrepayInfoE> pageV = billPrepayInfoAppService.getPrepayListPage(queryF);

        // 对不同租户项目的数据处理
        Map<String, List<BillPrepayInfoE>> map = pageV.getRecords().stream().collect
                (Collectors.groupingBy(BillPrepayInfoE::getTenantId));
        for (Map.Entry<String, List<BillPrepayInfoE>> tenantEntry : map.entrySet()) {
            List<BillPrepayInfoE> list = tenantEntry.getValue();
            // 对每组商户号的账单处理
            try {
                groupMchNoHand(localDate, tenantEntry, list);
            }catch (Exception e){
                log.error("商户号"+tenantEntry.getKey()+"回调异常",e);
            }
        }

    }

    /**
     * 处理每组商户号对应账单
     */
    private void groupMchNoHand(LocalDateTime localDate,
            Entry<String, List<BillPrepayInfoE>> tenantEntry, List<BillPrepayInfoE> list) {
        Map<String, List<BillPrepayInfoE>> mchNoMap = list.stream()
                .collect(Collectors.groupingBy(BillPrepayInfoE::getMchOrderNo));
        for (Entry<String, List<BillPrepayInfoE>> mchNoEntry : mchNoMap.entrySet()) {
            List<BillPrepayInfoE> infoList = mchNoEntry.getValue();
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setTenantId(tenantEntry.getKey());
            ThreadLocalUtil.set("IdentityInfo", identityInfo);
            PaymentOrderDetailV orderDetailV = payClient.getDetailByMchOrderNo(
                    mchNoEntry.getKey());
            if (Objects.nonNull(orderDetailV) && orderDetailV.getState().equals(2)){
                // 支付成功进行回调处理
                chargeClient.callBack(orderDetailV, tenantEntry.getKey());
            }else {
                // 未成功判断是否过期 则过期处理
                if (localDate.isAfter(infoList.get(0).getExpireTime())){
                    updatePayOverTimeOpr(list);
                }
            }
        }
    }

    /**
     * 非方圆环境过期处理
     * @param localDate 当前日期
     * @param isNowOpr  是否只处理过期
     * @param queryF    查询条件
     */
    private void otherEnvPrepay(LocalDateTime localDate, Boolean isNowOpr, PageF<SearchF<?>> queryF) {
        if (isNowOpr){
            // 获取过期时间到当下时间节点的数据
            queryF.getConditions().getFields().add(new Field("b.expire_time", localDate, 6));
        }
        // 查询要过期的账单信息
        PageV<BillPrepayInfoE> pageV = billPrepayInfoAppService.getPrepayListPage(queryF);
        while(pageV.getRecords().size() > 0) {
            Map<String, List<BillPrepayInfoE>> map = pageV.getRecords().stream().collect(Collectors.groupingBy(BillPrepayInfoE::getTenantId));
            map.forEach((x,y)->{
                IdentityInfo identityInfo = new IdentityInfo();
                identityInfo.setTenantId(x);
                ThreadLocalUtil.set("IdentityInfo", identityInfo);
                updatePayOverTimeOpr(y);
            });
            pageV = billPrepayInfoAppService.getPrepayListPage(queryF);
        }
    }

    /**
     * 过期处理
     */
    private void updatePayOverTimeOpr(List<BillPrepayInfoE> list) {
        // 更新过期后预支付信息状态
        List<Long> billIds = list.stream().map(BillPrepayInfoE::getBillId).collect(Collectors.toList());
        BillPrepayInfoUpdateF billParam = new BillPrepayInfoUpdateF();
        billParam.setPayState(BillPayStateEnum.支付超时.getCode());
        billParam.setBillIds(billIds);
        billParam.setSupCpUnitId(list.get(0).getSupCpUnitId());
        try {
            billPrepayInfoAppService.releasePaymentOpr(billParam);
        }catch (Exception e){
            log.error("定时任务更新预支付错误【{}】,{}",e,e.getMessage());
        }
    }
}
