package com.wishare.contract.apps.service.revision.remind;

import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;

import java.util.Set;

public interface ContractRemindMessageSendService {

    /**
     * 发送PC端提醒/预警通知
     *
     * @param userIds
     * @param info
     */
    void sendPCNoticeMessage(Set<String> userIds, ContractAndPlanInfoV info);

    /**
     * 发送中建端提醒/预警消息
     *
     * @param userThirdPartyIds
     * @param info
     */
    void sendZJNoticeMessage(Set<String> userThirdPartyIds, ContractAndPlanInfoV info);

    void sendPCNoticeMessage(String userId, Boolean flag, String reason);

    void sendZJNoticeMessage(String userThirdPartyId, Boolean flag, String reason);
}
