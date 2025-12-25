package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillCarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillOnAccountEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillVerifyStateEnum;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 付款单表
 * </p>
 *
 * @author yancao
 * @since 2022-12-20
 */
@Getter
@Setter()
@Accessors(chain = true)
@TableName(TableNames.PAY_BILL)
public class PayBill {

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @TableField(exist = false)
    private Integer billType = BillTypeEnum.付款单.getCode();

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

    @ApiModelProperty(value = "支付交易单号(交易订单表单号)")
    private String transactionNo;

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

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty("商户清分对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

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

    @ApiModelProperty("归属年")
    private Integer accountYear;

    @ApiModelProperty("归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
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

    @TableField(exist = false)
    private List<PayDetail> payDetails;

    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        }
        if (Objects.isNull(getBillNo())){
            setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        }
    }

    /**
     * 重置账单所有的状态
     */
    public void resetState(){
        this.state = BillStateEnum.正常.getCode();
        this.onAccount = BillOnAccountEnum.未挂账.getCode();
        this.refundState = BillRefundStateEnum.未退款.getCode();
        this.verifyState = BillVerifyStateEnum.未核销.getCode();
        this.approvedState = BillApproveStateEnum.待审核.getCode();
        this.carriedState = BillCarryoverStateEnum.未结转.getCode();
        this.invoiceState = BillInvoiceStateEnum.未开票.getCode();
        this.accountHanded = BillAccountHandedStateEnum.未交账.getCode();
        this.reversed = BillReverseStateEnum.未冲销.getCode();
    }
}
