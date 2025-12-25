package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 结算方式
 */
@Data
public class PaymentMethodV implements Serializable {

    @ApiModelProperty("ID")
    private String ID;

    @ApiModelProperty("编号")
    private String CODE;

    @ApiModelProperty("中文简体名称")
    private String NAME_CHS;

    @ApiModelProperty("PJ :票据 XH：现汇")
    private String SETTLEMENTPROPERTY;

    @ApiModelProperty("启用状态 1：启用 0：停用")
    private String STATE_ISENABLED;
}
