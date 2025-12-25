package com.wishare.finance.infrastructure.remote.vo.bpm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 历史表单流程模型表，每次保存/发布新增一条记录
 * </p>
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProcessProgressV {

    private List<String> actionVo;
    private Boolean showBtnTransfer;
    private Boolean showBtnBack;
    private Boolean showBtnAddlog;
    private Boolean showBtnAccept;
    private Boolean showBtnNotAccept;
    private Boolean showBtnCommit;
    private Boolean showBtnCommitAgain;
    private Boolean showBtnRevoke;
    private String instanceId;
    private Object formItems;
    private Object formData;
    private List<Progress> progress;
    private String processDefName;
    private Object version;
    private String status;
    private Object result;
    private Object staterUser;
    private Object starterDept;
    private Object startTime;
    private String appCode;
    private String appName;
    private String sysStatus;


}
