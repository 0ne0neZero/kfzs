package com.wishare.finance.apps.model.report.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分页查询预收率统计表报表返回数据
 * @author yancao
 */
@Setter
@Getter
@ApiModel("分页查询预收率统计表报表返回数据")
public class AdvanceRateReportPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("区域")
    private String region;

    @ApiModelProperty("项目群")
    private String projectGroup;

    @ApiModelProperty("组织id")
    private Long costCenterId;

    @ApiModelProperty("组织代码")
    private String costCenterCode;

    @ApiModelProperty("组织名称")
    private String costCenterName;

    @ApiModelProperty("楼盘名称")
    private String communityName;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("房屋id")
    private Long roomId;

    @ApiModelProperty("房屋名称")
    private String roomName;

    @ApiModelProperty("管家姓名")
    private String housekeeperName;

    @ApiModelProperty("计费面积")
    private BigDecimal chargingArea;

    @ApiModelProperty("建筑面积")
    private BigDecimal buildingArea;

    @ApiModelProperty("业主名称")
    private String ownerName;

    @ApiModelProperty("开票类别")
    private List<String> invoiceTypeList;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("饱和应收")
    private Long totalAmount;

    @ApiModelProperty("实收")
    private Long actualPayAmount;

    @ApiModelProperty("未收")
    private Long actualUnPayAmount;

    @ApiModelProperty("预收率")
    private BigDecimal advanceRate;

    @ApiModelProperty("收款日期")
    private LocalDateTime chargeTime;

}
