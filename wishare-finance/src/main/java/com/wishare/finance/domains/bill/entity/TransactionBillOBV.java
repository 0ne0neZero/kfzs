package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易账单值对象信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/17
 */
@Getter
@Setter
@ApiModel("交易账单值对象信息")
public class TransactionBillOBV {

    @ApiModelProperty(value = "1应收账单,2预收账单, 3临时收费账单, 4应付账单, 5退款账单, 6付款单, 7收款单")
    private Integer billType;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "付款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "结算渠道")
    private String payChannel;

    @ApiModelProperty(value = "付款方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "付款类型：0普通付款，1退款付款")
    private Integer payType;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "账单说明")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额（单位：分）")
    private Long totalAmount;

    @ApiModelProperty(value = "开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty(value = "收款人ID")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款人ID")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "交易账单明细列表")
    private List<TransactionBillDetailOBV> transactionBillDetails;

}
