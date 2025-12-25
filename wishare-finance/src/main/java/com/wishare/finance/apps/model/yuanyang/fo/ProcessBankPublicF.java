package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/24
 */
@Getter
@Setter
@ApiModel("银企直连收款方信息：对公")
public class ProcessBankPublicF {

    @NotBlank(message = "收款人编号不能为空")
    @ApiModelProperty(value = "收款人编号", required = true)
    private String payeeCode;

    @NotBlank(message = "收款人名称不能为空")
    @ApiModelProperty(value = "收款人名称", required = true)
    private String payee;

    @NotBlank(message = "收款账号不能为空")
    @ApiModelProperty(value = "收款账号", required = true)
    private String payeeAccount;

    @NotBlank(message = "收款联行号不能为空")
    @ApiModelProperty(value = "收款联行号", required = true)
    private String payeeUnionCode;

    @NotBlank(message = "收款开户行不能为空")
    @ApiModelProperty(value = "收款开户行", required = true)
    private String payeeBank;

    @NotNull(message = "收款金额不能为空")
    @ApiModelProperty(value = "收款金额", required = true)
    private Long receivedAmount;

    @ApiModelProperty(value = "企业信用代码")
    private String creditCode;

}
