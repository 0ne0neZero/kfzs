package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.ContractConcludeF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeSaveF;
import com.wishare.contract.apps.fo.contractset.ContractConcludeUpdateF;
import com.wishare.contract.apps.fo.contractset.OrgInfoTreeF;
import com.wishare.contract.apps.remote.vo.TemporaryChargeBillPageV;
import com.wishare.contract.apps.service.contractset.ContractConcludeAppService;
import com.wishare.contract.domains.entity.contractset.ContractConcludeE;
import com.wishare.contract.domains.vo.contractset.*;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
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
 * 合同订立信息
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同订立信息"})
@RequestMapping("/contract/conclude")
public class ContractConcludeApi implements IOwlApiBase {

    private final ContractConcludeAppService contractConcludeAppService;

    @ApiOperation(value = "根据id查询详情", notes = "根据id查询详情", response = ContractConcludeV.class)
    @GetMapping("/getById")
    public ContractDetailsV getContractConclude(@RequestParam("id") Long id) {
        return contractConcludeAppService.getContractConclude(id);
    }

    @ApiOperation(value = "合同订立列表", notes = "合同订立列表", response = ContractConcludeV.class)
    @PostMapping("/list")
    public List<ConcludeInfoV> listContractConclude(@RequestBody ContractConcludeF contractConcludeF) {
        contractConcludeF.setTenantId(tenantId());
        return contractConcludeAppService.listContractConclude(contractConcludeF);
    }

    @ApiOperation(value = "新增合同", notes = "新增合同")
    @PostMapping("/save")
    public Long saveContractConclude(@Validated @RequestBody ContractConcludeSaveF contractConcludeF) {
        contractConcludeF.setTenantId(tenantId());
        return contractConcludeAppService.saveContractConclude(contractConcludeF);
    }

    @ApiOperation(value = "更新合同信息", notes = "更新合同信息")
    @PutMapping("/update")
    public String updateContractConclude(@Validated @RequestBody ContractConcludeUpdateF contractConcludeF) {
        contractConcludeF.setTenantId(tenantId());
        contractConcludeAppService.updateContractConclude(contractConcludeF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除合同", notes = "删除合同")
    @DeleteMapping("/remove")
    public void removeContractConclude(@RequestParam("id") Long id) {
        contractConcludeAppService.removeContractConclude(id);
    }

    @ApiOperation(value = "合同列表（分页）", response = ContractConcludeV.class)
    @PostMapping("/page")
    public PageV<ContractConcludeV> pageContractConcludePage(@RequestBody PageF<SearchF<ContractConcludeE>> request) {
        String tenantId = tenantId();
        return contractConcludeAppService.pageContractConcludePage(request, tenantId);
    }

    @ApiOperation(value = "合同台账列表（分页）", response = ContractConcludeV.class)
    @PostMapping("/pageList")
    public PageV<ContractInfoV> contractConcludePage(@RequestBody PageF<SearchF<ContractConcludeE>> request) {
        String tenantId = tenantId();
        return contractConcludeAppService.pageContractPage(request, tenantId);
    }

    @ApiOperation(value = "合同台账金额统计", response = ContractAccountSumV.class)
    @PostMapping("/accountAmountSum")
    public ContractAccountSumV accountAmountSum(@RequestBody PageF<SearchF<ContractConcludeE>> request) {
        String tenantId = tenantId();
        return contractConcludeAppService.accountAmountSum(request, tenantId);
    }

    @ApiOperation(value = "合同金额统计", response = ContractConcludeSumV.class)
    @PostMapping("/amountSum")
    public ContractConcludeSumV amountSum(@RequestBody PageF<SearchF<ContractConcludeE>> request) {
        String tenantId = tenantId();
        return contractConcludeAppService.amountSum(request, tenantId);
    }

    @ApiOperation(value = "变更合同状态", notes = "变更合同状态")
    @PutMapping("/contractState")
    public String contractState(@RequestParam("id") Long id, @RequestParam("contractState") Integer contractState, @RequestParam("reviewStatus") Integer reviewStatus) throws ParseException {
        contractConcludeAppService.contractState(id, contractState, reviewStatus);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "提交合同审核", notes = "提交合同审批")
    @PutMapping("/contractApprove")
    public String contractApprove(@RequestParam("id") Long id){
        contractConcludeAppService.contractApprove(id);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "强制终止合同", notes = "强制终止合同")
    @PutMapping("/stopContract")
    public String stopContract(@RequestParam("id") Long id, @RequestParam("reason") String reason) {
        contractConcludeAppService.stopContract(id, reason);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "新增补充/终止/续签合同状态校验", notes = "进行补充/终止/续签合同状态校验")
    @GetMapping("/checkContract")
    public Long checkContract(@RequestParam("id") Long id, @RequestParam(value = "signingMethod", required = false) Integer signingMethod) {
        return contractConcludeAppService.checkContract(id, signingMethod);
    }

    @ApiOperation(value = "合同编码唯一性校验", notes = "合同编码唯一性校验")
    @PostMapping("/checkContractNo")
    public Boolean checkContractNo(@RequestBody ContractConcludeF contractConcludeF) {
        return contractConcludeAppService.checkContractNo(contractConcludeF);
    }

    @ApiOperation(value = "工作台合同预警统计", notes = "工作台合同预警统计", response = ContractStatisticsV.class)
    @GetMapping("/contractStatistics")
    public ContractStatisticsV contractStatistics() {
        return contractConcludeAppService.contractStatistics(tenantId());
    }

    @ApiOperation(value = "招投标保证金分页查询", notes = "招投标保证金分页查询", response = TemporaryChargeBillPageV.class)
    @PostMapping("/temporary/queryPage")
    public PageV<TemporaryChargeBillPageV> temporaryQueryPage(@RequestBody PageF<SearchF<?>> queryF) {
        return contractConcludeAppService.temporaryQueryPage(queryF);
    }

    @PostMapping("/orgInfo/listTree")
    public List<OrgInfoTreeV> temporaryQueryPage(@RequestBody OrgInfoTreeF orgInfoF) {
        return contractConcludeAppService.orgInfoTree(orgInfoF);
    }
}
