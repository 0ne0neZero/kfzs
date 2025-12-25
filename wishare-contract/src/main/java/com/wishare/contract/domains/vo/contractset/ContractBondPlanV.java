package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 合同保证金计划信息
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractBondPlanV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("招投标保证金")
    private Boolean bidBond;
    @ApiModelProperty("计划收款时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划收款金额（原币）")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("本币金额（元）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("责任部门名称")
    private String orgName;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("结转状态 0 未结转  1已结转")
    private Integer settleTransferStatus;
    @ApiModelProperty("结转金额")
    private BigDecimal settleTransferAmount;
    @ApiModelProperty("中台临时账单id")
    private Long billId;
    @ApiModelProperty("中台临时账单编号（招投标保证金才有）")
    private String billNo;
    @ApiModelProperty("已收/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("已收/退款金额")
    private BigDecimal refundAmount;
    @ApiModelProperty("退款状态/收款状态 0未收/退  1部分收/退  2已收/退")
    private Integer refundStatus;
    @ApiModelProperty("已开收据金额")
    private BigDecimal receiptAmount;

    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
}
