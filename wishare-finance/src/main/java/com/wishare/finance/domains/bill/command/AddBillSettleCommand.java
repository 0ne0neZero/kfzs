package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/5
 * @Description: 新增结算记录command
 */
@Getter
@Setter
public class AddBillSettleCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 账单id
     */
    private Long billId;


    /**
     * 上级账单id
     */
    private String supCpUnitId;
    /**
     * 结算编号
     */
    private String settleNo;
    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;
    /**
     * 结算方式(0线上，1线下)
     */
    private Integer settleWay;
    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 开户行账号
     */
    private String bankAccount;
    /**
     * 外部结算编号（支付宝单号，银行流水号等）
     */
    private String outSettleNo;
    /**
     * 结转账单id
     */
    private Long carriedBillId;
    /**
     * 结转账单编号
     */
    private String carriedBillNo;
    /**
     * 结转金额
     */
    private Long carriedAmount;
    /**
     * 结算金额（单位：分）
     */
    private Long settleAmount;
    /**
     * 收款金额（单位：分）(合单支付时，收款金额 > 结算金额)
     */
    private Long payAmount;
    /**
     * 减免金额（单位：分）
     */
    private Long discountAmount;
    /**
     * 减免说明列表
     * [{
     *  "outDiscountId": "外部减免id",
     *  "discountType": "减免类型",
     *  "amount": 100, //减免金额（分）
     *  "discription": "减免说明"
     * }]
     */
    private String discounts;
    /**
     * 结算时间
     */
    private LocalDateTime settleTime;
    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;
    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;
    /**
     * 收费对象类型
     */
    private Integer payerType;
    /**
     * 收费对象属性（1个人，2企业）
     */
    private Integer payerLabel;

    /**
     * 收款方id
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;
    /**
     * 收款方手机号
     */
    private String payeePhone;
    /**
     * 付款方id
     */
    private String payerId;
    /**
     * 付款方名称
     */
    private String payerName;
    /**
     * 付款方手机号
     */
    private String payerPhone;
    /**
     * 备注
     */
    private String remark;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 费项id (结算转预收指定费项)
     */
    private Long chargeItemId;

    /**
     * 费项名称 (结算转预收指定费项)
     */
    private String chargeItemName;

    /**
     * 支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，
     * 13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端
     */
    private Integer paySource;

    //添加管家催收信息字段 2023-06-21 李彪 start
    @ApiModelProperty("自定义项1--管家id")
    private String extField1;

    @ApiModelProperty("自定义项2--管家名称")
    private String extField2;
    //添加管家催收信息字段 2023-06-21 李彪 end

    /**
     * 被结转账单支付方式
     */
    private String carriedBillPayChannel;

    @ApiModelProperty("结转规则")
    private String carryRule;

    @ApiModelProperty("自动结转方式")
    private Integer autoCarryRule;

    @ApiModelProperty(value = "银行流水号")
    private String bankFlowNo;

}
