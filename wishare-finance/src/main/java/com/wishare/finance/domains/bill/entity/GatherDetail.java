package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.domains.bill.consts.enums.BillInvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.InvoiceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 收款单明细(gather_detail)
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Getter
@Setter
@TableName(value = TableNames.GATHER_DETAIL, autoResultMap = true)
@Accessors(chain = true)
public class GatherDetail {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单类型
     */
    private Integer gatherType;

    /**
     * 收款单id
     */
    private Long gatherBillId;

    /**
     * 收款单号
     */
    private String gatherBillNo;

    /**
     * 应收单id
     */
    private Long recBillId;

    /**
     * 应收单编号
     */
    private String recBillNo;

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 收费组织id
     */
    private String cpOrgId;

    /**
     * 收费组织名称
     */
    private String cpOrgName;

    /**
     * 上级收费单元id
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String supCpUnitId;

    /**
     * 上级收费单元名称
     */
    private String supCpUnitName;

    /**
     * 收费单元id
     */
    private String cpUnitId;

    /**
     * 收费单元名称
     */
    private String cpUnitName;

    /**
     * 结算渠道
     * ALIPAY：支付宝，
     * WECHATPAY:微信支付，
     * CASH:现金，
     * POS: POS机，
     * UNIONPAY:银联，
     * SWIPE: 刷卡，
     * BANK:银行汇款，
     * CARRYOVER:结转，
     * CHEQUE: 支票
     * OTHER: 其他
     */
    private String payChannel;

    /**
     * 结算方式(0线上，1线下)
     */
    private Integer payWay;

    /**
     * 外部结算编号（支付宝单号，银行流水号等）
     */
    private String outPayNo;

    /**
     * 结转账单id
     */
    private Long carriedBillId;

    /**
     * 结转账单编号
     */
    private String carriedBillNo;

    /**
     * 结转账单类型 1:应收单，2:预收单，3:应付单，4:付款单，5:收款单，6预付单
     */
    private Integer carriedBillType;

    /**
     * 结转记录id
     */
    private Long billCarryoverId;


    /**
     * 被结转账单支付方式
     */
    private String carriedBillPayChannel;

    /**
     * 应收收款金额（单位：分）
     */
    private Long recPayAmount;

    /**
     * 收款金额（单位：分）(合单支付时，收款金额 > 结算金额)
     */
    private Long payAmount;

    /**
     * 优惠金额
     */
    private Long preferentialAmount;

    /**
     * 优惠金额退款金额
     */
    private Long preferentialRefundAmount;

    /**
     * 收费对象类型
     */
    private Integer payerType;

    /**
     * 付款人id
     */
    private String payerId;

    /**
     * 付款人名称
     */
    private String payerName;
    /**
     * 付款方手机号
     */
    private String payerPhone;

    /**
     * 收款方id
     */
    private String payeeId;

    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 收款方手机号
     */
    private String payeePhone;

    /**
     * 收款时间
     */
    private LocalDateTime payTime;

    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;

    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;

    /**
     * 推凭状态 0-未推凭，1-已推凭
     */
    private Integer inferenceState;

    /**
     * 开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    private Integer invoiceState;

    /**
     * 开票金额（分）
     */
    private Long invoiceAmount = 0L;

    /**
     * 退款金额（分）
     */
    private Long refundAmount;

    /**
     * 结转金额（分）
     */
    private Long carriedAmount;

    /**
     * 扣款金额（分）
     */
    private Long deductionAmount;


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
     * 是否有效 0有效 1失效
     */
    private Integer available;

    /**
     * 备注
     */
    private String remark;

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
     * 是否是违约金
     */
    @TableField(exist = false)
    private Integer isDefault;

    /**
     * 是否是违约金名称
     */
    @TableField(exist = false)
    private String isDefaultName;

    /**
     * 单价（单位：分）
     */
    @TableField(exist = false)
    private BigDecimal unitPrice;

    /**
     * 计费方式
     */
    @TableField(exist = false)
    private Integer billMethod;

    /**
     * 账单类型
     */
    @TableField(exist = false)
    private Integer billType;

    /**
     * 账单类型名称
     */
    @TableField(exist = false)
    private String billTypeName;

    /**
     * 收款方式名称
     */
    @TableField(exist = false)
    private String payWayName;

    /**
     * 收款方式拼接名称
     */
    @TableField(exist = false)
    private String payInfosString;

