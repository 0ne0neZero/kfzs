package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fxl
 * @describe
 * @date 2024/3/12
 */
@Getter
@Setter
public class UpdateSignF {

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "操作房间id")
    @NotBlank(message = "操作房间不能为空")
    private String roomId;

    @ApiModelProperty(value = "签约行为 true 签约， false 未签约")
    @NotNull(message = "签约行为不能为空")
    private Boolean isSign;
}
