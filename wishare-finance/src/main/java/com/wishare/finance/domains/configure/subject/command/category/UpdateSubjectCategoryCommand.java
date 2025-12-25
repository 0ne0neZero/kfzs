package com.wishare.finance.domains.configure.subject.command.category;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 更新科目类型命令
 *
 * @author yancao
 */
@Getter
@Setter
public class UpdateSubjectCategoryCommand {

    /**
     * 科目类型id
     */
    private Long id;

    /**
     * 科目类型名称
     */
    private String categoryName;

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
