package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 支付申请单-款项明细(PaymentApplicationFormKxmx)实体类
 *
 * @since 2025-01-10 17:36:52
 */
@Getter
@Setter
@TableName(value = TableNames.PAYMENT_APPLICATION_FORM_KXMX, autoResultMap = true)
public class PaymentApplicationFormKxmx extends BaseEntity {
    private static final long serialVersionUID = 314304426079833324L;
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 应收应付id
     **/
    private String ftId;

    /**
     * 核销应付编号
     */
    private String heyfbh;
    /**
     * 未核销金额(原币)
     */
    private BigDecimal writeOffInfoAmount;
    /**
     * 业务科目
     */
    private String subjectName;

    /**
     * 业务科目编码
     */
    private String subjectCode;
    /**
     * 应收应付摘要
     */
    private String summary;
    /**
     * 资金计划编号
     */
    private String fundingPlanNumber;
    /**
     * 预算科目名称
     */
    private String budgetAccountName;
    /**
     * 结算方式
     */
    private String payWay;
    /**
     * 合计金额
     */
    private BigDecimal amount;
    /**
     * 到期日期
     */
    private LocalDate dueDate;
    /**
     * 支付申请单id
     */
    private String payAppId;

}

