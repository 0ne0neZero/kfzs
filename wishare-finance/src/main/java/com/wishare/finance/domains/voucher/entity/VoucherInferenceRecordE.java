package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/11 10:41
 * @version: 1.0.0
 */
@Setter
@Getter
@TableName("voucher_inference_record")
public class VoucherInferenceRecordE {

    /**
     * 推凭记录主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 触发事件类型
     */
    private Integer eventType;

    /**
     * 凭证规则id
     */
    private Long voucherRuleId;

    /**
     * 凭证规则名称
     */
    private String voucherRuleName;

    /**
     * 凭证系统
     */
    private String voucherSystem;

    /**
     * 推凭状态：0成功，1失败
     */
    private Integer successState;

    /**
     * 借方金额
     */
    private Long debitAmount;

    /**
     * 贷方金额
     */
    private Long creditAmount;

    /**
     * 该次推凭的凭证记录id集合
     */
    private String voucherIds;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

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
