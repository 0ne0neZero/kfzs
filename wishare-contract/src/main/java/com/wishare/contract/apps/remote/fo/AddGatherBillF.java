package com.wishare.contract.apps.remote.fo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("创建收款单入参")
public class AddGatherBillF {

    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("外部业务id")
    private String outBusId;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("收款账号id")
    private Long sbAccountId;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "结算渠道\n" +
            "ALIPAY：支付宝，\n" +
            "WECHATPAY:微信支付，\n" +
            "CASH:现金，\n" +
            "POS: POS机，\n" +
            "UNIONPAY:银联，\n" +
            "SWIPE: 刷卡，\n" +
            "BANK:银行汇款，\n" +
            "CARRYOVER:结转，\n" +
            "CHEQUE: 支票\n" +
            "OTHER: 其他",required = true)
    @NotBlank(message = "结算渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)",required = true)
    @NotBlank(message = "结算方式不能为空")
    private Integer payWay;

    @ApiModelProperty("税率id")
    private Long taxRateId;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额")
    private Long taxAmount;

    @ApiModelProperty("账单说明")
    private String description;

    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额（单位：分）",required = true)
    @NotNull(message = "账单金额不能为空")
    private Long totalAmount;

    @ApiModelProperty("收款人ID")
    private String payeeId;

    @ApiModelProperty("收款人名称")
    private String payeeName;

    @ApiModelProperty("付款人ID")
    private String payerId;

    @ApiModelProperty("付款人名称")
    private String payerName;

    @ApiModelProperty("扩展参数")
    private String attachParams;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    @NotNull(message = "审核状态不能为空")
    private Integer approvedState;

    @ApiModelProperty(value = "收款时间",required = true)
    @NotNull(message = "收款时间不能为空")
    private LocalDateTime payTime;

    @Valid
    @ApiModelProperty("收款单明细")
    private List<AddGatherBillDetailF> addGatherBillDetails;
}