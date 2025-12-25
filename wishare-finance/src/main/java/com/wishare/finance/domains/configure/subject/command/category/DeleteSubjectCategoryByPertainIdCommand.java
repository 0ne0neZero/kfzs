package com.wishare.finance.domains.configure.subject.command.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 根据科目体系id删除科目类型命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteSubjectCategoryByPertainIdCommand {

    /**
     * 科目类型id
     */
    private Long pertainId;


}
