package com.wishare.contract.apis.revision.invoice;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculatePageF;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillCalculateE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.invoice.ContractSettlementsBillCalculateAppService;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateUpdateF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateListV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateListF;
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
 * 结算单计量明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"结算单计量明细表"})
@RequestMapping("/contractSettlementsBillCalculate")
public class ContractSettlementsBillCalculateApi {

    private final ContractSettlementsBillCalculateAppService contractSettlementsBillCalculateAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractSettlementsBillCalculateV.class)
    @GetMapping
    public ContractSettlementsBillCalculateV get(@Validated ContractSettlementsBillCalculateF contractSettlementsBillCalculateF){
        return contractSettlementsBillCalculateAppService.get(contractSettlementsBillCalculateF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractSettlementsBillCalculateV.class)
    @PostMapping("/list")
    public ContractSettlementsBillCalculateListV list(@Validated @RequestBody ContractSettlementsBillCalculateListF contractSettlementsBillCalculateListF){
        return contractSettlementsBillCalculateAppService.list(contractSettlementsBillCalculateListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractSettlementsBillCalculateSaveF contractSettlementsBillCalculateF){
        return contractSettlementsBillCalculateAppService.save(contractSettlementsBillCalculateF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractSettlementsBillCalculateUpdateF contractSettlementsBillCalculateF){
        contractSettlementsBillCalculateAppService.update(contractSettlementsBillCalculateF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractSettlementsBillCalculateAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractSettlementsBillCalculateV.class)
    @PostMapping("/page")
    public PageV<ContractSettlementsBillCalculateV> page(@RequestBody PageF<ContractSettlementsBillCalculatePageF> request) {
        return contractSettlementsBillCalculateAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractSettlementsBillCalculateV.class)
    @PostMapping("/pageFront")
    public PageV<ContractSettlementsBillCalculateV> frontPage(@RequestBody PageF<SearchF<ContractSettlementsBillCalculateE>> request) {
        return contractSettlementsBillCalculateAppService.frontPage(request);
    }

}
