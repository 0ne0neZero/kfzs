package com.wishare.finance.domains.configure.subject.dto;

import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目实体
 *
 * @author yancao
 */
@Getter
@Setter
public class SubjectD extends SubjectE {

    /**
     * 科目类型
     */
    private String categoryName;
}
