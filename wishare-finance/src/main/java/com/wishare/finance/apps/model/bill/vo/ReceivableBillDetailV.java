package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("应收账单详情")
public class ReceivableBillDetailV extends BillDetailV {

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("是否逾期：0未逾期，1已逾期")
    private Integer overdueState;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("扣款金额")
    private Long deductionAmount;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty(value = "应收日")
    private Integer receivableDay;

    @ApiModelProperty("计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积")
    private Integer billArea;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;

    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue;

}
