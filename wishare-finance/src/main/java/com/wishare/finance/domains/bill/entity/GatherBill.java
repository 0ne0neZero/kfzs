package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.bill.support.DiscountOBVJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 收款单表
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Getter
@Setter
@Slf4j
@Accessors(chain = true)
@TableName(value = TableNames.GATHER_BILL, autoResultMap = true)
public class GatherBill {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 外部账单编号
     */
    private String outBillNo;

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
     * 银行流水号
     */
    private String bankFlowNo;

    /**
     * 外部业务单号
     */
    private String outBusNo;

    /**
     * 外部业务id
     */
    private String outBusId;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 收款账号id
     */
    private Long sbAccountId;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 收款时间
     */
    private LocalDateTime payTime;

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
     * 支付来源：
     * 0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 10-亿家生活app，11-亿管家app，12-亿家生活公众号，13-智能pos机
     */
    private Integer paySource;

    /**
     * 支付渠道商户号
     */
    private String mchNo;

    /**
     * 支付渠道设备号
     */
    private String deviceNo;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 减免说明信息
     * {}
     */
    @TableField(typeHandler = DiscountOBVJSONListTypeHandler.class, javaType = true)
    private List<DiscountOBV> discounts;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 税额
     */
    private Long taxAmount;

    /**
     * 账单说明
     */
    private String description;

    /**
     * 币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 账单金额（单位：分）
     */
    private Long totalAmount;

    /**
     * 实收减免金额（单位：分）
     */
    private Long discountAmount;

    /**
     * 退款金额（单位：分）
     */
    private Long refundAmount;

    /**
     * 结转金额（单位：分）
     */
    private Long carriedAmount;

    /**
     * 开票金额（单位：分）
     */
    private Long invoiceAmount;

    /**
     * 扣款金额（单位：分）
     */
    private Long deductionAmount;

    /**
     * 优惠金额
     */
    private Long preferentialAmount;

    /**
     * 优惠金额退款金额
     */
    private Long preferentialRefundAmount;

    /**
     * 收款人ID
     */
    private String payeeId;

    /**
     * 收款人名称
     */
    private String payeeName;

    /**
     * 付款人ID
     */
    private String payerId;

    /**
     * 付款人名称
     */
    private String payerName;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer sysSource;

    /**
     * 账单状态（0正常，1冻结，2作废）
     */
    private Integer state;

    /**
     * 是否挂账：0未挂账，1已挂账，2已销账
     */
    private Integer onAccount;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    private Integer refundState;

    /**
     * 核销状态（0未核销，1已核销）
     */
    private Integer verifyState;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    private Integer approvedState;

    /**
     * 结转状态：0未结转，1待结转，2部分结转，3已结转
     */
    private Integer carriedState;

    /**
     * 开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    private Integer invoiceState;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty("商户清分对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

    /**
     * 是否交账：0未交账，1部分交账，2已交账
     */
    private Integer accountHanded;

    /**
     * 是否推凭：0未推凭，1已推凭
     */
    private Integer inferenceState;

    /**
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

    /**
     * 是否参与优惠赠送：0否，1是
     */
    private Integer preferential;

    /**
     * 备注
     */
    private String remark;

    /**
     * 归属年
     */
    private Integer accountYear;

    /**
     * 归属月（账期）
     */
    private LocalDate accountDate;

    /**
     * 代收银行卡号
     */
    private String collectBankAccount;

    /**
     * 代收流水单号
     */
    private String collectSerialNumber;

    /**
     * 自定义项1
     */
    private String extField1;

    /**
     * 自定义项2
     */
    private String extField2;

    /**
     * 自定义项3
     */
    private String extField3;

    /**
     * 自定义项4
     */
    private String extField4;

    /**
     * 自定义项5
     */
    private String extField5;

    /**
     * 自定义项6
     */
    private String extField6;

    /**
     * 自定义项7
     */
    private String extField7;

    /**
     * 自定义项8
     */
    private String extField8;

    /**
     * 自定义项9
     */
    private String extField9;

    /**
     * 自定义项10
     */
    private String extField10;

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

    /**
     * 收款明细
     */
    @TableField(exist = false)
    private List<GatherDetail> gatherDetails;

    @ApiModelProperty("收款明细退款详细包装参数")
    @TableField(exist = false)
    private Map<Long, Map<Long, BigDecimal>> gatherMap;

