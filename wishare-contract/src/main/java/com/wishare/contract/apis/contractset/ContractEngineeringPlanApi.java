package com.wishare.contract.apis.contractset;

import com.wishare.owl.enhance.IOwlApiBase;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractEngineeringPlanAppService;
import com.wishare.contract.domains.vo.contractset.ContractEngineeringPlanV;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanF;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanSaveF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.text.ParseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 工程类合同计提信息表
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"工程类合同计提信息表"})
@RequestMapping("/contractEngineeringPlan")
public class ContractEngineeringPlanApi implements IOwlApiBase {

    private final ContractEngineeringPlanAppService contractEngineeringPlanAppService;

    @ApiOperation(value = "查询工程计提详情", notes = "查询工程计提详情", response = ContractEngineeringPlanV.class)
    @PostMapping("/getById")
    public ContractEngineeringPlanV get(@RequestBody ContractEngineeringPlanF contractEngineeringPlanF){
        return contractEngineeringPlanAppService.get(contractEngineeringPlanF);
    }

    @ApiOperation(value = "工程计划列表", notes = "工程计划列表", response = ContractEngineeringPlanV.class)
    @PostMapping("/list")
    public List<ContractEngineeringPlanV> list(@RequestBody ContractEngineeringPlanF contractEngineeringPlanF){
        return contractEngineeringPlanAppService.list(contractEngineeringPlanF);
    }

    @ApiOperation(value = "新增工程计提", notes = "新增工程计提")
    @PostMapping("/save")
    public Long save(@Validated @RequestBody ContractEngineeringPlanSaveF contractEngineeringPlanF) throws ParseException {
        contractEngineeringPlanF.setTenantId(tenantId());
        return contractEngineeringPlanAppService.save(contractEngineeringPlanF);
    }
}
