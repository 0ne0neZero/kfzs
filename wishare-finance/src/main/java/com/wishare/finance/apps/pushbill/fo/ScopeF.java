package com.wishare.finance.apps.pushbill.fo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("适用范围新增信息")
public class ScopeF {
    @ApiModelProperty(value = "项目id")
    private String id;
    @ApiModelProperty(value = "项目名称")
    private String name;
    @ApiModelProperty(value = "类型  项目 0 期区 1")
    private Integer type;
}
