package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 结算记录表(SettleRecord)实体类
 *
 * @author makejava
 * @since 2022-09-05 19:31:12
 */
@Getter
@Setter
@TableName("bill_settle")
@Accessors(chain = true)
public class BillSettleE {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单 66全部）
     */
    private Integer billType;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 收款/付款单id
     */
    private Long gatherBillId;
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
     * 票据id
     */
    @TableField( updateStrategy = FieldStrategy.IGNORED)
    private Long invoiceReceiptId;
    /**
     * 减免说明列表
     [{
     "outDiscountId": "外部减免id",
     "discountType": "减免类型",
     "amount": 100, //减免金额（分）
     "discription": "减免说明"
     }]
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
     * 付款方id
     */
    private String payerId;
    /**
     * 付款方名称
     */
    private String payerName;
    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 收费对象属性（1个人，2企业）
     */
    @TableField(exist = false)
    private Integer payerLabel;
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
     * 初始化结算信息
     */
    public void init(){
        //自动加载结算账单id
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_SETTLE);
        }
        //自动加载账单编号
        if (settleNo == null){
            settleNo = IdentifierFactory.getInstance().serialNumber("settle_no", "JS", 20);
        }
    }
}

