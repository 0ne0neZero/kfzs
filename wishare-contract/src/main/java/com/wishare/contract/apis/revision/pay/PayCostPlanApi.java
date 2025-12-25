package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanPageV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author longhuadmin
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"成本计划"})
@RequestMapping("/payCostPlan")
public class PayCostPlanApi {

    @Autowired
    private ContractPayCostPlanService payCostPlanService;

    @ApiOperation(value = "成本计划分页列表")
    @PostMapping("/pageFront")
    public PageV<PayCostPlanPageV> pageFront(@RequestBody PageF<SearchF<?>> request) {
        return payCostPlanService.pageFront(request);
    }
}
