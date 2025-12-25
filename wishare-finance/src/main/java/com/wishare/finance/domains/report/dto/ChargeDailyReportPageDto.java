package com.wishare.finance.domains.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收费日报分页返回参数
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收费日报分页返回参数")
public class ChargeDailyReportPageDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("楼栋名称")
    private String buildingName;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("业主名称")
    private String ownerName;

    @ApiModelProperty("单据编号")
    private String billNo;

    @ApiModelProperty("票据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("收款人")
    private String payeeName;

    @ApiModelProperty("收款方式")
    private String payChannel;

    @ApiModelProperty("收费项目")
    private String chargeItemName;

    @ApiModelProperty("金额")
    private Long payAmount;

    @ApiModelProperty("备注")
    private String remark;
}
