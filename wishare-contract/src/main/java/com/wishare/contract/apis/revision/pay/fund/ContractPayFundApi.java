package com.wishare.contract.apis.revision.pay.fund;

import com.wishare.contract.apps.fo.revision.FunChargeItemF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundPageF;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.pay.fund.ContractPayFundAppService;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundSaveF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundUpdateF;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundListV;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundListF;
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
 * 支出合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-25
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支出合同-款项表"})
@RequestMapping("/contractPayFund")
public class ContractPayFundApi {

    private final ContractPayFundAppService contractPayFundAppService;

    private final ContractPayFundService contractPayFundService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayFundV.class)
    @GetMapping("/detail")
    public ContractPayFundV get(@RequestParam("id") String id){
        return contractPayFundAppService.getDetail(id);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayFundV.class)
    @PostMapping("/list")
    public ContractPayFundListV list(@Validated @RequestBody ContractPayFundListF contractPayFundListF){
        return contractPayFundAppService.list(contractPayFundListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String save(@Validated @RequestBody ContractPayFundSaveF contractPayFundF){
        return contractPayFundAppService.save(contractPayFundF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PostMapping("/edit")
    public String update(@Validated @RequestBody ContractPayFundUpdateF contractPayFundF){
        contractPayFundAppService.update(contractPayFundF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "数据批处理(新增，编辑，删除)", notes = "数据批处理(新增，编辑，删除)")
    @PostMapping("/dealBatch")
    public Boolean dealBatch(@Validated @RequestBody List<ContractPayFundUpdateF> list) {
        //走正常清单金额校验逻辑
        return contractPayFundService.dealBatch(list, true);
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayFundAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractPayFundV.class)
    @PostMapping("/page")
    public PageV<ContractPayFundV> page(@RequestBody PageF<ContractPayFundPageF> request) {
        return contractPayFundAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayFundV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayFundV> frontPage(@RequestBody PageF<SearchF<ContractPayFundE>> request) {
        return contractPayFundAppService.frontPage(request);
    }

    @ApiOperation(value = "根据合同ID获取关联款项数据", notes = "根据合同ID获取关联款项数据")
    @GetMapping("/getFundForContract")
    public List<ContractPayFundV> getFundForContract(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayFundAppService.getFundForContract(id);
    }

    @ApiOperation(value = "根据支出合同ID获取清单费项数据", notes = "根据支出合同ID获取清单费项数据")
    @GetMapping("/getFundChargeItemById")
    public List<FunChargeItemF> getFundChargeItemById(@RequestParam("id") @NotBlank(message = "合同ID不可为空") String id) {
        return contractPayFundService.getFundChargeItemById(id);
    }
}
