package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款账单信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/21
 */
@Getter
@Setter
@ApiModel("付款账单信息")
public class BillPayInfoV {

    @ApiModelProperty(value = "账单id", required = true)
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty("付款交易流水号")
    private String tradeNo;

    @ApiModelProperty(value = "法定单位id", required = true)
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码", required = true)
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称", required = true)
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty(value = "费项id", required = true)
    private String chargeItemId;

    @ApiModelProperty(value = "费项编码", required = true)
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "上级收费单元id", required = true)
    private String supCpUnitId = "上级收费单元id";

    @ApiModelProperty(value = "成本中心id，成本中心id和编码二选一条件")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心编码")
    private String costCenterCode;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id", required = true)
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "收款账号id")
    private String sbAccountId;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号， 可用于账单展示信息", required = true)
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id， 业务唯一标识如订单号等")
    private String outBusId;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    @ApiModelProperty(value = "账单说明", required = true)
    @NotBlank(message = "账单说明不能为空")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（默认：CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "付款金额（单位：分）", required = true)
    private Long totalAmount;

    @ApiModelProperty(value = "账单开始时间，账单的开始账期", required = true)
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间，账单的结束账期", required = true)
    private LocalDateTime endTime;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他")
    private String payChannel;

    @ApiModelProperty(value = "付款方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "付款类型：0普通付款，1退款付款")
    private Integer payType;

    @ApiModelProperty(value = "实付减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty(value = "退款金额（单位：分）")
    private Long refundAmount;

    @ApiModelProperty(value = "结转金额（单位：分）")
    private Long carriedAmount;

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

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty(value = "是否挂账：0未挂账，1已挂账，2已销账")
    private Integer onAccount;

    @ApiModelProperty(value = "退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty(value = "开票状态：0未收票，1收票中，3已收票")
    private Integer invoiceState;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段2")
    private String extField2;

    @ApiModelProperty("扩展字段3")
    private String extField3;

    @ApiModelProperty("扩展字段4")
    private String extField4;

    @ApiModelProperty("扩展字段5")
    private String extField5;

    @ApiModelProperty("扩展字段6")
    private String extField6;

    @ApiModelProperty("扩展字段7")
    private String extField7;

    @ApiModelProperty("扩展字段8")
    private String extField8;

    @ApiModelProperty("扩展字段9")
    private String extField9;

    @ApiModelProperty("扩展字段10")
    private String extField10;

}
