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
@ApiModel("收款业务收付方信息:付款方信息")
public class ProcessPayerF {

    @NotBlank(message = "付款方不能为空")
    @ApiModelProperty(value = "付款方", required = true)
    private String payerAccount;

    @NotBlank(message = "付款方编码不能为空")
    @ApiModelProperty(value = "付款方编码", required = true)
    private String payerCode;

    @ApiModelProperty(value = "企业信用代码")
    private String creditCode;

}
