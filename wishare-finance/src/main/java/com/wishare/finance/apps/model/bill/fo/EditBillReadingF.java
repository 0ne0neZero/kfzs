package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/2/10
 * @Description:
 */
@Getter
@Setter
@ApiModel("调整应收账单")
public class EditBillReadingF {


    @ApiModelProperty(value = "应收账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;


    @ApiModelProperty("账单金额(单位：分)")
    private Long totalAmount;

    @ApiModelProperty("计费面积(单位：m²)")
    @Digits(integer = 18, fraction = 6, message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMax(value = "1000000000.000000", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMin(value = "0.000001", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}