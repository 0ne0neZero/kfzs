package com.wishare.finance.domains.configure.businessunit.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitAccountV;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitDetailE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.mapper.BusinessUnitDetailMapper;
import com.wishare.finance.domains.configure.businessunit.repository.mapper.BusinessUnitMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 业务单元Repository
 *
 * @author
 */
@Repository
public class BusinessUnitDetailRepository extends ServiceImpl<BusinessUnitDetailMapper, BusinessUnitDetailE> {

    public List<Long> queryBusinessUnitIdByRelevanceId(Long id) {
        return baseMapper.queryBusinessUnitIdByRelevanceId(id);
    }

    public List<BusinessUnitAccountV> queryAccountByUnitId(List<Long> unitIds) {
        return baseMapper.queryAccountByUnitId(unitIds);
    }
}
