package com.wishare.finance.domains.configure.subject.command.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 删除科目命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class DeleteSubjectCommand {

    /**
     * 科目id
     */
    private Long id;

}
