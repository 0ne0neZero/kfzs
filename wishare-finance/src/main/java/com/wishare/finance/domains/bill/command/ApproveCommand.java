package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单审核命令
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class ApproveCommand {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * supCpUnitId
     */
    private String supCpUnitId;

    /**
     * 审核状态
     */
    private Integer approveState;

    /**
     * 申请驳回理由
     */
    private String rejectReason;

    /**
     * 已审核已结算账单调低和减免审核后操作：1退款，2转预收
     */
    private Integer approvedAction;

    /**
     * 转预收的费项id
     */
    private Long chargeItemId;

    /**
     * 转预收的费项名称
     */
    private String chargeItemName;

    /**
     * 外部审批标识
     */
    private String outApproveId;

    /**
     * 审核类型： 0内部审核，1外部审核
     */
    private Integer approveType;

    /**
     * 审核操作时间
     */
    private LocalDateTime operateTime;


    /**
     * 退款方式(1汇款，2支票，3其他，4现金，5线下-支付宝，6线下-微信,7-原路退款)
     */
    private String refundMethod;

    /**
     * 审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款
     */
    private Integer operateType;
}
