package com.wishare.contract.apps.remote.vo.charge;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hhb
 * @describe
 * @date 2025/10/28 18:20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ApproveFilter {

    /*
    审批系统
     */
    private String approveSystem;
    /*
    审批流程,对应bpm的formId
     */
    private String approveRule;
    /*
    附件id
     */
    private String formItemFileId;

    /*
    是否可以发起审批
    */
    private Boolean isAllowApprove;

    /*
        1:走审批流审核,2:不需要审核,0:走变更审核页审核
    */
    private Integer approveWay;

    public Integer getApproveWay() {
        return approveWay == null ? 0 : approveWay;
    }
}