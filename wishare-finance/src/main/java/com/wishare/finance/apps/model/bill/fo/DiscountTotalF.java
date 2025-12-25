package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 账单减免合计检索条件
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单减免合计检索条件")
public class DiscountTotalF {

    @ApiModelProperty(value = "费项id", required = true)
    @NotNull(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "房号ID", required = true)
    @NotBlank(message = "房号ID不能为空")
    private String roomId;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单， 66全部）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    /**
     * 上级收费单元ID
     */
    @ApiModelProperty(value = "上级收费单元ID", required = true)
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

}
