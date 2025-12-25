package com.wishare.finance.apps.model.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 收缴率分页返回数据
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收缴率分页返回数据")
public class ChargeCollectionRateReportPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

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

    @ApiModelProperty("开票类别")
    private List<String> invoiceTypeList;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("组织代码")
    private String costCenterCode;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

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

    @ApiModelProperty("前期应收")
    private Long earlyReceivableAmount;

    @ApiModelProperty("前期应收减免")
    private Long earlyDeductibleAmount;

    @ApiModelProperty("前期实收")
    private Long earlyActualPayAmount;

    @ApiModelProperty("前期实收减免")
    private Long earlyDiscountAmount;

    @ApiModelProperty("前期未收")
    private Long earlyActualUnPayAmount;

    @ApiModelProperty("前期收缴率")
    private BigDecimal earlyCollectionRate;

    @ApiModelProperty("总和应收")
    private Long sumReceivableAmount;

    @ApiModelProperty("总和实收")
    private Long sumActualPayAmount;

    @ApiModelProperty("总和收缴率")
    private BigDecimal sumCollectionRate;
}
