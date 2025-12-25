package com.wishare.contract.apps.remote.clients;


import com.wishare.contract.apps.fo.remind.ListUserInfoF;
import com.wishare.contract.apps.fo.remind.UserOrgRoleF;
import com.wishare.contract.apps.remote.vo.UserAccountPermissionSaveWrapperV;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.domains.vo.revision.remind.UserMobileV;
import com.wishare.contract.domains.vo.revision.remind.UserOrgRoleV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @Author wangrui
 * 用户中心调用
 * @Date 2022/12/08
 */
@OpenFeignClient(name = "wishare-user", serverName = "用户中心", path = "/user")
public interface UserFeignClient {

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户ID
     * @param gatewayTagEnum 网关标识，可不传，默认使用发起调用方的网关标识
     * @return
     */
    @GetMapping(value = "/info/pickUserInfo", name = "根据用户id查询用户信息")
    UserInfoRv getUserInfoById(@ApiParam(value = "用户ID",required = true) @RequestParam("userId") String userId,
                               @ApiParam(value = "需要获取的指定字段", required = true) @RequestParam("fields") List<String> fields,
                               @ApiParam("网关标识，可不传，默认使用发起调用方的网关标识") @RequestParam("gateway") String gatewayTagEnum);

    @GetMapping(value = "/info", name = "根据用户ID查询用户信息")
    UserInfoRv getUsreInfoByUserId(@RequestParam("id") String id);


    @GetMapping(value = "/state", name = "根据用户ID查询用户是否为超级管理员")
    UserStateRV getStateByUserId(@RequestParam("userId") String userId);

    /**
     * 根据角色id和组织id获取用户id以及角色/组织权限列表
     *
     * @param userOrgRoleF
     * @return
     */
    @PostMapping("/info/getUserOrgRole")
    List<UserOrgRoleV> getUserOrgRole(@RequestBody UserOrgRoleF userOrgRoleF);

    /**
     * 获取员工手机号信息
     *
     * @param listUserInfoF
     * @return
     */
    @PostMapping(value = "/info/listUserInfoBy")
    List<UserMobileV> listUserInfoBy(@RequestBody ListUserInfoF listUserInfoF);

    @GetMapping("/permission/accountPermission")
    UserAccountPermissionSaveWrapperV getAccountPermission(@RequestParam("userId") String userId, @RequestParam("code") String code);

    @GetMapping("/info/getUserOrgIds")
    UserOrgRoleV getUserOrgIds(@RequestParam("userId") String userId);
}
