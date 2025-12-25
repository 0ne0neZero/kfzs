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
@ApiModel("收款业务收付方信息:收款方信息")
public class ProcessPayeeF {

    @NotBlank(message = "收款账号不能为空")
    @ApiModelProperty(value = "收款账号", required = true)
    private String payeeAccount;

    @NotNull(message = "收款金额不能为空")
    @ApiModelProperty(value = "收款金额", required = true)
    private Long receivedAmount;

}
