package com.wishare.finance.domains.configure.subject.command.subject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 创建科目命令
 *
 * @author yancao
 */
@Getter
@Setter
public class CreateSubjectCommand {

    /**
     * 科目id
     */
    private Long id;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目类型id
     */
    private Long categoryId;

    /**
     * 父科目id
     */
    private Long parentId;

    /**
     * 科目体系id
     */
    private Long subjectSystemId;

    /**
     * 是否生效：0否，1是
     */
    private Integer disabled;

    /**
     * 辅助核算
     */
    private String auxiliaryCount;

    /**
     * 是否税费科目 0否 1是 默认0
     */
    private Integer existTax;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

}
