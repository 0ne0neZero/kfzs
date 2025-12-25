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
@TableName("contract_income_conclude_plan_fxm_record")
public class ContractIncomeConcludePlanFxmRecordE {

    @TableId(value = "id")
    private String id;

    /**
     * 推送类型
     */
    private Integer pushType;

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
     * 项目PJ码
     */
    private String projectCode;

    /**
     * 应收金额，以分计数
     */
    private BigDecimal amount;

    /**
     * 我司分成比例
     */
    private BigDecimal companyRate;

    /**
     * 业主分层比例
     */
    private BigDecimal ownerRate;

    /**
     * 末级费项名称
     */
    private String feeName;

    /**
     * 末级费项id
     */
    private Long feeNameConfigInfoId;

    /**
     * 计划收款日期
     */
    private LocalDate firstStartDate;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 收款周期类型，对应收费方式
     */
    private Integer fundPeriodType;

    /**
     * 收入关系 0:计收入类 1:不计入收入类
     */
    private Integer incomeType;

    /**
     * 分成类型 0:按收入 1:按受益 2:不分成
     */
    private Integer percentType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 推送结果
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


