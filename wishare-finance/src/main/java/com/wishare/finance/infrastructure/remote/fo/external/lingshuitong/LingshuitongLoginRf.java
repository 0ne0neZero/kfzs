package com.wishare.finance.infrastructure.remote.fo.external.lingshuitong;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/11/28
 * @Description:
 */
@Getter
@Setter
@ApiModel("开灵单点登录入参")
public class LingshuitongLoginRf {

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;
}
