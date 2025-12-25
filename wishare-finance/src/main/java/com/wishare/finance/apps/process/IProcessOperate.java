package com.wishare.finance.apps.process;

import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.fo.BusinessInfoF;
import com.wishare.owl.exception.OwlBizException;

/**
 * OA流程操作顶层接口
 */
public interface IProcessOperate<T> {

    /**
     * 创建流程，返回OA审批页面地址
     *
     * @param mainDataId
     * @param processType
     * @return
     */
    String createProcess(T mainDataId, BusinessProcessType processType);

    /**
     * 创建OA审批页面地址
     *
     * @param requestId
     * @return
     */
    String validateFw(String requestId);

    /**
     * 处理审批回到的状态
     *
     * @param mainDataId
     * @param reviewStatus
     * @return
     */
    Boolean handleReviewStatus(T mainDataId, Integer reviewStatus);

    /**
     * 创建OA流程表单信息
     *
     * @param mainDataId
     * @return
     */
    default BusinessInfoF buildBusinessInfoF(T mainDataId) {
        throw new OwlBizException("未实现审批表单组转");
    }

    /**
     * 审批驳回回调
     *
     * @param mainDataId
     */
    default void reject(T mainDataId) {
        throw new OwlBizException("未实现审批驳回回调");
    }

    /**
     * 审批通过回调
     *
     * @param mainDataId
     */
    default void approved(T mainDataId) {
        throw new OwlBizException("未实现审批通过回调");
    }

    /**
     * 审批中回调
     *
     * @param mainDataId
     */
    default void approving(T mainDataId) {
        throw new OwlBizException("未实现审批中回调");
    }

}
