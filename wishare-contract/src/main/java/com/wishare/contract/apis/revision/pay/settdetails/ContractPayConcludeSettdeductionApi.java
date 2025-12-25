package com.wishare.contract.apis.revision.pay.settdetails;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionPageF;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.pay.settdetails.ContractPayConcludeSettdeductionAppService;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionListV;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionListF;
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
/**
 * <p>
 * 结算单扣款明细表信息
 * </p>
 *
 * @author zhangfy
 * @since 2024-05-20
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"结算单扣款明细表信息"})
@RequestMapping("/contractPayConcludeSettdeduction")
public class ContractPayConcludeSettdeductionApi {

    private final ContractPayConcludeSettdeductionAppService contractPayConcludeSettdeductionAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractPayConcludeSettdeductionV.class)
    @GetMapping
    public ContractPayConcludeSettdeductionV get(@Validated ContractPayConcludeSettdeductionF contractPayConcludeSettdeductionF){
        return contractPayConcludeSettdeductionAppService.get(contractPayConcludeSettdeductionF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractPayConcludeSettdeductionV.class)
    @PostMapping("/list")
    public ContractPayConcludeSettdeductionListV list(@Validated @RequestBody ContractPayConcludeSettdeductionListF contractPayConcludeSettdeductionListF){
        return contractPayConcludeSettdeductionAppService.list(contractPayConcludeSettdeductionListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractPayConcludeSettdeductionSaveF contractPayConcludeSettdeductionF){
        return contractPayConcludeSettdeductionAppService.save(contractPayConcludeSettdeductionF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractPayConcludeSettdeductionUpdateF contractPayConcludeSettdeductionF){
        contractPayConcludeSettdeductionAppService.update(contractPayConcludeSettdeductionF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractPayConcludeSettdeductionAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractPayConcludeSettdeductionV.class)
    @PostMapping("/page")
    public PageV<ContractPayConcludeSettdeductionV> page(@RequestBody PageF<ContractPayConcludeSettdeductionPageF> request) {
        return contractPayConcludeSettdeductionAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractPayConcludeSettdeductionV.class)
    @PostMapping("/pageFront")
    public PageV<ContractPayConcludeSettdeductionV> frontPage(@RequestBody PageF<SearchF<ContractPayConcludeSettdeductionE>> request) {
        return contractPayConcludeSettdeductionAppService.frontPage(request);
    }

}
