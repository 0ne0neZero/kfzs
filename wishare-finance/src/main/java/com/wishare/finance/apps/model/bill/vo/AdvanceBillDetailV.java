package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("预收账单详情")
public class AdvanceBillDetailV extends BillDetailV {

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("预收时间")
    private LocalDateTime payTime;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

}
