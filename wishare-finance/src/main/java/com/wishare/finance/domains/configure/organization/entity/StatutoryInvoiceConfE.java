package com.wishare.finance.domains.configure.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 法定单位电子发票设置表(StatutoryInvoiceConf)实体类
 *
 * @author makejava
 * @since 2022-11-01 14:48:51
 */
@Getter
@Setter
@TableName("statutory_invoice_conf")
public class StatutoryInvoiceConfE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 法定单位Id
     */
    private Long statutoryBodyId;
    /**
     * 法定单位名称
     */
    private String statutoryBodyName;
    /**
     * 机器编号（12位盘号）
     */
    private String machineCode;
    /**
     * 分机号
     */
    private Long extensionNumber;

    /**
     * 终端号
     */
    private Long terminalNumber;

    /**
     * 开票人
     */
    private String clerk;

    /**
     * 终端代码
     */
    private String terminalCode;

    /**
     * 用户代码
     */
    private String userCode;

    /**
     * 租户id
     */
    private String tenantId;

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

    /**
     * 诺诺token
     */
    @TableField(exist = false)
    private String token;

    /**
     * 自动加载主键id
     */
    public StatutoryInvoiceConfE() {
        generateIdentifier();
    }

    /**
     * 构造id
     */
    private void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("statutory_invoice_conf_id"));
        }
    }
}

