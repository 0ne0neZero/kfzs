package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Setter
@Getter
@ApiModel("收付款记录")
public class GatherAndPayDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("收费单元名称")
    private String cpUnitId;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式")
    private Integer payWay;

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("收付款类型（gather 收款 pay 付款）")
    private String gatherAndPayType;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

}
