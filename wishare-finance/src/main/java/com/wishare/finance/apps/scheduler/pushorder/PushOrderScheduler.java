package com.wishare.finance.apps.scheduler.pushorder;


import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushOrderScheduler {
    private static final String PUSH_ORDER_STATE = "PUSH_ORDER_STATE";
    private final PushBillZJDomainService pushBillZJDomainService;
    /**
     * 查询财务云推单是否成功
     */
    @XxlJob("pushOrderStateScheduler")
    private void pushOrderStateScheduler() {
        log.info("中交财务云查询推单状态");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        try {
            RedisHelper.set(PUSH_ORDER_STATE, "BILL_STATE");
            OrderStatusBody orderStatusBody=new OrderStatusBody();
            pushBillZJDomainService.queryFinanceOrderDealResult(orderStatusBody);
        }catch (Exception e){
            log.error("查询中交推单账单失败 错误信息 :{}",e.getMessage());
        } finally {
            log.info("删除redis key");
        }
    }

    @XxlJob("pushOrderStateSchedulerDx")
    private void pushOrderStateSchedulerDx() {
        log.info("中交财务云查询推单状态-新");
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        try {
            RedisHelper.set(PUSH_ORDER_STATE, "BILL_STATE");
            OrderStatusBody orderStatusBody=new OrderStatusBody();
            pushBillZJDomainService.queryFinanceOrderDealResultOnDx(orderStatusBody);
        }catch (Exception e){
            log.error("查询中交推单账单失败 错误信息 :{}",e.getMessage());
        } finally {
            log.info("删除redis key");
        }
    }

}
