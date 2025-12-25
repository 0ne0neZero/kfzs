package com.wishare.finance.domains.configure.businessunit.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitAccountV;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitDetailE;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务单元mapper
 *
 * @author
 */
public interface BusinessUnitDetailMapper extends BaseMapper<BusinessUnitDetailE> {

    List<Long> queryBusinessUnitIdByRelevanceId(@Param("id") Long id);

    List<BusinessUnitAccountV> queryAccountByUnitId(@Param("unitIds") List<Long> unitIds);
}
