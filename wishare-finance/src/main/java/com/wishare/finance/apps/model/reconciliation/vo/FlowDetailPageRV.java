package com.wishare.finance.apps.model.reconciliation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流水领用流水明细详细信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("流水领用流水明细详细信息")
public class FlowDetailPageRV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("流水号")
    private String serialNumber;

    @ApiModelProperty("交易金额")
    private Long settleAmount;

    @ApiModelProperty("交易日期")
    private LocalDateTime payTime;

    @ApiModelProperty("对方账户")
    private String oppositeAccount;

    @ApiModelProperty("对方名称")
    private String oppositeName;

    @ApiModelProperty("对方开户行")
    private String oppositeBank;

    @ApiModelProperty("本方账户")
    private String ourAccount;

    @ApiModelProperty("本方名称")
    private String ourName;

    @ApiModelProperty("本方开户行")
    private String ourBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("资金用途")
    private String fundPurpose;

    @ApiModelProperty("交易平台")
    private String tradingPlatform;

    @ApiModelProperty("交易方式")
    private String transactionMode;

    @ApiModelProperty("是否为同步数据（0否，1是）")
    private Integer syncData;

    @ApiModelProperty("流水类型：1收入 2退款")
    private Integer type;

}
