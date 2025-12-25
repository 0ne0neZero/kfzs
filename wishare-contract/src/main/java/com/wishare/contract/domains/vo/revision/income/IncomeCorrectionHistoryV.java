package com.wishare.contract.domains.vo.revision.income;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author hhb
 * @describe
 * @date 2025/11/21 17:25
 */
@Data
public class IncomeCorrectionHistoryV {

    //编号
    private Integer no;
    //修正ID
    private String id;
    //修正字段
    private String correctionDesc;
    //修正原因
    private String correctionReason;
    //创建人
    private String creatorName;
    //创建时间
    private LocalDateTime gmtCreate;
    //修正状态
    private Integer correctionStatus;
    //修正状态描述
    private String correctionStatusDesc;
    //BPM流程ID
    private String bpmProcInstId;
    //是否可编辑
    private Boolean isShowEdit;
    //是否可删除
    private Boolean isShowDeleted;
    //是否展示发起审批
    private Boolean isShowApprovalBpm;
    //是否展示查看审批记录
    private Boolean isShowBpmDetail;
}
