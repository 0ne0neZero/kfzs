package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.GatherDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yyx
 * @date 2023/6/13 15:28
 */
@Setter
@Getter
@ApiModel("收款记录")
public class GatherDto {

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

    @ApiModelProperty("收费单元ID")
    private String cpUnitId;

    @ApiModelProperty("收付款类型（gather 收款 pay 付款）")
    private String gatherAndPayType;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式")
    private Integer payWay;

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("收款明细")
    List<GatherDetail> gatherDetails;

    @ApiModelProperty("收款时间")
    private LocalDateTime payTime;

}
