package com.wishare.finance.domains.configure.subject.command.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据id查询科目类型命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectCategoryByIdQuery {

    /**
     * 科目类型id
     */
    private Long id;
}
