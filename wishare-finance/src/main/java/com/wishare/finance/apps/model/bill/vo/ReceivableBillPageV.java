package com.wishare.finance.apps.model.bill.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.domains.bill.entity.PayInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("应收账单分页列表")
public class ReceivableBillPageV extends BillPageV{

    @ApiModelProperty(value = "推凭状态 0-未推凭，1-已推凭")
    private Integer inferenceState;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("是否逾期：0未逾期，1已逾期")
    private Integer overdueState;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("优惠金额 (单位： 分)")
    private Long preferentialAmount;

    @ApiModelProperty("优惠退款金额 (单位： 分)")
    private Long preferentialRefundAmount;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("应收账单类型 1应收，3临时")
    private Integer billType;
    /**
     * 归属月（账期）
     */
    private LocalDate accountDate;

    /**
     * 调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否
     */
    private Integer isExact;

    private List<PayInfo> payInfos;

    private String payStr;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    @ApiModelProperty("应收日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate receivableDate;

    @ApiModelProperty("应收违约金")
    private Long receivableOverdueAmount = 0L;

    @ApiModelProperty("实收违约金")
    private Long actualPayOverdueAmount = 0L;

    @ApiModelProperty("减免违约金")
    private Long deductionOverdueAmount = 0L;

    @ApiModelProperty("未收违约金")
    private Long notReceivedOverdueAmount = 0L;

    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty("减免比率")
    private String adjustRatio;

    @ApiModelProperty("减免金额")
    private BigDecimal adjustAmount;

    @ApiModelProperty("减免提出人")
    private String adjustCreatorName;

    @ApiModelProperty("账单说明")
    private String description;

    @ApiModelProperty("冻结类型 1跳收 2代扣")
    private Integer freezeType;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

    @ApiModelProperty(value = "结算状态： 0-未结算， 1-结算中， 2-已结算")
    private Integer settlementStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer provisionVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer settlementVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer receiptConfirmationVoucherPushingStatus;

}
