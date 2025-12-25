package com.wishare.contract.apis.revision.income.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundPageF;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.income.fund.ContractIncomeFundAppService;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundSaveF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundListV;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundListF;
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
 * 收入合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收入合同-款项表"})
@RequestMapping("/contractIncomeFund")
public class ContractIncomeFundApi {

    private final ContractIncomeFundAppService contractIncomeFundAppService;

    private final ContractIncomeFundService contractIncomeFundService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractIncomeFundV.class)
    @GetMapping("/detail")
    public ContractIncomeFundV get(@RequestParam("id") String id){
        return contractIncomeFundAppService.get(id);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractIncomeFundV.class)
    @PostMapping("/list")
    public ContractIncomeFundListV list(@Validated @RequestBody ContractIncomeFundListF contractIncomeFundListF){
        return contractIncomeFundAppService.list(contractIncomeFundListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractIncomeFundSaveF contractIncomeFundF){
        return contractIncomeFundAppService.save(contractIncomeFundF).getId();
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PostMapping("/edit")
    public String update(@Validated @RequestBody ContractIncomeFundUpdateF contractIncomeFundF){
        contractIncomeFundAppService.update(contractIncomeFundF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "数据批处理(新增，编辑，删除)", notes = "数据批处理(新增，编辑，删除)")
    @PostMapping("/dealBatch")
    public Boolean dealBatch(@Validated @RequestBody List<ContractIncomeFundUpdateF> list) {
        return contractIncomeFundService.dealBatch(list, true);
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractIncomeFundAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractIncomeFundV.class)
    @PostMapping("/page")
    public PageV<ContractIncomeFundV> page(@RequestBody PageF<ContractIncomeFundPageF> request) {
        return contractIncomeFundAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractIncomeFundV.class)
    @PostMapping("/pageFront")
    public PageV<ContractIncomeFundV> frontPage(@RequestBody PageF<SearchF<ContractIncomeFundE>> request) {
        return contractIncomeFundAppService.frontPage(request);
    }

    @ApiOperation(value = "根据收入合同ID获取关联款项服务数据", notes = "根据收入合同ID获取关联款项服务数据")
    @GetMapping("/getFundForContract")
    public List<ContractIncomeFundV> getFundForContract(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeFundAppService.getFundForContract(id);
    }

    @ApiOperation(value = "根据收入合同ID获取清单费项数据", notes = "根据收入合同ID获取清单费项数据")
    @GetMapping("/getFundChargeItemById")
    public List<FunChargeItemF> getFundChargeItemById(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractIncomeFundService.getFundChargeItemById(id);
    }

}
