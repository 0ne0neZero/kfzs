package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 账单票据红冲表单
 *
 * @Author dxclay
 * @Date 2023/1/11
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单票据红冲表单")
public class InvoiceReverseF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "红冲金额", required = true)
    @NotNull(message = "红冲金额不能为空")
    private Long reverseAmount;

}
