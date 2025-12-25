package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("推凭记录")
public class VoucherInferenceRecordV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    /**
     * 推凭记录主键id
     */
    @ApiModelProperty("推凭记录主键id")
    private Long id;

    /**
     * 费项id
     */
    @ApiModelProperty("费项id")
    private Long chargeItemId;

    /**
     * 费项名称
     */

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    /**
     * 触发事件类型
     */
    @ApiModelProperty("触发事件类型")
    private Long eventType;

    /**
     * 凭证规则id
     */
    @ApiModelProperty("凭证规则id")
    private Long voucherRuleId;

    /**
     * 凭证规则名称
     */
    @ApiModelProperty("凭证规则名称")
    private String voucherRuleName;

    /**
     * 凭证系统
     */
    @ApiModelProperty("凭证系统")
    private String voucherSystem;

    /**
     * 推凭状态：0成功，1失败
     */
    @ApiModelProperty("推凭状态：0成功，1失败")
    private Integer successState;

    /**
     * 借方金额
     */
    @ApiModelProperty("借方金额")
    private Long debitAmount;

    /**
     * 贷方金额
     */
    @ApiModelProperty("贷方金额")
    private Long creditAmount;

    /**
     * 该次推凭的凭证记录id集合
     */
    @ApiModelProperty("该次推凭的凭证记录id集合")
    private String voucherIds;

    /**
     * 租户id
     */
    @ApiModelProperty("租户id")
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private String creator;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("创建时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @ApiModelProperty("操作人ID")
    private String operator;

    /**
     * 操作人姓名
     */
    @ApiModelProperty("操作人姓名")
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @ApiModelProperty("修改时间 yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
