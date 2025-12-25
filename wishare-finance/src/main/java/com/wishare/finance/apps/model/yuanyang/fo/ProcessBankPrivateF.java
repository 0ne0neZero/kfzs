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
@ApiModel("银企直连收款方信息：对私")
public class ProcessBankPrivateF {

    @NotBlank(message = "收款人名称不能为空")
    @ApiModelProperty(value = "收款人名称", required = true)
    private String payee;

    @NotBlank(message = "收款人编码不能为空")
    @ApiModelProperty(value = "收款人编码", required = true)
    private String payeeCode;

    @NotBlank(message = "收款账号不能为空")
    @ApiModelProperty(value = "收款账号", required = true)
    private String payeeAccount;

    @NotBlank(message = "收款银行类型不能为空")
    @ApiModelProperty(value = "收款银行类型", required = true)
    private String payeeBankType;

    @NotNull(message = "收款金额不能为空")
    @ApiModelProperty(value = "收款金额", required = true)
    private Long receivedAmount;

    @ApiModelProperty(value = "企业信用代码")
    private String creditCode;

}
