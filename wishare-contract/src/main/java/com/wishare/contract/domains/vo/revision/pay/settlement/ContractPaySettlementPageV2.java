package com.wishare.contract.domains.vo.revision.pay.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
public class ContractPaySettlementPageV2 extends Tree<ContractPaySettlementPageV2,String> {

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

    @ApiModelProperty("合同-合同管理类别-合同业务分类")
    private String contractCategoryName;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private Integer contractServeType;

    @ApiModelProperty("合同-供应商")
    private String merchant;

    @ApiModelProperty("合同-供应商名称")
    private String merchantName;

    @ApiModelProperty("合同-合同金额")
    private BigDecimal contractAmount;

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

    @ApiModelProperty("V2.15-结算计划金额(原合同-应结算金额)-仅改名")
    private BigDecimal rangeAmountPayable;


    /**
     * 应结算未结算
     **/


    @ApiModelProperty("应结算未结算-已发生已结算")
    private BigDecimal rangeSettledAmount;

    @ApiModelProperty("应结算未结算-未结算金额")
    private BigDecimal rangeUnsettledAmount;

    @ApiModelProperty("V2.15-应结算金额")
    private BigDecimal rangeToSettleAmountFromSettlement;

    @ApiModelProperty("V2.15-扣款金额")
    private BigDecimal rangeDeductAmountFromSettlement;

    @ApiModelProperty("V2.15-实际结算金额")
    private BigDecimal rangeActualSettleAmountFromSettlement;


    @ApiModelProperty("应结算未结算-未结算周期-开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date rangeUnsettledStartDate;

    @ApiModelProperty("应结算未结算-未结算周期-结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date rangeUnsettledEndDate;

    @ApiModelProperty("应结算未结算-未结算周期开始/结束时间拼接，格式:yyyy-MM-dd~yyyy-MM-dd")
    private String rangeUnsettledStartEndDisplay;

    @ApiModelProperty("应结算未结算-期数合计")
    private Integer rangeUnsettledTermDate;

    /**
     * 结算单信息
     **/
    @ApiModelProperty("结算单信息-结算单标题")
    private String title;

    @ApiModelProperty("结算单信息-结算单编号")
    private String payFundNumber;

    @ApiModelProperty("结算单信息-结算单编号(截取倒数第二个“-”之后数据)")
    private String payFundNumberCapture;

    @ApiModelProperty("结算类型")
    private Integer settlementType;

    @ApiModelProperty("结算类型名称")
    private String settlementTypeName;

    @ApiModelProperty("结算单信息-应结算金额")
    private BigDecimal amountPayable;

    @ApiModelProperty("结算单信息-扣款金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty("结算单信息-实际结算金额")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty("结算单信息-累计结算金额")
    private BigDecimal totalSettledAmount;

    @ApiModelProperty("结算单信息-计量周期")
    private String periodsStr;

    @ApiModelProperty("结算单信息-成本预估使用期数")
    private String termDateStr;

    @ApiModelProperty("结算单信息-结算单状态-原审核状态")
    private Integer reviewStatus;

    @ApiModelProperty("结算单信息-结算单状态名称")
    private String reviewStatusName;

    @ApiModelProperty("结算单信息-步骤")
    private Integer step;

    @ApiModelProperty("结算单信息-创建人")
    private String creator;

    @ApiModelProperty("结算单信息-创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

    @ApiModelProperty("结算单信息-结算开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结算单信息-结算结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("结算单信息-结算审批完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveCompletedTime;
    /**
     * 收票信息
     **/
    @ApiModelProperty("收票信息-收票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("收票信息-收票状态")
    private Integer invoiceStatus;

    @ApiModelProperty("收票信息-收票状态名称")
    private String invoiceStatusName;

    /**
     * 付款信息
     **/
    @ApiModelProperty("付款信息-付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("付款信息-付款方式")
    private Integer paymentMethod;

    @ApiModelProperty("付款信息-付款方式名称")
    private String paymentMethodName;

    @ApiModelProperty("付款信息-付款状态")
    private Integer paymentStatus;

    @ApiModelProperty("付款信息-付款状态名称")
    private String paymentStatusName;

    /**
     * 辅助处理,不用管
     **/
    private String planIdStr;
}
