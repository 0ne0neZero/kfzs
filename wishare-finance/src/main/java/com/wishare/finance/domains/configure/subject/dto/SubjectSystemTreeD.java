package com.wishare.finance.domains.configure.subject.dto;

import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 科目体系信息
 *
 * @author yancao
 */
@Getter
@Setter
public class SubjectSystemTreeD {

    /**
     * 法定单位id
     */
    private String pertainId;

    /**
     * 科目体系
     */
    private List<SubjectSystemE> subjectSystemList;

}
