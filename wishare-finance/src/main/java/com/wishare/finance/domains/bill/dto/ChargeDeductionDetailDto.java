package com.wishare.finance.domains.bill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChargeDeductionDetailDto {

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


    @ApiModelProperty("删除状态：0 未删除，1 已删除")
    private Byte deleted;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名字")
    private String creatorName;

    @ApiModelProperty("操作人id")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty(value = "本次减免金额")
    private BigDecimal adjustAmount;

}
