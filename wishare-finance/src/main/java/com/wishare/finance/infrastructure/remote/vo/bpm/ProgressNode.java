package com.wishare.finance.infrastructure.remote.vo.bpm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.infrastructure.remote.fo.bpm.OrgUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgressNode {

    //节点ID
    private String nodeId;

    private String taskId;
    //审批类型
    private String approvalMode;
    //节点类型
    private String nodeType;
    //节点名称
    private String name;
    //节点相关人员
    private OrgUser user;
    //该节点动作操作类型
    private String action;
    //处理意见
    private List<TaskCommentVo> comment;
    //private List<>
    //处理结果
    private String result;
    //开始结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;
    private List<String> orgNames;

}
