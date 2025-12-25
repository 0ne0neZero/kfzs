package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesF;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesUpdateF;
import com.wishare.contract.apps.service.contractset.ContractSpaceResourcesAppService;
import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import com.wishare.contract.domains.vo.contractset.ContractSpaceResourcesV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同空间资源信息"})
@RequestMapping("/contractSpaceResources")
public class ContractSpaceResourcesApi implements IOwlApiBase {

    private final ContractSpaceResourcesAppService contractSpaceResourcesAppService;

    @ApiOperation(value = "单个查询", notes = "查询详情", response = ContractSpaceResourcesV.class)
    @GetMapping("/getById")
    public ContractSpaceResourcesV get(@RequestParam(value = "id") Long id) {
        return contractSpaceResourcesAppService.get(id);
    }

    @ApiOperation(value = "空间资源列表", response = ContractSpaceResourcesV.class)
    @PostMapping("/list")
    public List<ContractSpaceResourcesV> list(@RequestBody ContractSpaceResourcesF contractSpaceResourcesF) {
        return contractSpaceResourcesAppService.list(contractSpaceResourcesF);
    }

//    @ApiOperation(value = "更新空间资源", notes = "更新空间资源")
//    @PutMapping
//    @OperationLog(bizType = "'update'", bizId = "#contractSpaceResourcesF.id", msg = "'用户' + #CustomFunctionStatic_testUserName() + " +
//            "'从' +#CustomFunctionStatic_testInfo(#contractSpaceResourcesF.id).name + '修改了'+ #contractSpaceResourcesF.name + #_DIFF(#testDiffUserParam1,#testDiffUserParam2)" )
//    public String update(@Validated @RequestBody ContractSpaceResourcesUpdateF contractSpaceResourcesF) {
//        LogRecordContext.putVariable("testDiffUserParam2",contractSpaceResourcesF);
//        LogRecordContext.putVariable("testDiffUserParam1",contractSpaceResourcesAppService.get(contractSpaceResourcesF.getId()));
//        contractSpaceResourcesAppService.update(contractSpaceResourcesF);
//        return PromptInfo.OK.info;
//    }

    @ApiOperation(value = "删除空间资源", notes = "删除空间资源")
    @DeleteMapping
    public boolean remove(@RequestParam(value = "id") Long id) {
        return contractSpaceResourcesAppService.remove(id);
    }

    @ApiOperation(value = "批量设置空间信息", notes = "批量设置空间信息")
    @PostMapping("/batchSetting")
    public void batchSetting(@Validated @RequestBody List<ContractSpaceResourcesUpdateF> resourcesUpdateFList) {
        contractSpaceResourcesAppService.batchSetting(resourcesUpdateFList);
    }

    @ApiOperation(value = "分页列表", response = ContractSpaceResourcesV.class)
    @PostMapping("/page")
    public PageV<ContractSpaceResourcesV> page(@RequestBody PageF<SearchF<ContractSpaceResourcesE>> request) {
        String tenantId = tenantId();
        return contractSpaceResourcesAppService.page(request, tenantId);
    }
}
