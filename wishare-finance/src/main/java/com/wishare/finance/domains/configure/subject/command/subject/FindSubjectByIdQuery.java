package com.wishare.finance.domains.configure.subject.command.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据id查询科目命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectByIdQuery {

    /**
     * 科目id
     */
    private Long id;

}
