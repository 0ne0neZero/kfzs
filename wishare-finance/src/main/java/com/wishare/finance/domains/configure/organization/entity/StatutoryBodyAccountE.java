package com.wishare.finance.domains.configure.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 法定单位银行账户表(StatutoryBodyAccount)实体类
 * 法定单位银行账户表(statutory_body_account)
 * @author makejava
 * @since 2022-08-11 15:10:34
 */
@Getter
@Setter
@TableName("statutory_body_account")
public class StatutoryBodyAccountE  {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 账户名称
     */
    private String name;
    /**
     * 账户类型：1.基本账户，2一般账户，3专用账户
     */
    private Integer type;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 开户行账号
     */
    private String bankAccount;
    /**
     * 收款付款类型：1.收款付款，2.收款，3.付款
     */
    private Integer recPayType;
    /**
     * 法定单位Id
     */
    private Long statutoryBodyId;
    /**
     * 法定单位名称
     */
    private String statutoryBodyName;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}

