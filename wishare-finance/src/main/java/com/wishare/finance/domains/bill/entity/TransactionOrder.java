package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.bill.command.TransactionCallbackCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTransactStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.finance.infrastructure.remote.enums.payment.PaymentMethod;
import com.wishare.finance.infrastructure.remote.model.PaymentState;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 交易订单表
 *
 * @author dxclay
 * @since 2023-03-06
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="交易订单信息", description="交易订单")
@TableName(value = TableNames.TRANSACTION_ORDER, autoResultMap = true)
public class TransactionOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;
    @ApiModelProperty(value = "交易订单号")
    private String transactionNo;

    @ApiModelProperty(value = "业务系统交易单号")
    private String bizTransactionNo;

    @ApiModelProperty(value = "支付订单号（支付中心支付单号）")
    private String payNo;

    @ApiModelProperty(value = "交易标题")
    private String transactionTitle;

    @ApiModelProperty(value = "支付渠道订单号（如支付宝，微信订单号）")
    private String payChannelNo;

    @ApiModelProperty(value = "结算渠道	   ALIPAY：支付宝，	   WECHATPAY:微信支付，	   CASH:现金，	   POS: POS机，	   UNIONPAY:银联，	   SWIPE: 刷卡，	   BANK:银行汇款，	   CARRYOVER:结转，	   CHEQUE: 支票	   OTHER: 其他	   COMPLEX：组合支付")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "收款人信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private Payee payee;

    @ApiModelProperty(value = "付款人信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private Payer payer;

    @ApiModelProperty(value = "支付场景信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private Scene scene;

    @ApiModelProperty(value = "消息通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "超时时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "入账参数")
    @TableField(typeHandler = JSONTypeHandler.class)
    private TransactionBillOBV billParam;

    @ApiModelProperty(value = "扩展参数")
    private String attachParam;

    @ApiModelProperty(value = "支付参数")
    private String payParam;

    @ApiModelProperty(value = "交易金额")
    private Long amount;

    @ApiModelProperty(value = "交易参数, transactionType=4时使用")
    private String tradeParam;

    @ApiModelProperty(value = "推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败")
    private Integer voucherState;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty(value = "发票id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> invoiceIds;

    @ApiModelProperty(value = "交易类型：1收款，2付款，3退款，4账单支付")
    private Integer transactionType;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5推凭中, 6交易已关闭")
    private Integer transactState;

    @ApiModelProperty(value = "交易成功时间")
    private  LocalDateTime successTime;

    @ApiModelProperty(value = "支付方式接口")
    private String payMethod;

    @ApiModelProperty(value = "凭证id列表")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> voucherIds;

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

    @ApiModelProperty(value = "错误代码")
    @TableField(exist = false)
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    @TableField(exist = false)
    private String errMsg;

    /**
     * 子订单信息
     */
    @TableField(exist = false)
    private List<TransactionOrder> subOrders;

    public void generateIdentifier(){
        if (Objects.isNull(id)){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.TRANSACTION_ORDER);
        }

        if (Objects.isNull(transactionNo)){
            transactionNo = IdentifierFactory.getInstance().serialNumber(TableNames.TRANSACTION_ORDER, "FT", 22);
        }
    }

    /**
     * 交易状态
     * @return
     */
    public static List<Integer> payingStates() {
        return List.of(BillTransactStateEnum.交易中.getCode(), BillTransactStateEnum.交易成功.getCode());
    }

    /**
     * 支付方式
     * @return
     */
    public static String payMethod(int payWay, String payChannel){
        if (SettleWayEnum.线上.equalsByCode(payWay)){
            switch (SettleWayChannelEnum.valueOfByCode(payChannel)){
                case 招商银企直连:
                    return PaymentMethod.CBSPAY_APP;
                default:
                    throw BizException.throw402(ErrorMessage.PAYMENT_PAY_CHANNEL_NOT_SUPPORT.getErrMsg());
            }
        }
        return null;
    }

    public boolean succeed() {
        return BillTransactStateEnum.交易成功.equalsByCode(transactState);
    }

    /**
     * 交易回调
     * @param command 交易回调命令
     * @return 结果
     */
    public boolean transactCallback(TransactionCallbackCommand command) {
        //如果已经完成交易，则按正常处理
        if (!BillTransactStateEnum.待交易.equalsByCode(transactState) && !BillTransactStateEnum.交易中.equalsByCode(transactState)){
            return true;
        }
        successTime = command.getSuccessTime();
        BillTransactStateEnum transactStateEnum = getTransactStateByPayState(command.getPayState());
        this.transactState = transactStateEnum.getCode();
        return true;
    }

    /**
     * 交易失败
     * @param payState 支付状态
     * @return 返回支付状态
     */
    private BillTransactStateEnum getTransactStateByPayState(Integer payState) {
        switch (PaymentState.valueOfByCode(payState)){
            case 待支付:
                return BillTransactStateEnum.待交易;
            case 支付中:
                return BillTransactStateEnum.交易中;
            case 支付成功:
                return BillTransactStateEnum.交易成功;
            case 已关闭:
                return BillTransactStateEnum.交易已关闭;
            case 已撤销:
            case 支付失败:
            default:
                return BillTransactStateEnum.交易失败;
        }
    }
}
