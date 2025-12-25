package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("同步")
public class SyncF {

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统 22 用友ncc",required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;
}