    /**
     * 初始化结算信息
     */
    public void init(){
        //自动加载结算账单id
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.GATHER_BILL);
        }
    }

    /**
     * 应收收款
     */
    public GatherBill transact(List<ReceivableBill> receivableBills,String payerPhone) {
        if (Objects.isNull(id)) {
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL);
        }
        if (billNo == null) {
            billNo = BillSerialNumberFactory.getInstance().serialNumber();
        }
        LocalDateTime minStartTime = null;
        LocalDateTime maxEndTime = null;
        GatherDetail superGatherDetail = Global.mapperFacade.map(this, GatherDetail.class);
        for (ReceivableBill receivableBill : receivableBills) {
            GatherDetail gatherDetail = new GatherDetail();
            Global.mapperFacade.map(receivableBill, gatherDetail);
            Global.mapperFacade.map(superGatherDetail, gatherDetail);
            long actualUnpayAmount = receivableBill.getActualUnpayAmount(); //应收未缴金额
            receivableBill.settle(actualUnpayAmount,0L);
            receivableBill.setChargeTime(receivableBill.getEndTime());
            receivableBill.setPayTime(getPayTime());
            //添加支付信息
            receivableBill.setPayInfos(List.of(new PayInfo(gatherDetail.getPayWay(), gatherDetail.getPayChannel())));
            gatherDetail.setSupCpUnitId(receivableBill.getSupCpUnitId());
            gatherDetail.setSupCpUnitName(receivableBill.getSupCpUnitName());
            gatherDetail.setCpUnitId(receivableBill.getCpUnitId());
            gatherDetail.setCpUnitName(receivableBill.getCpUnitName());
            gatherDetail.setCpOrgId(receivableBill.getCpOrgId());
            gatherDetail.setCpOrgName(receivableBill.getCpOrgName());
            gatherDetail.setChargeItemId(receivableBill.getChargeItemId());
            gatherDetail.setChargeItemName(receivableBill.getChargeItemName());
            gatherDetail.setCostCenterId(receivableBill.getCostCenterId());
            gatherDetail.setCostCenterName(receivableBill.getCostCenterName());
            gatherDetail.setGatherType(GatherTypeEnum.应收.getCode());
            gatherDetail.setRecBillId(receivableBill.getId());
            gatherDetail.setRecBillNo(receivableBill.getBillNo());
            gatherDetail.setGatherBillId(id);
            gatherDetail.setGatherBillNo(billNo);
            gatherDetail.setRecPayAmount(actualUnpayAmount);
            gatherDetail.setPayAmount(actualUnpayAmount);
            gatherDetail.setChargeStartTime(receivableBill.getStartTime());
            gatherDetail.setChargeEndTime(receivableBill.getEndTime());
            gatherDetail.setPayerPhone(payerPhone);
            gatherDetail.setId(null);
            gatherDetail.init();
            addDetail(gatherDetail);
            minStartTime = DateTimeUtil.minDateTime(minStartTime, receivableBill.getStartTime());
            maxEndTime = DateTimeUtil.maxDateTime(maxEndTime, receivableBill.getEndTime());
        }
        this.startTime = minStartTime;
        this.endTime = maxEndTime;
        this.approvedState = BillApproveStateEnum.已审核.getCode();
        return this;
    }

    /**
     * 新增明细
     * @param gatherDetail
     */
    public void addDetail(GatherDetail gatherDetail){
        if (this.gatherDetails == null){
            gatherDetails = new ArrayList<>();
        }
        this.gatherDetails.add(gatherDetail);
    }

    /**
     * 置为待审核
     *
     * @return
     */
    public boolean applyRefund() {
        return apply();
    }

    /**
     * 置为待审核
     *
     * @return
     */
    public boolean apply() {
        this.approvedState = BillApproveStateEnum.待审核.getCode();
        return true;
    }

    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL));
        }
        if (Objects.isNull(getBillNo())){
            setBillNo(BillSerialNumberFactory.getInstance().serialNumber());
        }
    }

    /**
     * 作废
     */
    public Boolean invalid() {
        this.state = BillStateEnum.作废.getCode();
        return true;
    }


    /**
     * 开票
     * @param invoiceAmount 新开票金额
     *             this.invoiceAmount 已开票金额总额
     * @return
     */
    public boolean invoice(long invoiceAmount, boolean success){
        if (success){
            this.invoiceAmount += invoiceAmount;
        }
        if (this.invoiceAmount == 0L) {
            invoiceState = InvoiceStateEnum.未开票.getCode();
        } else if (getTotalShouldInvoiceAmount().compareTo(this.invoiceAmount) > 0){
            invoiceState = BillInvoiceStateEnum.部分开票.getCode();
        }else if (getTotalShouldInvoiceAmount().compareTo(this.invoiceAmount) == 0){
            invoiceState = BillInvoiceStateEnum.已开票.getCode();
        }
        log.info("通过开票金额计算出开票状态，计算后的开票状态：{}，退款金额：{}，结转金额：{}，EnvData.config is ：{}",invoiceState,refundAmount,carriedAmount,EnvData.config);
        return true;
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
     * 可开票金额
     * @return
     */
    public long invoicableAmount(){
        return totalAmount - invoiceAmount;
    }

    /**
     * 获取剩余可结转金额
     *
     * @return 可结转金额
     */
    public Long getRemainingCarriedAmount() {
        return this.totalAmount -
                Optional.ofNullable(this.refundAmount).orElse(0L) -
                Optional.ofNullable(this.carriedAmount).orElse(0L) -
                Optional.ofNullable(this.deductionAmount).orElse(0L);

    }

    public Long getTotalShouldInvoiceAmount() {
        if (EnvConst.FANGYUAN.equals(EnvData.config)){
            return this.totalAmount -
                    Optional.ofNullable(this.refundAmount).orElse(0L);
        }
        return this.totalAmount -
                Optional.ofNullable(this.refundAmount).orElse(0L) -
                Optional.ofNullable(this.carriedAmount).orElse(0L);
    }

    /**
     * 获取实收金额
     * @return
     */
    public Long getActualSettleAmount(){
        return this.totalAmount -
                Optional.ofNullable(this.refundAmount).orElse(0L) -
                Optional.ofNullable(this.carriedAmount).orElse(0L);
    }

    /**
     * 重置结转状态
     */
    public void restCarriedState() {
        if ( getRemainingCarriedAmount() == 0L){
            this.carriedState = BillCarryoverStateEnum.已结转.getCode();
        }else if (getRemainingCarriedAmount() > 0 && Optional.ofNullable(this.carriedAmount).orElse(0L) != 0L){
            this.carriedState = BillCarryoverStateEnum.部分结转.getCode();
        }else {
            this.carriedState = BillCarryoverStateEnum.未结转.getCode();
        }
    }

}
