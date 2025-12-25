package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 申请作废参数
 * @author dxclay
 * @since 2022-08-22
 */
@Getter
@Setter
@ApiModel("作废申请参数")
public class ApplyInvalidF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "审核原因", required = true)
    @NotBlank(message = "审核原因不能为空")
    private String reason;

}
