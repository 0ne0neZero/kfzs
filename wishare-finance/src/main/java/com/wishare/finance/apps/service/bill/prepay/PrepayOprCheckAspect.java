package com.wishare.finance.apps.service.bill.prepay;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoQueryF;
import com.wishare.finance.domains.bill.consts.enums.BillPayStateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title PrepayOprCheckAspect
 * @date 2023.11.10  12:36
 * @description  账单预支付状态校验切面处理
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class PrepayOprCheckAspect {

    private final BillPrepayInfoAppService billPrepayInfoAppService;

    @Before("@annotation(PrepayOprCheck)")
    public void beforeMyAnnotatedMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        List<Long> billIds = new ArrayList<>();
        Long billId = null;

        // 获取固定参数所需
        if (Objects.nonNull(args[0]) && args[0] instanceof List) {
            billIds = (List<Long>) args[0];
        } else if (Objects.nonNull(args[0]) && args[0] instanceof Long) {
            billId = (Long) args[0];
        }
        String supCpUnitId = Objects.nonNull(args[1]) ? (String) args[1] : null;
        log.info("获取了参数：sup={},billIds={},billId={}", supCpUnitId, JSON.toJSONString(billIds), billId);

        // 账单支付校验
        if (supCpUnitId != null && (billId != null || billIds.size() != 0)) {
            billPrepayInfoAppService.checkPrepayment(new BillPrepayInfoQueryF()
                    .setSupCpUnitId(supCpUnitId)
                    .setPayState(BillPayStateEnum.支付中.getCode())
                    .setBillIds(CollectionUtils.isEmpty(billIds) ? List.of(billId) : billIds));
        } else {
            log.info("supCpUnitId或(billIds和billId)为空，跳过支付校验");
        }
    }
}
