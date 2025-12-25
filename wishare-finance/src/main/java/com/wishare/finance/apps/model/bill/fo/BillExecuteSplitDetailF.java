package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yyx
 * @date 2023/5/8 15:04
 */
@Getter
@Setter
@ApiModel("账单拆分子账单拆分内容")
public class BillExecuteSplitDetailF {

    @ApiModelProperty("拆分账单收费对象类型")
    @NotNull(message = "收费对象类型不能为空")
    private Integer customerType;

    @ApiModelProperty("拆分账单收费对象")
    @NotBlank(message = "收费对象不能为空")
    private String customerId;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "计费面积 (单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("归属月")
    @NotBlank(message = "归属月不能为空")
    private String accountDate;

    @ApiModelProperty("账单金额")
    @DecimalMin(value = "0.01", message = "账单金额不能低于0.01")
    private BigDecimal totalAmount;

    @ApiModelProperty("尾差余数分配策略 true:表示分配至此")
    private Boolean isRemainderSplit;

    /** 账单拆分为结算账单和新账单，已结算账单集成原账单所有信息 */
    @ApiModelProperty("减免信息分配策略 true:表示分配至此")
    private Boolean isAdjustSplit;

    @ApiModelProperty("备注信息")
    private String remark;

}
