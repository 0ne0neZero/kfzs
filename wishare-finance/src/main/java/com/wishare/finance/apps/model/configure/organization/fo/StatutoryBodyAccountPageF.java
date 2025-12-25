package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("分页获取银行账户列表入参")
public class StatutoryBodyAccountPageF {

    @ApiModelProperty(value = "法定单位id", required = true)
    @NotBlank(message = "法定单位id不能为空")
    private Long statutoryBodyId;
}
