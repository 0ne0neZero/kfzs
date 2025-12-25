package com.wishare.contract.domains.vo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.wishare.starter.beans.Tree;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/14:01
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同付款计划表视图对象", description = "收入合同付款计划表视图对象")
public class ContractIncomePlanConcludeV extends Tree<ContractIncomePlanConcludeV,String> {

//    @ApiModelProperty("主键")
//    private String id;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("编码名称")
    private String nameNo;
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty("客户id")
    private String customer;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("付款计划编号")
    private String payNotecode;
    @ApiModelProperty("期数")
    private Integer termDate;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("未付金额")
    private BigDecimal noPaymentAmount;
    @ApiModelProperty("结算状态")
    private Integer paymentStatus;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("收款金额")
    private BigDecimal receiptAmount;
    @ApiModelProperty("收款状态")
    private String receiptStaus;
    @ApiModelProperty("未收金额")
    private BigDecimal noReceiptAmount;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("未计划金额")
    private BigDecimal noPlanAmount;
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;
    @ApiModelProperty("创建人")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;
    @ApiModelProperty("第几批")
    private Integer howOrder;
    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;
    @ApiModelProperty("服务类型")
    private Integer serviceType;
    @ApiModelProperty("费项名称")
    private String chargeItem;
    @ApiModelProperty("费项")
    private String chargeItemId;
    @ApiModelProperty("费项ID数组")
    private List<Long> chargeItemIdList;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("拆分方式")
    private Integer splitMode;
    @ApiModelProperty("已付款金额")
    private BigDecimal ssettlementAmount;
    @ApiModelProperty("已结算金额")
    private BigDecimal sreceiptAmount;
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    @ApiModelProperty("收入状态")
    private Integer acceptStatus;
    @ApiModelProperty("账单id")
    private Long billId;
    @ApiModelProperty("账单编号")
    private String billNo;
    @ApiModelProperty("项目ID")
    private String communityId;
    @ApiModelProperty("计费周期")
    private String timeRanges;
    @ApiModelProperty("计费周期前端")
    private List<String> timeRange;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("合同总额 原币含税")
    private BigDecimal contractAmountOriginalRate;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("合同变更后金额")
    private BigDecimal changContractAmount;

    @ApiModelProperty("我方单位id")
    private String ourPartyId;

    @ApiModelProperty("我方单位名称")
    private String ourParty;

    @ApiModelProperty("对方单位-1")
    private String oppositeOne;

    @ApiModelProperty("对方单位id-1")
    private String oppositeOneId;

    @ApiModelProperty("对方单位-2")
    private String oppositeTwo;

    @ApiModelProperty("对方单位id-2")
    private String oppositeTwoId;

    @ApiModelProperty("签约单位信息")
    private String qydws;

    @ApiModelProperty("合同结算金额总额")
    private BigDecimal contractTotalSettlementAmount;
    @ApiModelProperty("是否提示合同修正后未编辑收款计划")
    private Boolean isIncomeCorrection = Boolean.FALSE;
    //是否修正后编辑收款计划（1.是，0.否）
    private Integer isCorrectionAndPlan;
    @ApiModelProperty("是否含有未推送合同：false.否；true.是")
    private Boolean isHaveNoPushedContract = Boolean.FALSE;

}
