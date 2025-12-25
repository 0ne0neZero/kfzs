package com.wishare.contract.apis.contractset;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractBondCarryoverDetailAppService;
import com.wishare.contract.domains.vo.contractset.ContractBondCarryoverDetailV;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 保证金结转明细表
 * </p>
 *
 * @author ljx
 * @since 2022-11-21
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金结转明细表"})
@RequestMapping("/contractBondCarryoverDetail")
public class ContractBondCarryoverDetailApi {

    private final ContractBondCarryoverDetailAppService contractBondCarryoverDetailAppService;

    @ApiOperation("结转明细列表")
    @GetMapping("/listByBondPlanId")
    public List<ContractBondCarryoverDetailV> listByBondPlanId(@RequestParam Long bondPlanId) {
        return contractBondCarryoverDetailAppService.listByBondPlanId(bondPlanId);
    }
}
