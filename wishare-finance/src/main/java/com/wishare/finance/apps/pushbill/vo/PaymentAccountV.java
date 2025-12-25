package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 付款账户信息
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@ApiModel(value="支付申请单-付款账户信息")
public class PaymentAccountV implements Serializable {

    @ApiModelProperty(value = "ID")
    private String ID;
    @ApiModelProperty(value = "账号")
    private String ACCOUNTNO;
    @ApiModelProperty(value = "中文名称")
    private String ACCOUNTNAME_CHS;
    @ApiModelProperty(value = "核算单位")
    private String AccountUnit;
    @ApiModelProperty(value = "账户性质")
    private String AccountProperty;
    @ApiModelProperty(value = "账户状态 1,未启用;2,启用;3,停用;4,已销户;5,冻结")
    private String AccountStatus;
    @ApiModelProperty(value = "人行联行名称")
    private String BankNameOfChina;
    @ApiModelProperty(value = "人行联行号")
    private String BankNoOfChina;
    @ApiModelProperty(value = "开户单位")
    private String OpenAccountUnit;

}
