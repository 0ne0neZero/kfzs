package com.wishare.finance.apps.scheduler.pushorder;

import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.financialnumber.FinancialDocumentNumberFillService;
import com.wishare.finance.infrastructure.remote.fo.zj.OrderStatusBody;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author longhuadmin
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FinancialNumberFillScheduler {

    private final FinancialDocumentNumberFillService financialDocumentNumberFillService;

    @XxlJob("financialNumberInfoFillScheduler")
    private void financialNumberInfoFillScheduler() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId("13554968497211");
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        financialDocumentNumberFillService.fillFinancialDocumentNumber();
    }

}
