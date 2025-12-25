package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 对账单明细表,管理对账单单条账单的对账信息
 *
 * @author dxclay
 * @since 2022-10-13
 */
@Getter
@Setter
@ApiModel("对账账单信息")
public class ReconciliationBillDto {

    @ApiModelProperty(value = "账单id")
    private Long id;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    private String supCpUnitId;

    private String supCpUnitName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "应收金额")
    private Long receivableAmount = 0L;

    private Long noReductionAmount = 0L;

    @ApiModelProperty(value = "实收金额")
    private Long actualAmount = 0L;

    @ApiModelProperty("计费开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("计费结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("交易时间")
    private LocalDateTime tradeTime;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("退款金额")
    private Long refundAmount = 0L;

    @ApiModelProperty("结转金额")
    private Long carriedAmount;

    @ApiModelProperty("交易流水号")
    private String tradeNo;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("远洋对账专用字段:银行流水记录")
    private String bankFlowNo;

    @ApiModelProperty(value = "应收账单编号")
    private Long recId;
}
