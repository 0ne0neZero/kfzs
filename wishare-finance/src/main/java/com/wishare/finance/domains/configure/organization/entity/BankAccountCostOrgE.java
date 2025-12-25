package com.wishare.finance.domains.configure.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 银行账户与成本中心关联实体类
 *
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
@TableName("bank_account_cost_org")
public class BankAccountCostOrgE {

    /**
     * 成本中心id
     */
    private Long costOrgId;
    /**
     * 银行账户id
     */
    private Long bankAccountId;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String operatorName;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtModify;

}
