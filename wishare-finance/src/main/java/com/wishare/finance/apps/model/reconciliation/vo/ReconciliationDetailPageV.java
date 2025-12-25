package com.wishare.finance.apps.model.reconciliation.vo;

import com.wishare.finance.domains.reconciliation.entity.ReconciliationRecBillDetailOBV;
import com.wishare.finance.domains.reconciliation.enums.ExPayChannelEnums;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账单明细
 *
 * @author dxclay
 * @since 2022-10-13
 */
@Getter
@Setter
@ApiModel("对账单分页明细信息")
public class ReconciliationDetailPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty("对账详情单号")
    private String reconDetNo;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "对账记录id")
    private Long reconciliationId;

    @ApiModelProperty(value = "对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer result;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "流水认领金额")
    private BigDecimal flowClaimAmount;

    @ApiModelProperty(value = "对账日期")
    private LocalDateTime ReconcileTime;

    @ApiModelProperty("交易时间")
    private LocalDateTime tradeTime;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("结转金额（单位：分）")
    private BigDecimal carriedAmount;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("手续费(单位：分)")
    private BigDecimal commission;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("结算渠道\n" +
            "   ALIPAY：支付宝，\n" +
            "   WECHATPAY:微信支付，\n" +
            "   CASH:现金，\n" +
            "   POS: POS机，\n" +
            "   UNIONPAY:银联，\n" +
            "   SWIPE: 刷卡，\n" +
            "   BANK:银行汇款，\n" +
            "   CARRYOVER:结转，\n" +
            "   CHEQUE: 支票\n" +
            "   OTHER: 其他")
    private String payChannel;

    @ApiModelProperty("结算渠道描述")
    private String payChannelStr;

    @ApiModelProperty("渠道交易方式C-银行卡消费Z-支付宝W-微信P-pos通Y-翼支付WZF-沃支付U-银联二维码ECNY-数字人民币")
    private String channelPayWay;

    @ApiModelProperty("支付渠道描述")
    private String channelPayWayStr;

    @ApiModelProperty("渠道商户号")
    private String channelMid;

    @ApiModelProperty("交易流水号（渠道）")
    private String channelSeqId;

    @ApiModelProperty("渠道交易金额（单位：分）")
    private BigDecimal channelTradeAmount;

    @ApiModelProperty("渠道对账状态")
    private Integer channelReconState;

    @ApiModelProperty("银行流水号")
    private String bankSeqId;


    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("报账单id")
    private String voucherBillId;

    @ApiModelProperty("报账单编号")
    private String voucherBillNo;

    @ApiModelProperty("报账单id列表")
    private List<String> voucherBillIdList;

    @ApiModelProperty("报账单编号列表")
    private List<String> voucherBillNoList;

    @ApiModelProperty("对应账单id列表")
    private List<Long> recBillIdList;

    @ApiModelProperty("对应账单编号列表")
    private List<String> recBillNoList;


    @ApiModelProperty("对应账单编号字符串")
    private String recBillNoStr;

    @ApiModelProperty("退款结转金额")
    private BigDecimal refundCarriedAmount;

    @ApiModelProperty(value = "对账应收账单列表")
    private List<ReconciliationRecBillDetailOBV> recBillDetails;
    public String getPayChannelStr() {
        if (StringUtils.isNotBlank(this.getPayChannel())) {
            SettleWayChannelEnum settleWayChannelEnum = SettleWayChannelEnum.valueOfByCode(this.getPayChannel());
            if (settleWayChannelEnum != null) {
                return settleWayChannelEnum.getValue();
            }
        }
        return payChannelStr;
    }

    public String getChannelPayWayStr() {
        if (StringUtils.isNotBlank(this.getChannelPayWay())) {
            ExPayChannelEnums exPayChannelEnums = ExPayChannelEnums.valueOfByCode(this.getChannelPayWay());
            if (exPayChannelEnums != null) {
                return exPayChannelEnums.getDes();
            }
        }
        return channelPayWayStr;
    }


    /**
     * 对账日期取创建时间
     *
     * @return
     */
    public LocalDateTime getReconcileTime() {
        return this.getGmtCreate();
    }
}
