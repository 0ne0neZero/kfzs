package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 合同流水账单返回信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("合同流水账单返回信息")
public class ContractFlowBillV {

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("收款人")
    private String payeeName;

    @ApiModelProperty("发生时间")
    private LocalDateTime payTime;

    @ApiModelProperty("系统来源")
    private Integer sysSource;
}
