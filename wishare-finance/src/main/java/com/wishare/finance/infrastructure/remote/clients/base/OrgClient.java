package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.apps.model.bill.vo.OrgFinanceCostInfoV;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.fo.customer.CustomerF;
import com.wishare.finance.infrastructure.remote.fo.org.SupplierF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.customer.CustomerV;
import com.wishare.finance.infrastructure.remote.vo.org.*;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 组织中心
 *
 * @author yancao
 */
@OpenFeignClient(name = "${wishare.feignClients.org.name:wishare-org}", serverName = "组织中心", path = "${wishare.feignClients.org.context-path:/org}")
public interface OrgClient {

    @PostMapping("/customer/getByMainDataCode")
    CustomerV getByMainDataCode(@RequestBody CustomerF customerF);

    @PostMapping(value = "/orgFinance/list",name = "获取当前租户下法定单位列表")
    List<StatutoryBodyRv> getOrgFinanceList(@RequestBody OrgFinanceF orgFinanceF);

    @PostMapping("/orgFinanceCost/page")
    @ApiOperation(value = "分页获取成本中心信息", notes = "分页获取成本中心信息")
    PageV<OrgFinanceCostInfoV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF);

    @GetMapping(value = "/orgFinance/orgByPid",name = "查询当前组织和父级组织列表")
    List<Long> orgFinanceOrgByPid(@RequestParam("id") Long id);

    @GetMapping(value = "/orgFinance",name = "根据id查询财务组织详情")
    OrgFinanceRv orgFinance(@RequestParam("id") String id);

    @GetMapping(value = "/tenant/getById",name = "根据id获取租户详情")
    OrgTenantRv tenantGetById(@RequestParam("id") String id);

    /**
     * 获取当前租户下法定单位列表
     *
     * @param queryPageF queryPageF
     * @return List
     */
    @PostMapping("/orgFinance/page")
    PageV<StatutoryBodyRv> getOrgFinancePage(@RequestBody @Validated PageF<SearchF<?>> queryPageF);

    /**
     * 获取当前租户下法定单位或成本中心
     * 可筛选成本中心形式
     * @param queryPageF
     * @return
     */
    @PostMapping("/orgFinance/pageV2")
    PageV<StatutoryBodyRv> getOrgFinancePageV2(@RequestBody @Validated PageF<SearchF<?>> queryPageF);

    /**
     * 根据id批量获取财务组织信息
     *
     * @param idList 组织id集合
     * @return List
     */
    @PostMapping("/orgFinance/getList")
    List<OrgFinanceTreeRv> getOrgListById(@RequestBody @Validated List<Long> idList);

    /**
     * 根据客商名称获取客商id
     * @param name
     * @return
     */
    @GetMapping("/merchant/query/name/{name}")
    Long getMerchantIdByName(@PathVariable("name") String name);

    @GetMapping("/merchant/query/id/{id}")
    MerchantRv getMerchantById(@PathVariable("id") Long id);

    @PostMapping("/merchant/query/listBySn")
    Set<String> getCreditCodeBySn(@RequestBody Set<String> sn) ;


    @GetMapping("/merchant/query/list")
    List<MerchantRv> listMerchantByName(@RequestParam("name") String name);

    /**
     * 根据编码和名称查询客商
     * @param code 编码
     * @param name 名称
     * @return 客商列表
     */
    @PostMapping("/merchant/query/listByCodeAndName")
    List<MerchantRv> getMerchantByCodeAndName(@RequestParam("code") String code, @RequestParam("name") String name);

    /**
     * 根据法定单位编码获取法定单位信息
     * @param code
     * @return
     */
    @GetMapping("/orgFinance/sbByCode")
    OrgFinanceRv getSbByCode(@RequestParam("code") @ApiParam("法定单位编码") String code);

    /**
     * 根据编码和名称查询成本中心列表
     * @param code 成本中心编码
     * @param name 成本中心名称
     * @return 成本中心列表
     */
    @GetMapping("/orgFinanceCost/getByCodeAndName")
    List<OrgFinanceCostRv> getOrgFinanceCostByCodeAndName(@RequestParam("code") String code, @RequestParam("name") String name);

    /**
     * 根据id查询成本信息
     * @param id
     * @return
     */
    @GetMapping("/orgFinanceCost/getById")
    OrgFinanceCostRv getOrgFinanceCostById(@RequestParam("id") Long id);

    @GetMapping("/orgFinance/{orgFinanceId}")
    @ApiOperation(value = "根据id查询法定单位")
    OrgFinanceRv getOrgFinanceById(@ApiParam("法定单位id") @PathVariable("orgFinanceId") Long orgFinanceId);

    @GetMapping("/orgFinance/permission")
    @ApiOperation(value = "法定单位、成本中心 权限查询")
    PermissionRV permission(
            @ApiParam(value = "1 法定单位 2成本中心 权限查询", required = true) @RequestParam(value = "type",required = true) Integer type,
            @ApiParam(value = "用户ID", required = true) @RequestParam(value = "userId",required = true) String userId,
            @ApiParam(value = "查询节点ID", required = false) @RequestParam(value = "nodeId",required = false) Long nodeId
    );


    // todo 报错先注掉了
