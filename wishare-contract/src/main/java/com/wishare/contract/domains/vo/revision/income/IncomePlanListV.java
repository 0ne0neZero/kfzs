package com.wishare.contract.domains.vo.revision.income;

import com.wishare.contract.infrastructure.utils.PropertyMsg;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新收款计划列表", description = "新收款计划列表")
public class IncomePlanListV extends Tree<IncomePlanListV,String> {
    /**---------------------------------------合同基础信息--------------------------------------------*/
    @ApiModelProperty("合同id")
    private String contractId;

    @PropertyMsg("区域")
    private String region;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同管理类别")
    private String conmanagetypename;

    @ApiModelProperty("合同管理类别")
    private String conmanagetype;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合同金额")
    private String contractAmountOriginalRate;



    @ApiModelProperty(value = "合同起始日期")
    private String gmtExpireStart;

    @ApiModelProperty(value = "合同结束日期")
    private String gmtExpireEnd;

    @ApiModelProperty("合同-合同履约状态")
    private String contractStatusName;

    @ApiModelProperty("合同-合同履约状态")
    private Integer status;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "结算周期名称")
    private String splitModeName;

    @ApiModelProperty(value = "客户名称")
    private String oppositeOne;

    @ApiModelProperty(value = "法定单位")
    private String ourPartyId;

    @ApiModelProperty(value = "法定单位")
    private String ourParty;

    @ApiModelProperty(value = "收费方式")
    private String payWay;

    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;

    @ApiModelProperty("合同业务线描述")
    private String contractBusinessLineName;
    /**---------------------------------------计划收款信息--------------------------------------------*/

    @ApiModelProperty("期数")
    private Integer termDate;

    @ApiModelProperty("收款计划分组")
    private String settlePlanGroup;

    @ApiModelProperty("收款计划编码")
    private String costEstimationCode;

    @ApiModelProperty("合同清单")
    private String serviceType;

    @ApiModelProperty("合同清单")
    private String serviceTypeName;

    @ApiModelProperty("费项名称")
    private String chargeItem;

    @ApiModelProperty("费项开始时间")
    private LocalDate costStartTime;

    @ApiModelProperty("费项结束时间")
    private LocalDate costEndTime;

    @ApiModelProperty("应收日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty("调整后应收金额")
    private BigDecimal adjustedReceivableAmount;

    @ApiModelProperty("收款金额")
    private BigDecimal receiptAmount;

    @ApiModelProperty("未收金额")
    private BigDecimal noReceiptAmount;

    @ApiModelProperty("减免金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty("调整金额")
    private BigDecimal adjustmentAmount;

    @ApiModelProperty("收款状态")
    private String receiptStaus;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "所属部门")
    private String departName;

    @ApiModelProperty(value = "所属部门")
    private String departId;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty("审核状态")
    private Integer reviewStatus;

    @ApiModelProperty("收款状态")
    private Integer paymentStatus;

    @ApiModelProperty("收款状态")
    private String paymentStatusName;

    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;

    @ApiModelProperty("计划状态")
    private Integer planStatus;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;

    @ApiModelProperty(value = "创建时间")
    private String gmtCreate;

    @ApiModelProperty(value = "审核状态")
    private Integer approveState;

    @ApiModelProperty(value = "审核状态")
    private String approveStateName;

    @ApiModelProperty(value = "是否可以编辑收款计划")
    private Boolean canEditPlan = true;

    @ApiModelProperty(value = "是否可以删除收款计划")
    private Boolean canDeletePlan = true;

    @ApiModelProperty("是否已经生成收入计划")
    private Boolean hasGeneratePayIncomePlan;

    @ApiModelProperty("入账状态 1:未入账 2:已入账")
    private Integer iriStatus;

}
