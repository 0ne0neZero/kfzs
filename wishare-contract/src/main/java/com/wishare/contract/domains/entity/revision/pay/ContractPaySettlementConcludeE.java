package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.ContractSettlementConcludeE;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("contract_pay_conclude_settlement")
public class ContractPaySettlementConcludeE extends ContractSettlementConcludeE {

    /**
     * id
     */
    @TableId(value = ID)
    private String id;

    /**
     * 合同Id
     */
    private String contractId;

    /**
     * 父id
     */
    private String pid;

    /**
     * 合同付款计划Id
     */
    private String contractPlanId;

    /**
     * 供应商id
     */
    private String merchant;

    /**
     * 供应商名称
     */
    private String merchantName;

    /**
     * 结算单编号
     */
    private String payFundNumber;

    /**
     * 期数
     */
    private String termDate;

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
     * 结算开始时间
     */
    private LocalDate startTime;

    /**
     * 结算结束时间
     */
    private LocalDate endTime;

    /**
     * 结算周期
     */
    private String settleCycle;

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
     * 结算单标题
     **/
    private String title;

    /**
     * 所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域
     **/
    private Integer belongRegion;

    /**
     * 结算类型 0中期结算 1最终结算
     **/
    private Integer settlementType;

    /**
     * 结算分类 0工程类 1秩序类(保安) 2环境类(保洁、绿化、垃圾清运、不含化粪池清掏) 3消防维保 4电梯维保 5设备设施维修 6案场类 7智能化、信息化 8房屋租赁类 9劳务派遣类 10增值类 11猎头委托、培训类 12其他
     **/
    private Integer settlementClassify;

    /**
     * 增值类 0空间运营 1到家服务 2零售业务 3美居业务 4资产业务 5餐饮业务 6业态运营
     **/
    private Integer additionType;

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
     * 累计应结算金额
     **/
    private BigDecimal totalSettlementAmount;

    /**
     * 项目类型 0：住宅项目 1：非住宅项目
     */
    private Integer communityType;

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

    /**
     * 合同id
     */
    public static final String TERMDATE = "termDate";

    /**
     * 支付申请单 支付状态  1:未支付 2:支付中 3:已支付
     */
    private Integer applyStatus;

    //其他附件-业务事由
    private String otherBusinessReasons;

    //推送部门code
    private String externalDepartmentCode;
    /**
     * 原合同计划ID，NK使用
     */
    private String mainId;
    //计税方式（1.一般计税，2.简单计税）
    private Integer calculationMethod;
}
