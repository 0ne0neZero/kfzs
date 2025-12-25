package com.wishare.finance.apps.model.voucher.dto;

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
public class VoucherInferenceRecordDTO {

    /**
     * 推凭记录主键id
     */
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
    private Long eventType;

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
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;

}
