package com.wishare.finance.domains.configure.businessunit.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountBankDto;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountDto;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.mapper.BusinessUnitMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务单元Repository
 *
 * @author
 */
@Repository
public class BusinessUnitRepository extends ServiceImpl<BusinessUnitMapper, BusinessUnitE> {
    /**
     * 分页查询业务单元树信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<BusinessUnitE> queryBusinessUnitByPage(Page<BusinessUnitE> page, @Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper) {
        return baseMapper.queryBusinessUnitByPage(page, queryWrapper);
    }

    /**
     * 根据id获取当前业务单元以及所有子业务单元
     *
     * @param idList idList
     * @return List
     */
    public List<BusinessUnitE> getBusinessUnitWithRateByPath(List<Long> idList, @Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper) {
        return baseMapper.getBusinessUnitWithRateByPath(idList,queryWrapper);
    }

    /**
     * 树查询业务单元信息
     *
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public List<BusinessUnitE> queryBusinessUnitByTree(@Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper) {
        return baseMapper.queryBusinessUnitByTree(queryWrapper);
    }

    /**
     * 分页查询业务单元list
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<BusinessUnitE> queryBusinessUnitByListPage(Page<BusinessUnitE> page, @Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper) {
        return baseMapper.queryBusinessUnitByListPage(page, queryWrapper);
    }

    public BusinessUnitE getByCode(String businessUnitCode) {
        return StringUtils.isNotBlank(businessUnitCode) ? getOne(new LambdaQueryWrapper<BusinessUnitE>().eq(BusinessUnitE::getCode, businessUnitCode)) : null;
    }

    public List<BusinessUnitE> getByCodes(List<String> businessUnitCodes) {
        return CollectionUtils.isNotEmpty(businessUnitCodes) ? list(new LambdaQueryWrapper<BusinessUnitE>().in(BusinessUnitE::getCode, businessUnitCodes)) : new ArrayList<>();
    }


    /**
     * 根据关联id查询业务单元信息
     *
     * @param id 关联id
     * @return {@link List}<>{@link BusinessUnitAccountDto}</>
     */
    public List<BusinessUnitAccountDto> queryBusinessUnitWithByStatutoryBodysId(Long id) {
        return baseMapper.queryBusinessUnitWithByStatutoryBodysId(id);
    }

    public List<BusinessUnitAccountBankDto> queryBusinessUnitAccount(Long id) {
        return baseMapper.queryBusinessUnitAccount(id);
    }
}
