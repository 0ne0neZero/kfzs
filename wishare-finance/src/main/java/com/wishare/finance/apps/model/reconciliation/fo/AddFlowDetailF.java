package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 添加流水明细入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("添加流水明细入参")
public class AddFlowDetailF {

    @ApiModelProperty(value = "流水号",required = true)
    @NotBlank(message = "流水号不能为空")
    private String serialNumber;

    @ApiModelProperty(value = "缴费金额",required = true)
    @NotNull(message = "缴费金额不能为空")
    private Long settleAmount;

    @ApiModelProperty(value = "缴费时间",required = true)
    @NotNull(message = "缴费时间不能为空")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "对方账户",required = true)
    @NotBlank(message = "对方账户不能为空")
    private String oppositeAccount;

    @ApiModelProperty(value = "对方名称",required = true)
    @NotBlank(message = "对方名称不能为空")
    private String oppositeName;

    @ApiModelProperty(value = "对方开户行",required = true)
    @NotBlank(message = "对方开户行不能为空")
    private String oppositeBank;

    @ApiModelProperty(value = "本方账户",required = true)
    @NotBlank(message = "本方账户不能为空")
    private String ourAccount;

    @ApiModelProperty(value = "本方名称",required = true)
    @NotBlank(message = "本方名称不能为空")
    private String ourName;

    @ApiModelProperty(value = "本方开户行",required = true)
    @NotBlank(message = "本方开户行不能为空")
    private String ourBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("资金用途")
    private String fundPurpose;

    @ApiModelProperty(value = "交易平台",required = true)
    @NotBlank(message = "交易平台不能为空")
    private String tradingPlatform;

    @ApiModelProperty("交易方式")
    private String transactionMode;

}
