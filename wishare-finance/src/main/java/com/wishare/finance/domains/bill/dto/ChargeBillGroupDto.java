package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/1/4
 * @Description:
 */
@Setter
@Getter
@ApiModel("账单业务信息合计")
public class ChargeBillGroupDto {

    @ApiModelProperty("账单ids")
    private String billIds;

    @ApiModelProperty("结算状态")
    private String settleStates;

    @ApiModelProperty("房号ID")
    private String roomIds;

    @ApiModelProperty("房号名称")
    private String roomNames;

    @ApiModelProperty("项目ID")
    private String communityIds;

    @ApiModelProperty("项目名称")
    private String communityNames;

    @ApiModelProperty("法定单位ids")
    private String statutoryBodyIds;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("费项Id")
    private String chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount;

    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmount;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmount;
    @ApiModelProperty("结算金额 (单位： 分)")
    private Long settleAmount;
    @ApiModelProperty("结转金额")
    private Long carriedAmount;
    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    public Long getActualUnpayAmount() {
        return receivableAmount + overdueAmount - discountAmount + refundAmount + carriedAmount - settleAmount;
    }

    public Long getActualPayAmount() {
        return settleAmount - refundAmount - carriedAmount;
    }

}
