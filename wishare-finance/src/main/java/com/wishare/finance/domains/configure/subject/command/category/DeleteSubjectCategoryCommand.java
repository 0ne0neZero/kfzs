package com.wishare.finance.domains.configure.subject.command.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 删除科目命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteSubjectCategoryCommand {

    /**
     * 科目类型id
     */
    private Long id;

}
