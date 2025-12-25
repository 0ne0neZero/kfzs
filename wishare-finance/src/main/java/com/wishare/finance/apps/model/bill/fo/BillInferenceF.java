package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 账单推凭
 * @author: pgq
 * @since: 2022/10/24 20:55
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("账单推凭")
public class BillInferenceF {

    @ApiModelProperty("账单id")
    Long billId;

    @ApiModelProperty("账单推凭动作")
    private int actionEventCode;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;
}
