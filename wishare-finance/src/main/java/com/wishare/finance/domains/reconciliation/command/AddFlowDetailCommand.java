package com.wishare.finance.domains.reconciliation.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 添加流水明细命令
 *
 * @author yancao
 */
@Getter
@Setter
public class AddFlowDetailCommand {

    @ApiModelProperty("流水号")
    @NotBlank(message = "流水号不能为空")
    private String serialNumber;

    @ApiModelProperty("缴费金额")
    private Long settleAmount;

    @ApiModelProperty("缴费时间")
    @NotNull(message = "缴费时间不能为空")
    private LocalDateTime payTime;

    @ApiModelProperty("对方账户")
    @NotBlank(message = "对方账户不能为空")
    private String oppositeAccount;

    @ApiModelProperty("对方名称")
    @NotBlank(message = "对方名称不能为空")
    private String oppositeName;

    @ApiModelProperty("对方开户行")
    @NotBlank(message = "对方开户行不能为空")
    private String oppositeBank;

    @ApiModelProperty("本方账户")
    @NotBlank(message = "本方账户不能为空")
    private String ourAccount;

    @ApiModelProperty("本方名称")
    @NotBlank(message = "本方名称不能为空")
    private String ourName;

    @ApiModelProperty("本方开户行")
    @NotBlank(message = "本方开户行不能为空")
    private String ourBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("资金用途")
    private String fundPurpose;

    @ApiModelProperty("交易平台")
    private String tradingPlatform;

    @ApiModelProperty("交易方式")
    @NotBlank(message = "交易方式不能为空")
    private String transactionMode;

}
