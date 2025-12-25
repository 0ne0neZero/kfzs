package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.fo.contractset.OrgInfoTreeF;
import com.wishare.contract.apps.remote.fo.org.CustomerAccountListF;
import com.wishare.contract.apps.remote.fo.org.CustomerListF;
import com.wishare.contract.apps.remote.vo.MerchantRv;
import com.wishare.contract.apps.remote.vo.OrgFinanceCostRv;
import com.wishare.contract.apps.remote.vo.OrgFinanceRv;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.apps.remote.vo.OrgRevFinanceCostRV;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.apps.remote.vo.org.CustomerAccountListV;
import com.wishare.contract.apps.remote.vo.org.CustomerListV;
import com.wishare.contract.apps.remote.vo.org.OrgDetailsRV;
import com.wishare.contract.apps.remote.vo.revision.CustomerRv;
import com.wishare.contract.apps.remote.vo.revision.SupplierRv;
import com.wishare.contract.domains.vo.contractset.OrgIdsF;
import com.wishare.contract.domains.vo.contractset.OrgInfoTreeV;
import com.wishare.contract.domains.vo.contractset.OrgInfoV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 组织接口
 *
 * @author yancao
 */
@OpenFeignClient(name = "wishare-org", serverName = "组织中心", path = "/org")
public interface OrgFeignClient {

    /**
     * 根据id获取租户详情
     *
     * @param id 租户id
     * @return TenantInfoRv
     */
    @GetMapping("/tenant/getById")
    TenantInfoRv getById(@RequestParam(value = "id") String id);

    /**
     * 根据id获取组织详情
     *
     * @param id 组织id
     * @return OrgInfoRv
     */
    @GetMapping("/orgInfo/getById")
    OrgInfoRv getByOrgId(@RequestParam(value = "id") Long id);

    /**
     * 根据id查询客商详情
     *
     * @param id 客商id
     * @return MerchantRv
     */
    @GetMapping("/merchant/query/id/{id}")
    MerchantRv queryById(@PathVariable(value = "id") Long id);

    /**
     * 根据id获取财务组织名称
     *
     * @param id 财务组织id
     * @return OrgFinanceRv
     */
    @GetMapping("/orgFinance")
    OrgFinanceRv getByFinanceId(@RequestParam(value = "id") Long id);

    @GetMapping("/tenant/getById")
    @ApiOperation(value = "根据租户id获取租户信息", notes = "根据租户id获取租户信息")
    ResponseEntity<TenantInfoRv> getTenantInfoById(@RequestParam("id")String id);

    @GetMapping("/orgInfo/getOrgListByOrgId")
    @ApiOperation(value = "根据组织Id查询组织下全部子组织", notes = "根据组织Id查询组织下全部子组织")
    List<OrgDetailsRV> getOrgListByOrgId(@RequestParam("orgId")Long orgId);

    @GetMapping("/orgInfo/getById")
    @ApiOperation(value = "根据ID查询组织详情", notes = "根据ID查询组织详情")
    ResponseEntity<OrgDetailsRV> getOrgById(@RequestParam("id") Long id);

    /**
     * 根据id获取orgFinanceCost
     *
     * @param id 财务组织id
     * @return orgFinanceCost
     */
    @GetMapping("/orgFinanceCost/getById")
    OrgRevFinanceCostRV getByFinanceCostId(@RequestParam(value = "id") Long id);

    /**
     * 根据id获取财务组织名称
     *
     * @param id 财务组织id
     * @return OrgFinanceRv
     */
    @GetMapping("/orgFinanceCost/getById")
    OrgFinanceCostRv getFinanceCostByFinanceId(@RequestParam(value = "id") Long id);

    /**
     * 根据Id获取供应商信息
     * @param id 供应商ID
     * @return SupplierRv
     */
    @GetMapping("/manage/supplier/getDetailById")
    SupplierRv getSupplierVById(@RequestParam(value = "id") String id);

    /**
     * 根据Id获取客户信息
     * @param id 客户ID
     * @return CustomerRv
     */
    @GetMapping("/manage/customer/getDetailById")
    CustomerRv getCustomerVById(@RequestParam(value = "id") String id);

    @PostMapping("/orgInfo/belowOrgIds")
    Set<String> belowOrgIds(@RequestBody Set<String> targetOrgIds);

    @PostMapping("/orgInfo/listTree")
    List<OrgInfoTreeV> orgInfoTree(@RequestBody OrgInfoTreeF orgInfoF);

    @GetMapping("/customer/list")
    CustomerListV getCustomerList(@RequestBody CustomerListF customerListF);

    @GetMapping("/customerAccount/list")
    CustomerAccountListV getCustomerAccountList(@RequestBody CustomerAccountListF customerAccountListF);

    @ApiOperation(value = "根据组织ids查询组织信息", notes = "根据组织ids查询组织信息", response = OrgInfoV.class)
    @PostMapping("/orgInfo/getOrgListByOrgIds")
    List<OrgInfoV> getOrgListByOrgIds(@RequestBody OrgIdsF orgIds);

    @GetMapping("/orgFinance/{orgFinanceId}")
    @ApiOperation(value = "根据id查询法定单位")
    OrgFinanceRv getOrgFinanceById(@ApiParam("法定单位id") @PathVariable("orgFinanceId") Long orgFinanceId);
}
