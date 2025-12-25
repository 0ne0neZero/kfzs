package com.wishare.finance.domains.bill.command;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import lombok.Getter;
import lombok.Setter;

/**
 * 审核申请信息命令
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class BillApplyCommand<T> {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 审核原因
     */
    private String reason;

    /**
     * 外部审批标识
     */
    private String outApproveId;

    /**
     * 审核操作类型
     */
    private BillApproveOperateTypeEnum approveOperateType;

    /**
     * 扩展字段1
     */
    private String extField1;

    /**
     * 审核详情
     */
    private T detail;

    /**
     * 上次审核状态（处理初始审核可进行调整问题）
     */
    private Integer lastApprovedState;

    private Long operationId;

    /**
     * 操作备注
     */
    private String operationRemark;

    public BillApplyCommand(Long billId, String reason, BillApproveOperateTypeEnum approveOperateType, String outApproveId,String extField1) {
        this.outApproveId = outApproveId;
        this.billId = billId;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.extField1 = extField1;
    }


    public BillApplyCommand(Long billId, String reason, BillApproveOperateTypeEnum approveOperateType, String outApproveId,String extField1,String supCpUnitId) {
        this.outApproveId = outApproveId;
        this.billId = billId;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.extField1 = extField1;
        this.supCpUnitId = supCpUnitId;
    }

    public BillApplyCommand(Long billId, String reason, BillApproveOperateTypeEnum approveOperateType,
                            String outApproveId,String extField1, T detail) {
        this.outApproveId = outApproveId;
        this.billId = billId;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.extField1 = extField1;
        this.detail = detail;
    }

    public BillApplyCommand(Long billId, String reason, BillApproveOperateTypeEnum approveOperateType,
                            String outApproveId,String extField1, T detail,String supCpUnitId, String operationRemark) {
        this.supCpUnitId = supCpUnitId;
        this.outApproveId = outApproveId;
        this.billId = billId;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.extField1 = extField1;
        this.detail = detail;
        this.operationRemark = operationRemark;
    }

    /**
     * 设置审核详情
     * @param detail
     */
    public BillApplyCommand initDetail(String detail){
        switch (approveOperateType){
            case 调整:
                setDetail((T) getAdjustDetail(detail));
                break;
            case 退款:
                setDetail((T) getRefundDetail(detail));
                break;
            case 结转:
                setDetail((T) getCarryoverDetail(detail));
                break;
            case 收款单退款:
                setDetail((T) getRefundDetail(detail));
                break;
        }
        return this;
    }

    private void setDetail(T detail) {
        this.detail = detail;
    }

    /**
     * 获取退款申请详情
     * @return
     */
    private BillRefundE getRefundDetail(String detail){
        return JSON.parseObject(detail, BillRefundE.class);
    }

    /**
     * 获取结转申请详情
     * @return
     */
    private BillCarryoverE getCarryoverDetail(String detail){
        return JSON.parseObject(detail, BillCarryoverE.class);
    }

    /**
     * 获取调整申请详情
     * @return
     */
    private BillAdjustE getAdjustDetail(String detail){
        return JSON.parseObject(detail, BillAdjustE.class);
    }

}
