package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用户状态展示
 * </p>
 *
 * @author light
 * @since 2022/8/16
 */
@Data
@ApiModel("用户状态展示")
public class UserStateRV {

    @ApiModelProperty("账号是否正常，true正常，false不正常")
    private boolean normal;

    @ApiModelProperty("账号是否超管（租户超级管理和平台超级管理员）")
    private boolean superAccount;

}
