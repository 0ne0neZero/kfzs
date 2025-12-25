package com.wishare.contract.apis.revision.income.team;

import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamV;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamPageF;
import com.wishare.contract.domains.entity.revision.income.team.ContractIncomeTeamE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.income.team.ContractIncomeTeamAppService;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamV;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamSaveF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamUpdateF;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamListV;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamListF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 收入合同-团队表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同-团队表"})
@RequestMapping("/contractIncomeTeam")
public class ContractIncomeTeamApi {

    private final ContractIncomeTeamAppService contractIncomeTeamAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeTeamV.class)
    @GetMapping("/detail")
    public ContractIncomeTeamV get(@RequestParam("id") String id){
        return contractIncomeTeamAppService.get(id);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractIncomeTeamV.class)
    @PostMapping("/list")
    public ContractIncomeTeamListV list(@Validated @RequestBody ContractIncomeTeamListF contractIncomeTeamListF){
        return contractIncomeTeamAppService.list(contractIncomeTeamListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractIncomeTeamSaveF contractIncomeTeamF){
        return contractIncomeTeamAppService.save(contractIncomeTeamF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractIncomeTeamUpdateF contractIncomeTeamF){
        contractIncomeTeamAppService.update(contractIncomeTeamF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractIncomeTeamAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomeTeamV.class)
    @PostMapping("/page")
    public PageV<ContractIncomeTeamV> page(@RequestBody PageF<ContractIncomeTeamPageF> request) {
        return contractIncomeTeamAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractIncomeTeamV.class)
    @PostMapping("/pageFront")
    public PageV<ContractIncomeTeamV> frontPage(@RequestBody PageF<SearchF<ContractIncomeTeamE>> request) {
        return contractIncomeTeamAppService.frontPage(request);
    }

    @ApiOperation(value = "根据收入合同ID获取关联团队数据", notes = "根据收入合同ID获取关联团队数据")
    @GetMapping("/getTeamForContract")
    public List<ContractIncomeTeamV> getTeamForContract(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeTeamAppService.getTeamForContract(id);
    }

}
