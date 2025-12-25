package com.wishare.finance.domains.configure.subject.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.subject.entity.SubjectSystemE;
import org.springframework.stereotype.Repository;

/**
 * 财务科目体系表 Mapper 接口
 *
 * @author yancao
 */
@Repository
public interface SubjectSystemMapper extends BaseMapper<SubjectSystemE> {

    /**
     * 根据法定单位id分页获取关联的科目体系
     *
     * @param page page
     * @param statutoryBodyId 法定单位id
     * @return Page
     */
    Page<SubjectSystemE> queryPageByStatutoryBodyId(Page<Object> page, Long statutoryBodyId);
}
