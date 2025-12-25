package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ChargeDeductionDetailPageV {

    @ApiModelProperty(value = "账单id")
    private String billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty("项目ID")
    private String communityId;

    /**
     * 账单类型（1:应收账单，2:预收账单）
     */
    private Integer type;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty(value = "房号")
    private String roomName;

    @ApiModelProperty(value = "账单金额（单位：元）")
    private BigDecimal totalAmount;

    @ApiModelProperty("已减免金额")
    private BigDecimal alreadyReducedAmount = BigDecimal.ZERO;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "归属月")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM")
    private LocalDate accountDate;

    @ApiModelProperty(value = "本次减免金额")
    private BigDecimal adjustAmount;

}
