package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 付款人信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("付款人信息")
public class PayerF {

    @ApiModelProperty(value = "付款人id")
    @Length(max = 64, message = "付款人id格式不正确")
    private String payerId;

    @ApiModelProperty(value = "渠道付款人id")
    @Length(max = 64, message = "渠道付款人id格式不正确")
    private String channelPayerId;

    @ApiModelProperty(value = "付款人名称")
    @Length(max = 100, message = "付款人名称格式不正确")
    private String payerName;

    @ApiModelProperty(value = "付款人手机号")
    @Length(max = 32, message = "付款人手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "银行类型, 银企直连和银行转账必填,银行联行号填了可不填")
    private String backType;

    @ApiModelProperty(value = "银行联行号, 银企直连和银行转账必填")
    private String bankNo;

    @ApiModelProperty(value = "付款人收款账号，银行转账必传")
    @Length(max = 64, message = "付款人收款账号格式不正确")
    private String payerAccount;

    @ApiModelProperty(value = "付方客户号 付款账号所属企业号")
    private String payNumber;


}
