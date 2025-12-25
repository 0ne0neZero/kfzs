package com.wishare.finance.domains.bill.command;

import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/8
 * @Description: 新增退款command
 */
@Getter
@Setter
public class AddBillRefundCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 账单id
     */
    private Long billId;
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
     * 外部退款编号（支付宝退款单号，银行流水号等）
     */
    private String outRefundNo;
    /**
     * 退款金额（单位：分）
     */
    private Long refundAmount;
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
     * 租户id
     */
    private String tenantId;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 创建人ID
     */
    private String creator;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 操作人姓名
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;

    /**
     * 构建退款实体
     * @return
     */
    public BillRefundE getBillRefund(Long billApproveId) {
        BillRefundE billRefundE = Global.mapperFacade.map(this, BillRefundE.class);
        billRefundE.setBillApproveId(billApproveId);
        billRefundE.setState(BillApproveRefundStateEnum.待退款.getCode());
        billRefundE.setApproveTime(LocalDateTime.now());
        return billRefundE;
    }

    /**
     * 针对该笔退款单生成一条审批记录
     * @return
     */
    public BillApproveE getBillApprove() {
        BillApproveE billApproveE = new BillApproveE();
        billApproveE.setId(IdentifierFactory.getInstance().generateLongIdentifier("bill_approve_id"));
        billApproveE.setBillId(this.billId);
        billApproveE.setOperateType(BillApproveOperateTypeEnum.退款.getCode());
        billApproveE.setBillType(BillTypeEnum.应收账单.getCode());
        billApproveE.setApprovedState(BillApproveStateEnum.待审核.getCode());
        return billApproveE;
    }


}
