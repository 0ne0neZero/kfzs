package com.wishare.finance.domains.configure.subject.command.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据名称查询科目体系
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectSystemByNameQuery {

    /**
     * 体系名称
     */
    private String name;

}
