package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 招商银企直连支付信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/2
 */
@Getter
@Setter
@ApiModel("招商银企直连支付信息")
public class CbsF {

    @ApiModelProperty(value = "付方款人账户号")
    private String outAccount;

    @ApiModelProperty(value = "付方客户号 付款账号所属企业号")
    private String payNumber;

    @ApiModelProperty(value = "收款人账号, 内转时为收款内部户，其他业务为收款银行账号")
    private String receiveAccount;

    @ApiModelProperty(value = "收款人开户行")
    private String receiveBank;

    @ApiModelProperty(value = "收款人名称 收方银行类型，对外支付、银行调拨、集中支付不能为空")
    private String receivePerson;

}
