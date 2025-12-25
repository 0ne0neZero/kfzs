package com.wishare.finance.domains.configure.subject.command.category;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 创建科目类型命令
 *
 * @author yancao
 */
@Getter
@Setter
public class CreateSubjectCategoryCommand {

    /**
     * id
     */
    private Long id;

    /**
     * 科目类型名称
     */
    private String categoryName;

    /**
     * 父科目类型id
     */
    private Long parentId;

    /**
     * 科目体系id
     */
    private Long pertainId;

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
