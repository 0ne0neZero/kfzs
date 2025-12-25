package com.wishare.finance.apps.model.bill.fo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "创建应收账单请求信息", parent = AddBillF.class)
public class AddReceivableBillF extends AddBillF{

    @ApiModelProperty(value = "计费方式", required = true)
    @NotNull(message = "计费方式不能为空")
    private Integer billMethod;

    @ApiModelProperty(value = "计费面积")
    private Integer billArea;

    @ApiModelProperty("计费额度")
    @Digits(integer = 18, fraction = 6, message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMax(value = "1000000000.000000", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMin(value = "0.000001", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    //@Max(value = 1000000000, message = "单价格式不正确，允许区间为[1, 1000000000]")
    //@Min(value = 1, message = "单价格式不正确，允许区间为[1, 1000000000]")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "账单开始时间", required = true)
    @NotBlank(message = "账单开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间", required = true)
    @NotBlank(message = "账单结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "应收日")
    private Integer receivableDay;

    @ApiModelProperty(value = "调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否")
    private Integer isExact;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;

    @ApiModelProperty("空间路径")
    private String path;

    @ApiModelProperty("是否签约")
    private Boolean isSign ;

}
