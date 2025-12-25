package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.apps.model.reconciliation.fo.ListUserInfoF;
import com.wishare.finance.apps.model.reconciliation.fo.UserOrgRoleF;
import com.wishare.finance.apps.model.reconciliation.vo.UserMobileV;
import com.wishare.finance.apps.model.reconciliation.vo.UserOrgRoleV;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import com.wishare.finance.infrastructure.remote.vo.user.ArchivesUserV;
import com.wishare.finance.infrastructure.remote.vo.user.EnterpriseBaseInfoRV;
import com.wishare.finance.infrastructure.remote.vo.user.UserInfoRv;
import com.wishare.finance.infrastructure.remote.vo.user.UserTenantPermissionRv;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户中心
 *
 * @author yancao
 */
@OpenFeignClient(name = "wishare-user", serverName = "用户中心", path = "/user")
public interface UserClient {

    @GetMapping(value = "/permission/listDataPermissions", name = "获取用户配置的空间权限")
    List<UserTenantPermissionRv> listDataPermissions(@RequestParam("userId") String userId, @RequestParam("dataType") String dataType);
    @GetMapping(value = "/info", name = "用户信息获取")
    UserInfoRv getUserInfo(@RequestParam("id") String id,
                           @RequestParam(value = "tenant", required = false) Boolean tenant);

    @PostMapping(value = "/archivesUser/listByUserIds", name = "根据用户ID列表查询档案信息")
    List<ArchivesUserV> getPayUser(@RequestBody List<String> userIds);

    @GetMapping(value = "/enterpriseInfo", name = "单个查询")
    @ApiOperation(value = "单个查询", notes = "ID或者统一社会信用代码不能一起为空，一起为空时，直接返回空", response = EnterpriseBaseInfoRV.class)
    EnterpriseBaseInfoRV getEnterpriseInfo(@RequestParam(value = "id", required = false) Long id,
                                           @RequestParam(value = "creditCode", required = false) String creditCode);

    /**
     * 获取员工手机号信息
     *
     * @param listUserInfoF
     * @return
     */
    @PostMapping(value = "/info/listUserInfoBy")
    List<UserMobileV> listUserInfoBy(@RequestBody ListUserInfoF listUserInfoF);

    @PostMapping("/info/getUserOrgRole")
    List<UserOrgRoleV> getUserOrgRole(@RequestBody UserOrgRoleF userOrgRoleF);


    /**
     * Description :  根据租户ID角色ID查询用户信息-列表
     * @author Yuting.Wang
     * @since 2025/2/6 10:43
     **/
    @GetMapping("/info/listUserInfoByTenantIdAndRoleId")
    List<UserInfoRawV> listUserInfoByTenantIdAndRoleId(
            @ApiParam(value = "用户ID", required = true) @NotBlank(message = "租户ID不能为空") @RequestParam("tenantId") String tenantId,
            @ApiParam(value = "角色ID", required = true) @NotBlank(message = "角色ID不能为空") @RequestParam("roleId") String roleId,
            @ApiParam(value = "用户名或手机号码") @RequestParam(value = "symbol", required = false) String symbol,
            @ApiParam(value = "列表长度，最大长度100")
            @Max(value = 100, message = "最大不能大于100")
            @Min(value = 1, message = "最小不能小于1")
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
            @RequestParam(value = "fullOrg", required = false, defaultValue = "true") Boolean fullOrg);

    @GetMapping(value = "/info", name = "根据用户ID查询用户信息")
    UserInfoRv getUserInfoByUserId(@RequestParam("id") String id);

}
