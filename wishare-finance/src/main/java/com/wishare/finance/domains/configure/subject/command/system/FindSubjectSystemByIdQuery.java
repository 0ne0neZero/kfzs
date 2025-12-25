package com.wishare.finance.domains.configure.subject.command.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据id查询科目体系命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectSystemByIdQuery {

    /**
     * 科目id
     */
    private Long id;

}
