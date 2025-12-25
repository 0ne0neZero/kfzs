package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class BillDeductionF {

    @ApiModelProperty("账单id")
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "单据类型", required = true)
    @NotNull(message = "单据类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "扣款金额", required = true)
    @NotNull(message = "扣款金额不能为空")
    private BigDecimal deductionAmount;

    @ApiModelProperty("扣款原因")
    private String remark;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元Id不能为空!")
    private String supCpUnitId;


}
