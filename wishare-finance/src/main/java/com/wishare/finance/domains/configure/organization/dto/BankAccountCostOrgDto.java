package com.wishare.finance.domains.configure.organization.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
public class BankAccountCostOrgDto {

    @ApiModelProperty("银行账户id")
    private Long bankAccountId;

    @ApiModelProperty("开户行名称")
    private String bankAccountName;

    @ApiModelProperty("开户行账户")
    private String bankAccount;

}
