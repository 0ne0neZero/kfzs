package com.wishare.finance.domains.bill.command;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 审核申请信息命令
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class BillApplyBatchCommand<T extends BillDetailBase> {

    /**
     * 账单id
     */
    private List<Long> billIds;

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
     * 审核详情
     */
    private List<T> details;

    /**
     * 扩展字段1
     */
    private String extField1;

    //操作id
    private Long operationId;


    /**
     * 操作备注
     */
    private String operationRemark;


    public BillApplyBatchCommand(List<Long> billIds, String reason,
                                 BillApproveOperateTypeEnum approveOperateType, String outApproveId,String supCpUnitId,Long operationId,String operationRemark) {
        this.billIds = billIds;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.outApproveId = outApproveId;
        this.supCpUnitId = supCpUnitId;
        this.operationId = operationId;
        this.operationRemark = operationRemark;
    }

    public BillApplyBatchCommand(List<Long> billIds, String reason,String extField1,
                                 BillApproveOperateTypeEnum approveOperateType, String outApproveId,String supCpUnitId,Long operationId,String operationRemark) {
        this.billIds = billIds;
        this.reason = reason;
        this.approveOperateType = approveOperateType;
        this.extField1 = extField1;
        this.outApproveId = outApproveId;
        this.supCpUnitId = supCpUnitId;
        this.operationId = operationId;
        this.operationRemark = operationRemark;
    }

    /**
     * 设置审核详情
     *
     * @param detail
     */
    public BillApplyBatchCommand initDetail(String detail) {
        switch (approveOperateType) {
            case 调整:
            case 减免:
                setDetails((List<T>) getAdjustDetails(detail));
                break;
            case 退款:
            case 收款单退款:
                setDetails((List<T>) getRefundDetails(detail));
                break;
            case 跳收:
                setDetails((List<T>) getJumpDetails(detail));
                break;
            case 冲销:
                setDetails((List<T>) getReverseDetails(detail));
                break;
            case 收款单冲销:
                setDetails((List<T>) getReverseDetails(detail));
                break;
        }
        return this;
    }

    /**
     * 获取调整申请详情
     *
     * @return
     */
    private List<BillAdjustE> getAdjustDetails(String detail) {
        return JSON.parseObject(detail, new TypeReference<>() {
        });
    }

    private List<BillRefundE> getRefundDetails(String detail) {
        return JSON.parseObject(detail, new TypeReference<>() {
        });
    }

    private List<BillJumpE> getJumpDetails(String detail) {
        return JSON.parseObject(detail, new TypeReference<>() {
        });
    }

    private List<BillReverseE> getReverseDetails(String detail) {
        return JSON.parseObject(detail, new TypeReference<>() {
        });
    }
}
