package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.remote.vo.charge.ApproveFilter;
import com.wishare.starter.annotations.OpenFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hhb
 * @describe
 * @date 2025/10/28 18:18
 */
@OpenFeignClient(name = "wishare-charge", serverName = "charge服务", path = "/charge")
public interface ChargeClient {

    @GetMapping(value = "/approve/operatePassageRuleConfig", name = "获取审批规则")
    ApproveFilter getApprovePushBillFilter(@RequestParam("supCpUnitId") String supCpUnitId, @RequestParam("operateType") Integer operateType);

}
