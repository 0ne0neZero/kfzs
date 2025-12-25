package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 付款单信息
 * @author yancao
 */
@Setter
@Getter
@ApiModel("付款单信息")
public class PayBillV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "法定单位id")
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

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

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty(value = "是否挂账：0未挂账，1已挂账，2已销账")
    private Integer onAccount;

    @ApiModelProperty(value = "退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty(value = "审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty(value = "结转状态：0未结转，1待结转，2部分结转，3已结转")
    private Integer carriedState;

    @ApiModelProperty(value = "开票状态：0未收票，1收票中，3已收票")
    private Integer invoiceState;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty(value = "是否交账：0未交账，1部分交账，2已交账")
    private Integer accountHanded;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;

    @ApiModelProperty(value = "是否冲销：0未冲销，1已冲销")
    private Integer reversed;

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

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty(value = "付款单明细")
    private List<PayDetailV> payDetails;

}
