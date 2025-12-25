package com.wishare.contract.apps.service.event.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hhb
 * @describe
 * @date 2025/10/29 11:33
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("内部审批消息通知体")
public class ApproveProcessCompleteMsg {

    /**
     * 流程实例ID
     */
    private String procInstId;
    /**
     * 业务ID
     */
    private String businessKey;
    /**
     * 业务类型
     */
    private String type;
    /**
     * 流程状态
     */
    private String businessStatus;
}