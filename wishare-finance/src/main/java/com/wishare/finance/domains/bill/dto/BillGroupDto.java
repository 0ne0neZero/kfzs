package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class BillGroupDto {


    @ApiModelProperty("业务单元id")
    private Long businessUnitId;

    @ApiModelProperty("业务单元名称")
    private String businessUnitName;

    @ApiModelProperty(value = "开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty(value = "推凭状态 0-未推凭，1-已推凭")
    private Integer inferenceState;
    /**
     * 账单id
     */
    private String billIds;

    /**
     * 账单类型
     */
    private String billTypes;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 付款方方类型（0:业主，1开发商，2租客，3客商，4法定单位）
     */
    private Integer payerType;

    /**
     * 收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）
     */
    private Integer payeeType;

    /**
     * 付款方手机号码
     */
    private String payerPhone;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 账单金额
     */
    private Long totalAmount;

    /**
     * 应收金额  (单位： 分)
     */
    private Long receivableAmount;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;

    /**
     * 应收减免金额  (单位： 分)
     */
    private Long deductibleAmount;

    /**
     * 违约金金额 (单位： 分)
     */
    private Long overdueAmount;

    /**
     * 实收减免金额 (单位： 分)
     */
    private Long discountAmount;

    /**
     * 结算金额
     */
    private Long settleAmount;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 账单来源
     */
    private String source;
    /**
     * 分组的年份
     */
    private String billYear;

    /**
     * 实际缴费金额
     */
    private Long actualPayAmount;

    /**
     * 实际应缴金额
     */
    private Long actualUnpayAmount;

    /**
     * 账单归属年
     */
    private Integer accountYear;

    /**
     * 账单归属月
     */
    private String accountDate;

    /**
     * 账单归属季度
     */
    private Integer accountQuarter;

    /**
     * 账单归属半年度
     */
    private Integer accountPartYear;

    /**
     * 缴费周期 1-月 2-季度 3-半年度 4-年
     */
    private Integer settleType;

    /**
     * 费项id
     */
    private String chargeItemIds;

    //明细列表
    List<AllBillGroupDto> children;

    /**
     * 是否合并费项 1-是 2-否
     */
    private Integer isMerge;

    /**
     * 移动端展示样式1-费项2-时间
     */
    private Integer showType;

    /**
     * 缴费周期 1-月 2-季度 3-半年度 4-年
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

}
