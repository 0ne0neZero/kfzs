package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yyx
 * @date 2023/6/17 15:27
 */
@Setter
@Getter
public class BusinessUnitAccountBankDto {

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;
}
