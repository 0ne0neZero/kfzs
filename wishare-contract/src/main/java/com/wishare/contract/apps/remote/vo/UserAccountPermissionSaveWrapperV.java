package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class UserAccountPermissionSaveWrapperV {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户账号权限")
    private Map<String, UserAccountPermissionV> userAccountPermission;

}
