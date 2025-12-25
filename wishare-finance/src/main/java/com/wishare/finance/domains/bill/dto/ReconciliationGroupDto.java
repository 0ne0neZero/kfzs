package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("对账分组信息")
public class ReconciliationGroupDto {

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("成本中心id")
    private String costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("收款账号id")
    private String sbAccountId;

    @ApiModelProperty("支付渠道")
    private String payChannel;

    @ApiModelProperty(value = "支付方式")
    private String payWay;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

}
