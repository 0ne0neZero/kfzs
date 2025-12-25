package com.wishare.finance.infrastructure.remote.vo.bpm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.infrastructure.remote.fo.bpm.OrgUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : willian fu
 * @date : 2022/9/11
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentVo  {

    private String id;

    private String type;

    private String taskId;

    private String commentType;

    private OrgUser user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
