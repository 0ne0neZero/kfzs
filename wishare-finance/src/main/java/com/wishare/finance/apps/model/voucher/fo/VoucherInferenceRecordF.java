package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
public class VoucherInferenceRecordF {

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

}
