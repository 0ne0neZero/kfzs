package com.wishare.contract.apis.revision;

import com.wishare.contract.apps.fo.revision.pay.ContractPayConcludePageF;
import com.wishare.contract.apps.fo.revision.remote.OrgRelationF;
import com.wishare.contract.apps.remote.fo.org.CustomerMutualF;
import com.wishare.contract.apps.remote.fo.org.SupplierMutualF;
import com.wishare.contract.apps.service.revision.org.ContractOrgRelationService;
import com.wishare.contract.domains.vo.revision.ContractMiniOrgV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeDetailV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/5  9:38
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"改版合同与供应商客户交互处理管理API"})
@RequestMapping("/revision")
public class RevisionRelationApi {

    private final ContractOrgRelationService orgRelationService;

    @ApiOperation(value = "根据供应商ID获取关联合同数据", notes = "根据供应商ID获取关联合同数据")
    @GetMapping("/getMutualBySupplierId")
    public SupplierMutualF getMutualBySupplierId(@RequestParam("id") @NotBlank(message = "供应商ID不可为空") String id) {
        return orgRelationService.mutualSupplierLasted(id);
    }

    @ApiOperation(value = "根据客户ID获取关联合同数据", notes = "根据客户ID获取关联合同数据")
    @GetMapping("/getMutualByCustomerId")
    public CustomerMutualF getMutualByCustomerId(@RequestParam("id") @NotBlank(message = "供应商ID不可为空") String id) {
        return orgRelationService.mutualCustomerLasted(id);
    }

    @ApiOperation(value = "供应商获取关联合同分页列表", response = ContractMiniOrgV.class)
    @PostMapping("/pageBySupplierId")
    public PageV<ContractMiniOrgV> pageBySupplierId(@RequestBody PageF<SearchF<OrgRelationF>> request) {
        return orgRelationService.pageBySupplierId(request);
    }

    @ApiOperation(value = "客户获取关联合同分页列表", response = ContractMiniOrgV.class)
    @PostMapping("/pageByCustomerId")
    public PageV<ContractMiniOrgV> pageByCustomerId(@RequestBody PageF<SearchF<OrgRelationF>> request) {
        return orgRelationService.pageByCustomerId(request);
    }

    @ApiOperation(value = "将数据库中一些关键金额字段为空的数据初始化金额字段为0", notes = "将数据库中一些关键金额字段为空的数据初始化金额字段为0")
    @GetMapping("/fixAmountNullData")
    public Integer fixAmountNullData() {
        return orgRelationService.fixAmountNullData();
    }

}
