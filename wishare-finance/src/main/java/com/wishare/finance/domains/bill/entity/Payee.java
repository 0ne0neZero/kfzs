package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收款人信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("收款人信息")
public class Payee {

    @ApiModelProperty(value = "收款人ID")
    private String payeeId;

    @ApiModelProperty(value = "收款人编号")
    private String payeeCode;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "收款人电话")
    private String payeePhone;

    @ApiModelProperty(value = "收款人账号, 银企直连和银行转账必填")
    private String payeeAccount;

    @ApiModelProperty(value = "收款人开户行, 银企直连和银行转账必填")
    private String payeeBank;

    @ApiModelProperty(value = "银行类型, 银企直连和银行转账必填,银行联行号填了可不填")
    private String backType;

    @ApiModelProperty(value = "银行联行号, 银企直连和银行转账必填")
    private String bankNo;

    @ApiModelProperty(value = "收款金额")
    private Long receivedAmount;

}
