package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 发票领用金额
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("已勾选流水统计信息")
public class FlowStatisticsClaimInfoV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收入类型的总流水")
    private Long inAmount=0L;

    @ApiModelProperty("退款类型的总流水")
    private Long outAmount=0L;

    @ApiModelProperty("流水总额")
    private Long totalAmount=0L;

    /**
     * 对方账户
     */
    @ApiModelProperty("对方账户")
    private String oppositeAccount;

    /**
     * 对方名称
     */
    @ApiModelProperty("对方名称")
    private String oppositeName;

    /**
     * 对方开户行
     */
    @ApiModelProperty("对方开户行")
    private String oppositeBank;

    /**
     * 本方账户
     */
    @ApiModelProperty("本方账户")
    private String ourAccount;

    /**
     * 本方名称
     */
    @ApiModelProperty("本方名称")
    private String ourName;

    /**
     * 本方开户行
     */
    @ApiModelProperty("本方开户行")
    private String ourBank;
}
