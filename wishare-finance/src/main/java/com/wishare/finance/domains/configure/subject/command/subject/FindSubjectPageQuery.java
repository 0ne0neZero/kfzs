package com.wishare.finance.domains.configure.subject.command.subject;

import com.wishare.starter.beans.PageF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分页查询科目命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindSubjectPageQuery extends PageF {

    /**
     * 科目名称
     */
    private String subjectName;

    /**
     * 科目编码
     */
    private String subjectCode;

    /**
     * 科目类别id
     */
    private Long categoryId;

    /**
     * 科目体系id
     */
    private Long subjectSystemId;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
}
