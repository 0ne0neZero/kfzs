package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("收款单明细退款参数入口")
public class GatherDetailInfoF {

    @ApiModelProperty("收款单ID")
    @NotNull(message = "收款单ID不能为空")
    private Long gatherBillId;

    @ApiModelProperty("收款单编号")
    @NotNull(message = "收款单编号不能为空")
    private String gatherBillNo;

    @ApiModelProperty("收款明细ID")
    @NotNull(message = "收款单ID不能为空")
    private Long gatherDetailId;

    @ApiModelProperty("收款明细关联账单编号")
    @NotBlank(message = "收款明细关联账单编号不能为空")
    private String recBillNo;

    @ApiModelProperty("收款明细关联账单id")
    @NotNull(message = "收款明细关联账单id不能为空")
    private Long recBillId;

    @ApiModelProperty("收款明细账单类型 0:应收账单/临时账单 1:预收账单")
    @NotNull(message = "收款明细账单类型不能为空")
    private Integer gatherType;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    @ApiModelProperty("收款明细关联房号")
    private Long cpUnitId;

    @ApiModelProperty("收款明细关联房号名称")
    private String cpUnitName;

    @ApiModelProperty("费项ID")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;
}