//    @GetMapping("/orgFinance/permissionByIds")
//    @ApiOperation(value = "法定单位、成本中心 权限查询")
//    PermissionRV permissionByIds(
//            @RequestBody PermissionF permissionF
//    );

    @ApiOperation(value = "根据编码列表查询法定单位", response = OrgFinanceRv.class)
    @PostMapping("/orgFinance/sbListByCodes")
    List<OrgFinanceRv> listByCodes(@RequestBody @ApiParam("编码列表") List<String> codes);

    @ApiOperation(value = "根据id列表查询行政组织", response = OrgInfoRv.class)
    @PostMapping("/orgInfo/getOrgListByOrgIds")
    List<OrgInfoRv> getOrgListByOrgIds(@RequestBody @ApiParam("编码列表") Map<String, Object> stringObjectHashMap);

    /**
     * 当前分接口远洋专用
     * @param orgFinanceCostForBlockF
     * @return
     */
    @ApiOperation(value = "(远洋)根据项目下的期区ids查询成本信息集", notes = "(远洋)根据项目下的期区ids查询成本信息集")
    @PostMapping("/orgFinanceCost/getByBlockIds")
    List<OrgFinanceCostV> getByBlockIds(@RequestBody OrgFinanceCostForBlockF orgFinanceCostForBlockF);

    @ApiOperation(value = "根据项目ids查询财务组织",notes = "根据项目id查询财务组织id")
    @PostMapping("/orgFinanceCost/queryFinanceIdByCommunityId")
    List<OrgFinanceCostV> queryFinanceIdByCommunityId(@RequestBody List<String> communityIds);

    @GetMapping(value = "/orgFinanceCost/getByCommunityId", name = "根据项目id获取成本信息")
    OrgFinanceCostV getOrgFinanceCostByCommunityId(@RequestParam("communityId") String communityId);


    @ApiOperation(value = "根据id查询4A组织id")
    @PostMapping("/orgFinance/queryOids")
    List<String> queryOids(@RequestBody List<Long> ids);

    @ApiOperation(value = "批量查询所属项目id")
    @PostMapping("/orgFinanceCost/getCommunityIds")
    List<OrgFinanceCostV> getCommunityIds(@RequestBody List<Long> ids);

    @ApiOperation(value = "获取租户的顶级组织", notes = "获取租户的顶级组织", response = Long.class)
    @GetMapping("/orgInfo/getTenantIdByOrgId")
    Long getTenantIdByOrgId();

    @ApiOperation(value = "内部-简单列表", notes = "内部-简单列表")
    @PostMapping("/customer/simpleList")
    List<CustomerSimpleV> simpleList(@RequestBody List<String> ids);

    @ApiOperation(value = "内部-简单列表-supplier", notes = "内部-简单列表-supplier")
    @PostMapping("/supplier/simpleList")
    List<SupplierSimpleV> simpleListSupplier(@RequestBody List<String> ids);

    @ApiOperation(value = "查询所有有效组织OID", notes = "查询所有有效组织OID")
    @PostMapping("/orgInfo/all/oid")
    List<String> getAllValidOidList();

    @ApiOperation(value = "根据ID查询组织详情", notes = "根据ID查询组织详情", response = OrgDetailsV.class)
    @GetMapping("/orgInfo/getById")
    OrgDetailsV getOrgInfo(@RequestParam("id") Long id);

}
