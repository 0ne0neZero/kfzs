package com.wishare.finance.domains.voucher.support.fangyuan.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL_DETAIL, autoResultMap = true)
public class VoucherPushBillDetail extends BaseEntity{

    @ApiModelProperty(value = "单据明细id")
    private Long id;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "账单编号")
    private String billNo;
    @ApiModelProperty(value = "单据编号")
    private String voucherBillDetailNo;
    @ApiModelProperty(value = "汇总单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "账单类型 1:应收账单，2:预收账单，3:临时收费账单")
    private Integer billType;
    @ApiModelProperty(value = "项目id")
    private String communityId;
    @ApiModelProperty(value = "所属项目")
    private String communityName;
    @ApiModelProperty(value = "资产名称")
    private String roomName;
    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;
    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;
    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;
    @ApiModelProperty(value = "客户")
    private String customName;
    @ApiModelProperty(value = "客户id")
    private String customId;
    @ApiModelProperty(value = "收款方ID")
    private String payeeId;
    @ApiModelProperty(value = "收款方名称")
    private String payeeName;
    @ApiModelProperty(value = "付款方ID")
    private String payerId;
    @ApiModelProperty(value = "付款方名称")
    private String payerName;
    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;
    @ApiModelProperty(value = "税率id")
    private Long taxRateId;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "含税金额")
    private Long taxIncludAmount;
    @ApiModelProperty(value = "不含税金额")
    private Long taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private Long taxAmount;
    @ApiModelProperty(value = "手续费")
    private BigDecimal commission;
    @ApiModelProperty(value = "账单费用分类 1历史欠费,2当期应收,3预收款项")
    private Integer billCostType;
    @ApiModelProperty(value = "现金流项目")
    private String cashFlowItem;

    @ApiModelProperty(value = "银行账号")
    private String bankAccount;
    @ApiModelProperty(value = "银行流水号")
    private String bankSerialNumber;
    @ApiModelProperty(value = "业务单元id")
    private Long businessUnitId;
    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "结转账单id")
    private Long billCarryoverId;

    @ApiModelProperty(value = "结算方式  ALIPAY：支付宝，WECHATPAY:微信支付， CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡，BANK:银行汇款，CARRYOVER:结转，CHEQUE:支票，OTHER:其他，COMPLEX：组合支付'")
    private String payChannel;
    @ApiModelProperty(value = "结算时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认 5收款结转 6预收结转 ")
    private Integer billEventType;
    @ApiModelProperty(value = "推送状态  1待推送 2推送成功 3 推送失败")
    private Integer pushBillState;

    @ApiModelProperty(value = "业务场景记录ID")
    private Long sceneId;
    @ApiModelProperty(value = "业务场景类型")
    private Long sceneType;
    @ApiModelProperty(value = "发票id")
    private Long invoiceId;
    @ApiModelProperty(value = "是否推过红字凭证  0 否  1是")
    private Long invoiceRedType;

    @ApiModelProperty(value = "是否可见：0是 1否")
    private Integer visible = 0;
    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;
    @ApiModelProperty(value = "对账明细id")
    private Long reconciliationDetailId;
    @ApiModelProperty(value = "nc收款单号")
    private String gatherBillNc;
}
