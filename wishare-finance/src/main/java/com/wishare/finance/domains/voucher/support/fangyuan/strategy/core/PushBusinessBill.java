package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;


import com.wishare.finance.domains.bill.entity.Bill;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PushBusinessBill extends Bill {
    @ApiModelProperty(value = "单据编号")
    private String voucherBillDetailNo;
    @ApiModelProperty(value = "账单id")
    private Long billId;
    @ApiModelProperty(value = "账单编号")
    private String billNo;
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
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;
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
    @ApiModelProperty(value = "不含税金额 对应 应收金额")
    private Long taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private Long taxAmount;
    @ApiModelProperty(value = "账单费用分类 1历史欠费,2当期应收,3预收款项")
    private Integer billCostType;
    @ApiModelProperty(value = "现金流项目")
    private String cashFlowItem;
    @ApiModelProperty(value = "业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "业务单元id")
    private Long sbAccountId;
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;
    @ApiModelProperty(value = "银行流水号")
    private String bankSerialNumber;



    @ApiModelProperty(value = "结转详情")
    private String carryoverDetail;
    @ApiModelProperty(value = "结算渠道")
    private String payChannel;
    @ApiModelProperty(value = "结算时间")
    private LocalDateTime payTime;
    @ApiModelProperty(value = "聚合支付方式")
    private String payInfos;
    @ApiModelProperty(value = "预收聚合支付方式")
    private String payWays;
    @ApiModelProperty(value = "非结转结算渠道")
    private String payChannelCarryover;
    @ApiModelProperty(value = "结转记录id")
    private Long billCarryoverId;
    @ApiModelProperty(value = "收款明细id")
    private Long gatherDetailId;
    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认 5收款结转 6预收结转 ")
    private Integer billEventType;

    @ApiModelProperty(value = "是否冲销：0未冲销，1已冲销")
    private Integer inferenceState;

    @ApiModelProperty(value = "业务场景记录ID")
    private Long sceneId;

    @ApiModelProperty(value = "业务场景类型")
    private Integer sceneType;

    @ApiModelProperty(value = "发票id")
    private Long invoiceId;

    @ApiModelProperty(value = "是否推过红字凭证  0 否  1是")
    private Integer invoiceRedType;

    @ApiModelProperty(value = "是否可见：0是 1否")
    private Integer visible = 0;

    @ApiModelProperty(value = "手续费")
    private BigDecimal commission;
    /**
     * 是否是违约金：0-否/1-是
     */
    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue;
    @ApiModelProperty(value = "减免生成时间")
    private LocalDateTime baCreate;
    @ApiModelProperty(value = "报账单生成时间")
    private LocalDateTime vbdCreate;

    @ApiModelProperty(value = "对账id")
    private Long reconciliationId;
    @ApiModelProperty(value = "对账明细id")
    private Long reconciliationDetailId;

    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;
    @ApiModelProperty(value = "nc收款单号")
    private String gatherBillNc;
}
