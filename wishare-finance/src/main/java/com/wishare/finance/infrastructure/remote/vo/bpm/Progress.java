package com.wishare.finance.infrastructure.remote.vo.bpm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Progress {

    private String nodeId;
    private String taskId;
    private String approvalMode;
    private String nodeType;
    private String name;
    private User user;
    private Object action;
    private Object comment;
    private Object result;
    private Object startTime;
    private Object finishTime;
}
