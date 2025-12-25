package com.wishare.finance.apps.model.configure.businessunit.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yyx
 * @date 2023/6/17 15:24
 */
@Setter
@Getter
public class BusinessUnitAccountBankV {

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行账号")
    private String bankAccount;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;
}
