package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 支付申请单(PaymentApplicationForm)实体类
 *
 * @author
 * @since 2025-01-07 17:05:22
 */
@Getter
@Setter
@TableName(value = TableNames.PAYMENT_APPLICATION_FORM_ZJ, autoResultMap = true)
public class PaymentApplicationFormZJ extends BaseEntity {
    private static final long serialVersionUID = 727654670053855436L;
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     *  合同系统-结算单id+名称
     */
    private String settlementInfo;
    /**
     * 期望付款日期
     */
    private LocalDate expectPayDate;
    /**
     * 支付申请单应付金额
     */
    private BigDecimal totalPaymentAmount;
    /**
     * 业务类型 默认：其它业务
     */
    private String businessType;
    /**
     * 业务事由
     */
    private String businessReasons;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 收款人名称(往来单位对应账户ID)
     */
    private String recipient;
    /**
     * 往来单位主数据编号BP开头
     */
    private String recipientCode;
    /**
     * 收款人名称
     */
    private String recipientName;

    /**
     * 收款账户id(往来单位对应账户ID)
     */
    private String account;

    /**
     * 往来单位主数据编号BP开头(账户)
     */
    private String accountCode;
    /**
     * 收款账户名称
     */
    private String nameOfReceivingAccount;
    /**
     * 银行账号
     */
    private String bankAccountNumber;
    /**
     * 开户行
     */
    private String openingBank;

    /**
     * 单位code
     * */
    private String unitCode;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 部门名称
     */
    private String departName;
    /**
     * 单据日期
     */
    private LocalDateTime billDate;
    /**
     * 核算组织
     */
    private String org;
    /**
     * 附件张数
     */
    private Integer attachmentNum;
    /**
     * 审批状态
     * 1:草稿 2:待审批 3:完成审批
     */
    private Integer approvalStatus;

    /**
     * 支付申请单编号
     */
    private String payApplyCode;;

    /**
     * 支付状态
     * 1:未支付 2已支付
     */
    private Integer payStatus;


    /**
     * 支付时间
     */
    private LocalDateTime payDate;
    /**
     * 经办人
     */
    private String handledBy;

    /**
     * 现金流量
     */
    private String cashFlow;

    /**
     * 结算方式
     */
    private String paymentMethod;

    /**
     * 付款账户名称
     */
    private String nameOfPayAccount;
    /**
     * 付款银行账号
     */
    private String payBankAccountNumber;
    /**
     * 付款开户行
     */
    private String payOpeningBank;
    /**
     * 票据支付方式
     */
    private String paymentMethodForBills;

    /**
     * 票据数量
     */
    private String billsNumbers;

    /**
     * 付款详细说明
     */
    private String payDesc;

    /**
     * 转账附言
     */
    private String transferRemarks;

    /**
     * 推送交建通状态
     */
    private Integer pushJjtStatus;
    /**
     * 项目id
     */
    private String communityId;
    /**
     * 项目名称
     */
    private String communityName;
    /**
     * 合同CT码
     */
    private String conmaincode;
    /**
     * 供应商名称
     */
    private String oppositeOne;
    /**
     * 供应商id
     */
    private String oppositeOneId;
    /**
     * 区域
     */
    private String region;

    private String conmanagetypename;
    /**
     * 合同金额
     */
    private BigDecimal contractAmountOriginalRate;
    /**
     * 审批通过日期
     */
    private LocalDateTime approvalDate;
    /**
     * 付款账户id
     */
    private String payAccountId;
    /**
     * 现金流量名称
     */
    private String cashFlowName;
    /**
     * 结算方式名称
     */
    private String paymentMethodName;

    /**
     * 收款银行名称
     */
    private String beneficiaryBank;
    /**
     * 收款账户对应的银行id
     */
    private String bankIdAccount;

    /**
     * 信息台ID
     */
    private String bpmProcessId;
    /**
     * 信息台ID发起人
     */
    private String bpmProcessIdFq;
    /**
     * 推送部门code
     */
    private String externalDepartmentCode;

}

