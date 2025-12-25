package com.wishare.finance.domains.configure.subject.command.subject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 更新科目命令
 *
 * @author yancao
 */
@Getter
@Setter
public class UpdateSubjectCommand {

    /**
     * 科目id
     */
    private Long id;

    /**
     * 父科目id
     */
    private Long parentId;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目类型id
     */
    private Long categoryId;

    /**
     * 科目体系id
     */
    private Long subjectSystemId;

    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;

    /**
     * 辅助核算
     */
    private String auxiliaryCount;

    /**
     * 现金类别： 0无，1现金，2银行，3现金等价物
     */
    private Integer cashType;

    /**
     * 是否税费科目 0否 1是 默认0
     */
    private Integer existTax;

    /**
     * 租户id
     */
    private String tenantId;

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
