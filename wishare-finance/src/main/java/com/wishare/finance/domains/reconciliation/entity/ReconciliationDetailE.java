package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 对账单明细表,管理对账单单条账单的对账信息
 *
 * @author dxclay
 * @since 2022-10-13
 */
@Getter
@Setter
@TableName(value = "reconciliation_detail", autoResultMap = true)
public class ReconciliationDetailE {

    @ApiModelProperty(value = "主键id")
    @TableId
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

    /**
     * {@linkplain ReconcileResultEnum}
     */
    @ApiModelProperty(value = "对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer result;

    @ApiModelProperty(value = "应收金额")
    private Long receivableAmount = 0L;

    @ApiModelProperty(value = "实收金额")
    private Long actualAmount = 0L;

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount = 0L;

    @ApiModelProperty(value = "票据金额")
    private Long receiptAmount = 0L;

    @ApiModelProperty("退款金额")
    private Long refundAmount = 0L;

    @ApiModelProperty("结转金额（单位：分）")
    private Long carriedAmount;

    @ApiModelProperty("实际退款金额")
    private Long actRefundAmount = 0L;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;
    @ApiModelProperty(value = "流水认领金额")
    private Long flowClaimAmount = 0L;

    @ApiModelProperty(value = "对账日期")
    private LocalDateTime ReconcileTime;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("交易时间")
    private LocalDateTime tradeTime;

    @ApiModelProperty(value = "结算流水id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> settleIds;

    @ApiModelProperty(value = "关联的发票id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> invoiceIds;

    @ApiModelProperty(value = "关联的流水id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> flowIds;

    @ApiModelProperty(value = "退款记录id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> refundIds;

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

    @ApiModelProperty("手续费(单位：分)")
    private Long commission;

    @ApiModelProperty("渠道交易方式C-银行卡消费Z-支付宝W-微信P-pos通Y-翼支付WZF-沃支付U-银联二维码ECNY-数字人民币")
    private String channelPayWay;

    @ApiModelProperty("渠道商户号")
    private String channelMid;

    @ApiModelProperty("交易流水号（渠道）")
    private String channelSeqId;

    @ApiModelProperty("渠道交易金额（单位：分）")
    private Long channelTradeAmount = 0L;

    @ApiModelProperty("渠道对账状态")
    private Integer channelReconState;

    @ApiModelProperty("银行流水号")
    private String bankSeqId;

    @ApiModelProperty("对账模式: 0账票流水对账，1商户清分对账")
    private Integer reconcileMode;

    @ApiModelProperty("报账单id")
    private String voucherBillId;


    @ApiModelProperty("报账单编号")
    private String voucherBillNo;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;


    /**
     * 执行对账结果
     */
    public void doReconcileResult() {
        if (Objects.isNull(reconDetNo)){
            reconDetNo = IdentifierFactory.getInstance().serialNumber("reconciliation_detail", "DZ", 22);
        }
        if (ReconcileModeEnum.商户清分对账.equalsByCode(reconcileMode)){
            doMchClearReconcile();
        }
    }

    /**
     * 清分对账
     */
    public void doMchClearReconcile(){
        if (receivableAmount.equals(channelTradeAmount)){
            result = ReconcileResultEnum.已核对.getCode();
        }else {
            result =  ReconcileResultEnum.核对失败.getCode();
        }
    }

    /**
     * 账票流水对账
     */
    public void doBillInvoiceFlowsReconcile(){
        if (actualAmount.equals(invoiceAmount) && invoiceAmount.equals(flowClaimAmount) && ((receiptAmount - refundAmount) == actualAmount)){
            result = ReconcileResultEnum.已核对.getCode();
        }else {
            result =  ReconcileResultEnum.核对失败.getCode();
        }
    }


}
