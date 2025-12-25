package com.wishare.finance.domains.configure.subject.command.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 根据体系id查询科目类型命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectCategoryByPertainIdQuery {

    /**
     * 体系id
     */
    private List<Long> pertainIdList;

    /**
     * 租户id
     */
    private String tenantId;

}
