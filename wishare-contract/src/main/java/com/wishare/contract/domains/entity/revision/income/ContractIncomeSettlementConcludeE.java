package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.ContractSettlementConcludeE;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:33
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_settlement")
public class ContractIncomeSettlementConcludeE extends ContractSettlementConcludeE {

    /**
     * id
     */
    @TableId(value = ID)
    private String id;

    private String pid;

    /**
     * 合同Id
     */
    private String contractId;

    /**
     * 结算单编号
     */
    private String payFundNumber;

    /**
     * 期数
     */
    private String termDate;

    /**
     * 客户id
     */
    private String customer;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 结算时间
     */
    private LocalDate plannedCollectionTime;

    /**
     * 结算金额
     */
    private BigDecimal plannedCollectionAmount;


    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 收票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 申请付款日期
     */
    private LocalDateTime paymentDate;


    /**
     * 付款方式 0 现金 1 银行 转帐 2 支票
     */
    private Integer paymentMethod;

    /**
     * 付款状态 0未结算  1已完成
     */
    private Integer paymentStatus;

    /**
     * 结算状态 0未结算  1已完成  2已完成
     */
    private Integer settleStatus;

    /**
     * 收票状态 0未完成  1已完成
     */
    private Integer invoiceStatus;

    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    private Integer reviewStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 结算单标题
     **/
    private String title;

    /**
     * 所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域
     **/
    private Integer belongRegion;

    /**
     * 结算类型 0中期确收 1最终确收
     **/
    private Integer settlementType;

    /**
     * 确收类别 0基础物管&非业主增值 1业主增值 2综合业务
     **/
    private Integer settlementClassify;


    /**
     * 所属层级 0项目 1区域公司
     **/
    private Integer belongLevel;

    /**
     * 实际结算金额
     **/
    private BigDecimal actualSettlementAmount;

    /**
     * 本期结算百分比
     **/
    private String currentSettleRatio;

    /**
     * 结算审批完成时间
     **/
    private LocalDateTime approveCompletedTime;

    /**
     * 累计结算金额
     **/
    private BigDecimal totalSettledAmount;

    /**
     * 累计结算百分比
     **/
    private String totalSettledRatio;

    /**
     * 项目类型 0：住宅项目 1：非住宅项目
     */
    private Integer communityType;

    /**
     * 合同id
     */
    public static final String ID = "id";
    /**
     * 父id
     **/
    public static final String PID = "pid";

    /**
     * 合同id
     */
    public static final String DELETED = "deleted";

    /**
     * 合同id
     */
    public static final String CONTRACTID = "contractId";
}
