package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预收账单审核详情
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("预收账单审核详情")
public class AdvanceBillApplyDetailV extends BillApplyDetailV{

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据")
    private String invoiceType;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;


}
