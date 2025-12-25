package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.BillApproveRefundStateEnum;
import com.wishare.finance.domains.bill.support.ListFileVoTypeHandler;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 退款单表(RefundRecord)实体类
 * 退款单表(bill_refund)
 * @author makejava
 * @since 2022-09-08 09:28:55
 */
@Getter
@Setter
@TableName("bill_refund")
public class BillRefundE implements BillDetailBase {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer billType;
    /**
     * 退款编号
     */
    private String refundNo;
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
     * OTHER: 其他
     */
    private String refundChannel;
    /**
     * 退款方式(0线上，1线下)
     */
    private Integer refundWay;

    /**
     * 退款方式(1汇款，2支票，3其他，4现金，5线下-支付宝，6线下-微信,7-原路退回)
     */
    private String refundMethod;
    /**
     * 外部退款编号（支付宝退款单号，银行流水号等）
     */
    private String outRefundNo;
    /**
     * 退款金额（单位：分）
     */
    private Long refundAmount;

    @ApiModelProperty(value = "收款明细详细退款列表")
    @TableField(exist = false)
    private Map<Long, BigDecimal> gatherDetailList;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    /**
     * 审核记录id
     */
    private Long billApproveId;
    /**
     * 申请退款时间
     */
    private LocalDateTime approveTime;
    /**
     * 收费对象类型
     */
    private Integer refunderType;
    /**
     * 退款人ID
     */
    private String refunderId;
    /**
     * 退款人名称
     */
    private String refunderName;
    /**
     * 退款状态：0待退款，1退款中，2已退款，3未生效
     */
    private Integer state;

    @ApiModelProperty("附件")
    private String fileInfo;
    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;
    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 推凭状态
     */
    private Integer inferenceState;
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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 获取退款中状态
     * @return
     */
    public static List<Integer> getRefundingState(){
        return List.of(BillApproveRefundStateEnum.待退款.getCode(), BillApproveRefundStateEnum.退款中.getCode(),BillApproveRefundStateEnum.未生效.getCode());
    }

}

