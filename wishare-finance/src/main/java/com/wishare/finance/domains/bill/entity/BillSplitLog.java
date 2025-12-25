package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 账单费项拆分记录表(bill_split_log)
 *
 * @author admin
 * @since 2025-02-18
 */
@Getter
@Setter
@TableName(value = TableNames.BILL_SPLIT_LOG, autoResultMap = true)
@Accessors(chain = true)
public class BillSplitLog {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 拆分状态 1成功，2 失败
     */
    private Integer state;


    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
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
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}
