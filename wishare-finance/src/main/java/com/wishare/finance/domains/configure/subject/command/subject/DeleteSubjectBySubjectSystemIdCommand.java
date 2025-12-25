package com.wishare.finance.domains.configure.subject.command.subject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据科目体系id删除科目命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor

public class DeleteSubjectBySubjectSystemIdCommand {

    /**
     * 科目体系id
     */
    private Long subjectSystemId;

}
