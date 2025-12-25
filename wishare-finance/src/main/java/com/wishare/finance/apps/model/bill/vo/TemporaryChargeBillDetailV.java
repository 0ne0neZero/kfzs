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
@ApiModel("临时收费账单详情")
public class TemporaryChargeBillDetailV  extends BillDetailV{

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "是否引用：0未被引用，1已被引用")
    private Integer referenceState;

    @ApiModelProperty(value = "收付类型：0收款（临时收款），1付款（临时付款）")
    private String payType;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积")
    private BigDecimal chargingArea;

    @ApiModelProperty("计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积")
    private Integer billArea;

    @ApiModelProperty("计费数量")
    private Integer chargingCount;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("扣款金额")
    private Long deductionAmount;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty(value = "应收日")
    private LocalDate receivableDate;
}