    @ApiModelProperty("现金流科目")
    @TableField(exist = false)
    private String subjectName;

    @ApiModelProperty(value = "法定单位名称中文")
    @TableField(exist = false)
    private String statutoryBodyName;

    /**
     * 法定单位id
     */
    @TableField(exist = false)
    private Long statutoryBodyId;

    /**
     * 系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    @TableField(exist = false)
    private Integer sysSource;
    /**
     * 账单状态（0正常，1冻结，2作废）
     */
    @TableField(exist = false)
    private Integer state;
    /**
     * 核销状态（0未核销，1已核销）
     */
    @TableField(exist = false)
    private Integer verifyState;
    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    @TableField(exist = false)
    private Integer approvedState;
    /**
     * 是否冲销：0未冲销，1已冲销
     */
    @TableField(exist = false)
    private Integer reversed;

    /**
     * 新收款明细查询使用：所属收款单明细总数
     */
    @TableField(exist = false)
    private Integer total;

    /**
     * 应收日(包含年月日)
     */
    @TableField(exist = false)
    private LocalDate receivableDate;

    /**
     * 初始化结算信息
     */
    public void init() {
        //自动加载结算账单id
        if (id == null) {
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_DETAIL);
        }
    }

    /**
     * 获取收款类型
     *
     * @return
     */
    public static GatherTypeEnum getGatherType(Bill bill) {
        return bill instanceof AdvanceBill ? GatherTypeEnum.预收 : GatherTypeEnum.应收;
    }

    /**
     * 获取剩余可结转金额
     *
     * @return 可结转金额
     */
    public Long getRemainingCarriedAmount() {
        return this.payAmount -
                Optional.ofNullable(this.refundAmount).orElse(0L) -
                Optional.ofNullable(this.carriedAmount).orElse(0L) -
                Optional.ofNullable(this.deductionAmount).orElse(0L);

    }

    /**
     * 获取剩余可用金额
     *
     * @return 可结转金额
     */
    public Long getCanRefundAmount() {
        return this.payAmount -
                Optional.ofNullable(this.refundAmount).orElse(0L) -
                Optional.ofNullable(this.carriedAmount).orElse(0L) -
                Optional.ofNullable(this.deductionAmount).orElse(0L);

    }

    public void invoice(Long invoiceAmount, boolean success) {
        if (success) {
            this.invoiceAmount += invoiceAmount;
        }
        if (this.invoiceAmount == 0L) {
            this.invoiceState = InvoiceStateEnum.未开票.getCode();
        } else if (this.invoiceAmount < this.recPayAmount && this.invoiceAmount > 0L) {
            this.invoiceState = InvoiceStateEnum.部分开票.getCode();
        } else {
            // 对于其余情况，都当时已开票，即使是异常情况，防止异常情况下继续开票造成数据进一步错误
            // 在错误的情况下，变为已开票就不能再开，这样可以让开发第一时间发现异常，介入修复
            this.invoiceState = InvoiceStateEnum.已开票.getCode();
        }
    }

    /**
     * 红冲
     * @param invoiceAmount 开票金额
     * @return
     */
    public boolean voidBatch(long invoiceAmount){
        long afterInvoiceAmount =  this.invoiceAmount - invoiceAmount;
        if (afterInvoiceAmount == 0){
            invoiceState = BillInvoiceStateEnum.未开票.getCode();
        }else {
            invoiceState = BillInvoiceStateEnum.部分开票.getCode();
        }
        this.invoiceAmount = afterInvoiceAmount;
        return true;
    }

    /**
     * 获取可开票金额
     * 扣款金额不影响开票
     *
     * @return 可开票金额
     */
    public Long getCanInvoiceAmount() {
        if (TenantUtil.bf24()) {
            // 方圆、慧享云环境 开票时包含结转出去的金额，结转进来的钱不能开
            if (StringUtils.equals(this.getPayChannel(), SettleChannelEnum.结转.getCode())) {
                return 0L;
            }
            return this.payAmount - Optional.ofNullable(this.refundAmount).orElse(0L)
                    - this.invoiceAmount;
        } else {
            return this.payAmount - this.invoiceAmount -
                    Optional.ofNullable(this.refundAmount).orElse(0L) -
                    Optional.ofNullable(this.carriedAmount).orElse(0L);
        }
    }

}
