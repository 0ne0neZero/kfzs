package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收费减免分页返回参数
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收费减免分页返回参数")
public class ChargeReductionReportPageDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("房屋名称")
    private String roomName;

    @ApiModelProperty("减免项目")
    private String chargeItemName;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("减免方式")
    private String reductionType;

    @ApiModelProperty("减免单号")
    private String reductionNo;

    @ApiModelProperty("减免期间")
    private String reductionTime;

    @ApiModelProperty("应收金额")
    private Long receivableAmount;

    @ApiModelProperty("减免金额")
    private Long reductionAmount;

    @ApiModelProperty("减免比例")
    private BigDecimal reductionRatio;

    @ApiModelProperty("审批状态")
    private Integer approvedState;

    @ApiModelProperty("审批通过日期")
    private LocalDateTime approvedTime;

    @ApiModelProperty("流程发起日期")
    private LocalDateTime initiationTime;

    @ApiModelProperty("原因分类")
    private String reductionReason;

    @ApiModelProperty("具体说明")
    private String approvedRemark;

}
