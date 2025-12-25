package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("列出用户信息")
@Data
public class ListUserInfoF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id列表")
    private List<String> userIds;

    @ApiModelProperty("网关标识")
    private String gatewaySymbol;

}
