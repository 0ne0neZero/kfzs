package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 付款人信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("付款人信息")
public class Payer {

    @ApiModelProperty(value = "付款人ID")
    private String payerId;

    @ApiModelProperty(value = "付款人名称", required = true)
    private String payerName;

    @ApiModelProperty(value = "付款人电话", required = true)
    private String payerPhone;

    @ApiModelProperty(value = "付款人收款账号，银行转账必传")
    private String payerAccount;

    @ApiModelProperty(value = "付方客户号 付款账号所属企业号")
    private String payNumber;

    @ApiModelProperty(value = "银行类型, 银企直连和银行转账必填,银行联行号填了可不填")
    private String backType;

    @ApiModelProperty(value = "银行联行号, 银企直连和银行转账必填")
    private String bankNo;

}
