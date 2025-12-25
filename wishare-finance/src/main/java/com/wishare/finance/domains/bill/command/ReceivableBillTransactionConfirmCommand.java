package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import org.springframework.validation.annotation.Validated;

/**
 * 预支付确认信息
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("预支付确认信息")
public class ReceivableBillTransactionConfirmCommand {

    @ApiModelProperty(value = "预支付交易id", required = true)
    private String preTradeId;

    @ApiModelProperty(value = "外部支付编号（支付宝/微信单号，银行流水号等）")
    private String outTradeNo;

    @ApiModelProperty(value = "支付状态 （0支付失败，1支付成功，2取消支付）", required = true)
    private Integer payState;

    @ApiModelProperty(value = "支付渠道")
    private String payChannel;

    @ApiModelProperty(value = "支付方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "支付时间 格式： yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty("支付来源：0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 10-亿家生活app，11-亿管家app，12-亿家生活公众号，13-智能pos机")
    private Integer paySource;

    @ApiModelProperty(value = "支付渠道商户号")
    private String mchNo;

    @ApiModelProperty(value = "支付渠道设备号")
    private String deviceNo;

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("银行流水号")
    private String bankFlowNo;

    @ApiModelProperty("收款人id")
    private String payeeId;

    @ApiModelProperty("收款人名称")
    private String payeeName;
}
