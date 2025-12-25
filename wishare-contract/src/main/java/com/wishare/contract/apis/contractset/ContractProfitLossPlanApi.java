package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.ContractConcludeUpdateF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanUpdateF;
import com.wishare.contract.apps.service.contractset.ContractProfitLossPlanAppService;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanV;
import com.wishare.contract.domains.vo.contractset.ContractConcludeSumV;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossPlanV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangrui
 * @since 2022-09-29
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同损益计划信息"})
@RequestMapping("/contractProfitLossPlan")
public class ContractProfitLossPlanApi implements IOwlApiBase {
    private final ContractProfitLossPlanAppService contractProfitLossPlanAppService;

    @ApiOperation(value = "损益列表列表", response = ContractCollectionPlanV.class)
    @PostMapping("/list")
    public List<ContractProfitLossPlanV> listProfitLossPlan(@RequestBody ContractProfitLossPlanF profitLossPlanF) {
        return contractProfitLossPlanAppService.listProfitLossPlan(profitLossPlanF);
    }

    @ApiOperation(value = "删除损益计划", notes = "删除损益计划")
    @DeleteMapping("/remove")
    public void remove(@RequestParam("id") Long id) {
        contractProfitLossPlanAppService.remove(id);
    }

    @ApiOperation(value = "更新损益信息", notes = "更新损益信息")
    @PutMapping("/update")
    public String updateProfitLossPlan(@Validated @RequestBody ContractProfitLossPlanUpdateF profitLossPlanUpdateF) {
        profitLossPlanUpdateF.setTenantId(tenantId());
        contractProfitLossPlanAppService.updateProfitLossPlan(profitLossPlanUpdateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "新增损益信息", notes = "新增损益信息")
    @PostMapping("/save")
    public String saveProfitLossPlan(@Validated @RequestBody ContractProfitLossPlanSaveF profitLossPlanSaveF) {
        profitLossPlanSaveF.setTenantId(tenantId());
        contractProfitLossPlanAppService.saveProfitLossPlan(profitLossPlanSaveF);
        return PromptInfo.OK.info;
    }
}
