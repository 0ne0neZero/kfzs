package com.wishare.finance.domains.configure.subject.command.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 删除科目体系命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteSubjectSystemCommand {

    /**
     * 科目体系id
     */
    private Long id;
}
