package com.wishare.contract.apis.revision.pay.team;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamPageF;
import com.wishare.contract.domains.entity.revision.pay.team.ContractPayTeamE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.pay.team.ContractPayTeamAppService;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamV;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamSaveF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamUpdateF;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamListV;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamListF;
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
 * 支出合同-团队表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同-团队表"})
@RequestMapping("/contractPayTeam")
public class ContractPayTeamApi {

    private final ContractPayTeamAppService contractPayTeamAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayTeamV.class)
    @GetMapping("/detail")
    public ContractPayTeamV get(@RequestParam("id") String id){
        return contractPayTeamAppService.get(id);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayTeamV.class)
    @PostMapping("/list")
    public ContractPayTeamListV list(@Validated @RequestBody ContractPayTeamListF contractPayTeamListF){
        return contractPayTeamAppService.list(contractPayTeamListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractPayTeamSaveF contractPayTeamF){
        return contractPayTeamAppService.save(contractPayTeamF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractPayTeamUpdateF contractPayTeamF){
        contractPayTeamAppService.update(contractPayTeamF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayTeamAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractPayTeamV.class)
    @PostMapping("/page")
    public PageV<ContractPayTeamV> page(@RequestBody PageF<ContractPayTeamPageF> request) {
        return contractPayTeamAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayTeamV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayTeamV> frontPage(@RequestBody PageF<SearchF<ContractPayTeamE>> request) {
        return contractPayTeamAppService.frontPage(request);
    }

    @ApiOperation(value = "根据合同ID获取关联团队数据", notes = "根据合同ID获取关联团队数据")
    @GetMapping("/getTeamForContract")
    public List<ContractPayTeamV> getTeamForContract(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayTeamAppService.getTeamForContract(id);
    }

}
