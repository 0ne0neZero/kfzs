package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_plan_fxm_receipt_record")
public class ContractIncomeConcludePlanFxmReceiptRecordE {

    @TableId(value = "id")
    private String id;

    /**
     * 计划所属类型
     */
    private Integer planType;

    /**
     * 合同id
     */
    private String agreementId;

    /**
     * 合同编号
     */
    private String agreementNo;

    /**
     * 应收账单id，对应收款计划id
     */
    private String agreementBillId;

    /**
     * 实收日期
     */
    private LocalDate finishApproveTime;

    /**
     * 收款凭证图片地址
     */
    private String certUrl;

    /**
     * 发票类型 0:未开票 1:已开票 2:不需要开票
     */
    private Integer invoiceType;

    /**
     * 收款金额, 以分计算
     */
    private BigDecimal payAmount;

    /**
     * 收款方式 1:刷卡 2:微信 3:支付宝 4:支票 5:转账 6:银行托收
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 推送状态 0:失败 1:成功
     */
    private Integer pushStatus;

    /**
     * 推送信息
     */
    private String pushMessage;

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
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 更新人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除 0:未删除 1:已删除
     */
    @TableLogic
    private Integer deleted;

}


