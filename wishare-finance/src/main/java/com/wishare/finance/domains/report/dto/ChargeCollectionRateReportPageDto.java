package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收缴率分页数据
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收缴率分页数据")
public class ChargeCollectionRateReportPageDto {

    @ApiModelProperty("票据类型")
    private String invoiceType;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("房屋id")
    private Long roomId;

    @ApiModelProperty("房屋名称")
    private String roomName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("计费面积")
    private BigDecimal chargingArea;

    @ApiModelProperty("业主名称")
    private String ownerName;

    @ApiModelProperty("成本中心id")
    private String costCenterId;

    @ApiModelProperty("组织代码")
    private String orgCode;

    @ApiModelProperty("饱和应收")
    private Long totalAmount;

    @ApiModelProperty("应收减免")
    private Long deductibleAmount;

    @ApiModelProperty("实收")
    private Long actualPayAmount;

    @ApiModelProperty("实收减免")
    private Long discountAmount;

    @ApiModelProperty("未收")
    private Long actualUnPayAmount;

    @ApiModelProperty("收缴率")
    private BigDecimal collectionRate;

    @ApiModelProperty("收款截止日期")
    private LocalDateTime chargeTime;

}
