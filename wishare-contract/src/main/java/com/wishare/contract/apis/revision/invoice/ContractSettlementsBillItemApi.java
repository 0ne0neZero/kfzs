package com.wishare.contract.apis.revision.invoice;

import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemPageF;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillItemE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.revision.invoice.ContractSettlementsBillItemAppService;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemUpdateF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemListV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemListF;
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
 * 收票款项明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收票款项明细表"})
@RequestMapping("/contractSettlementsBillItem")
public class ContractSettlementsBillItemApi {

    private final ContractSettlementsBillItemAppService contractSettlementsBillItemAppService;
    @ApiOperation(value = "获取详细信息", notes = "获取详细信息", response = ContractSettlementsBillItemV.class)
    @GetMapping
    public ContractSettlementsBillItemV get(@Validated ContractSettlementsBillItemF contractSettlementsBillItemF){
        return contractSettlementsBillItemAppService.get(contractSettlementsBillItemF);
    }

    @ApiOperation(value = "下拉列表", notes = "下拉列表，默认数量20", response = ContractSettlementsBillItemV.class)
    @PostMapping("/list")
    public ContractSettlementsBillItemListV list(@Validated @RequestBody ContractSettlementsBillItemListF contractSettlementsBillItemListF){
        return contractSettlementsBillItemAppService.list(contractSettlementsBillItemListF);
    }

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping
    public String save(@Validated @RequestBody ContractSettlementsBillItemSaveF contractSettlementsBillItemF){
        return contractSettlementsBillItemAppService.save(contractSettlementsBillItemF);
    }

    @ApiOperation(value = "更新", notes = "根据主键更新")
    @PutMapping
    public String update(@Validated @RequestBody ContractSettlementsBillItemUpdateF contractSettlementsBillItemF){
        contractSettlementsBillItemAppService.update(contractSettlementsBillItemF);
        return PromptInfo.OK.info;
    }

    @ApiOperation(value = "删除", notes = "根据主键删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractSettlementsBillItemAppService.removeById(id);
    }

    @ApiOperation(value = "分页列表", response = ContractSettlementsBillItemV.class)
    @PostMapping("/page")
    public PageV<ContractSettlementsBillItemV> page(@RequestBody PageF<ContractSettlementsBillItemPageF> request) {
        return contractSettlementsBillItemAppService.page(request);
    }

    @ApiOperation(value = "分页列表仅供前台调用", response = ContractSettlementsBillItemV.class)
    @PostMapping("/pageFront")
    public PageV<ContractSettlementsBillItemV> frontPage(@RequestBody PageF<SearchF<ContractSettlementsBillItemE>> request) {
        return contractSettlementsBillItemAppService.frontPage(request);
    }

}
