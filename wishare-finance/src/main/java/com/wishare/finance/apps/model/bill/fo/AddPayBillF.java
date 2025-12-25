package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建付款单入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建付款单入参")
public class AddPayBillF{

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "付款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "收款账号id")
    private Long pnAccountId;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "结算渠道 ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE: 支票 OTHER: 其他")
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

    @ApiModelProperty(value = "实付减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty(value = "收款人ID")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款人ID")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "自定义项1")
    private String extField1;

    @ApiModelProperty(value = "自定义项2")
    private String extField2;

    @ApiModelProperty(value = "自定义项3")
    private String extField3;

    @ApiModelProperty(value = "自定义项4")
    private String extField4;

    @ApiModelProperty(value = "自定义项5")
    private String extField5;

    @ApiModelProperty(value = "自定义项6")
    private String extField6;

    @ApiModelProperty(value = "自定义项7")
    private String extField7;

    @ApiModelProperty(value = "自定义项8")
    private String extField8;

    @ApiModelProperty(value = "自定义项9")
    private String extField9;

    @ApiModelProperty(value = "自定义项10")
    private String extField10;

    @ApiModelProperty("付款单明细")
    private List<AddPayDetailF> addPayDetailves;
}
