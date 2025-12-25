package com.wishare.contract.domains.vo.revision.income.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单列表视图对象V2", description = "结算单列表视图对象V2")
public class ContractIncomeSettlementPageV2 extends Tree<ContractIncomeSettlementPageV2,String> {

    /**************
     * 合同视图-相关 *
     **************/
    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同-区域")
    private String contractRegion;

    @ApiModelProperty("合同-项目id")
    private String communityId;

    @ApiModelProperty("合同-项目名称")
    private String communityName;

    @ApiModelProperty("合同-合同编码")
    private String contractNo;

    @ApiModelProperty("合同-合同名称")
    private String contractName;

    @ApiModelProperty("合同-合同管理类别-合同管理类别编码")
    private String conManageType;

    @ApiModelProperty("合同-合同管理类别-合同业务分类")
    private String contractCategoryName;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private Integer contractServeType;

    @ApiModelProperty("合同-客户")
    private String customer;

    @ApiModelProperty("合同-客户名称")
    private String customerName;

    @ApiModelProperty("合同-合同金额")
    private BigDecimal contractAmount;

    @ApiModelProperty("合同-合同变更后金额")
    private BigDecimal contractChangeAmount;

    @ApiModelProperty("合同-合同起止日期-合同开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date contractStartDate;

    @ApiModelProperty("合同-合同起止日期-合同结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date contractEndDate;

    @ApiModelProperty("合同-合同开始/结算时间拼接，格式:yyyy-MM-dd~yyyy-MM-dd")
    private String contractStartEndDisplay;

    @ApiModelProperty("合同-合同履约状态")
    private Integer contractStatusCode;

    @ApiModelProperty("合同-合同履约状态名称")
    private String contractStatusName;

    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;

    @ApiModelProperty("合同业务线描述")
    private String contractBusinessLineName;

    @ApiModelProperty("合同-结算周期-清单的付款方式")
    private String splitMode;

    @ApiModelProperty("合同-结算周期名称")
    private String splitModeName;

    /**
     * 1级、2级相同内容
     **/
    @ApiModelProperty("计划收款金额")
    private BigDecimal rangeAmountPayable;

    @ApiModelProperty("已发生已确认")
    private BigDecimal rangeSettledAmount;


    /**
     * 已发生未确收
     **/



    @ApiModelProperty("已发生未确收-未确收金额")
    private BigDecimal rangeUnsettledAmount;

    @ApiModelProperty("V2.15-应确收金额")
    private BigDecimal rangeToSettleAmountFromSettlement;

    @ApiModelProperty("V2.15-减免金额")
    private BigDecimal rangeDeductAmountFromSettlement;

    @ApiModelProperty("V2.15-实际确收金额")
    private BigDecimal rangeActualSettleAmountFromSettlement;

    @ApiModelProperty("已发生未确收-未确收周期-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date rangeUnsettledStartDate;

    @ApiModelProperty("已发生未确收-未确收周期-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date rangeUnsettledEndDate;

    @ApiModelProperty("已发生未确收-未结算周期开始/结束时间拼接，格式:yyyy-MM-dd~yyyy-MM-dd")
    private String rangeUnsettledStartEndDisplay;

    @ApiModelProperty("已发生未确收-期数合计")
    private Integer rangeUnsettledTermDate;

    /**
     * 确收单信息
     **/
    @ApiModelProperty("确收单信息-确收单编号")
    private String payFundNumber;

    @ApiModelProperty("确收单信息-收款计划金额")
    private BigDecimal amountPayable;

    @ApiModelProperty("确收单信息-减免金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty("确收单信息-实际确收金额")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty("确收单信息-计量周期")
    private String periodsStr;

    @ApiModelProperty("确收单信息-成本预估使用期数")
    private String termDateStr;

    @ApiModelProperty("结算状态 0未结算  1已完成  2已完成")
    private Integer settleStatus;

    @ApiModelProperty("结算状态 0未结算  1已完成  2已完成")
    private String settleStatusName;

    @ApiModelProperty("确收单信息-确收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date approveCompletedTime;

    @ApiModelProperty("确收单信息-确收单状态-原审核状态")
    private Integer reviewStatus;

    @ApiModelProperty("确收单信息-确收单状态名称")
    private String reviewStatusName;

    @ApiModelProperty("结算单信息-步骤")
    private Integer step;

    @ApiModelProperty("结算单信息-创建人")
    private String creator;

    @ApiModelProperty("结算单信息-创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;


    /**
     * 收款信息
     * todo 枚举新增字段名称
     **/
    @ApiModelProperty("收款信息-收款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("收款信息-收款状态")
    private Integer paymentStatus;

    @ApiModelProperty("收款信息-收款状态名称")
    private String paymentStatusName;

    /**
     * 辅助处理,不用管
     **/
    private String planIdStr;
}
