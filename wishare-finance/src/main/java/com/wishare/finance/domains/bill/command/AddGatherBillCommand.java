package com.wishare.finance.domains.bill.command;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2023/1/9
 * @Description:
 */
@Getter
@Setter
public class AddGatherBillCommand {

    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("外部业务id")
    private String outBusId;

    @ApiModelProperty("上级收费单元ID")
    @NotBlank(message = "上级收费单元不能为空")
    private String supCpUnitId;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

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

    @ApiModelProperty("结算渠道\n" +
            "ALIPAY：支付宝，\n" +
            "WECHATPAY:微信支付，\n" +
            "CASH:现金，\n" +
            "POS: POS机，\n" +
            "UNIONPAY:银联，\n" +
            "SWIPE: 刷卡，\n" +
            "BANK:银行汇款，\n" +
            "CARRYOVER:结转，\n" +
            "CHEQUE: 支票\n" +
            "OTHER: 其他")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
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

    @ApiModelProperty("账单金额（单位：分）")
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
    private Integer sysSource;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty("收款单明细")
    private List<AddGatherBillDetailCommand> addGatherBillDetailCommandList;
}
