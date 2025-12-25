package com.wishare.finance.domains.configure.businessunit.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountBankDto;
import com.wishare.finance.domains.bill.dto.BusinessUnitAccountDto;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务单元mapper
 *
 * @author
 */
public interface BusinessUnitMapper extends BaseMapper<BusinessUnitE> {

    /**
     * 分页查询业务单元树信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<BusinessUnitE> queryBusinessUnitByPage(Page<BusinessUnitE> page, @Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper);

    /**
     * 根据id获取当前业务单元以及所有子业务单元
     *
     * @param idList idList
     * @return List
     */
    List<BusinessUnitE> getBusinessUnitWithRateByPath(@Param(value = "idList") List<Long> idList,@Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper);

    /**
     * 树查询业务单元信息
     *
     * @param queryWrapper queryWrapper
     * @return Page
     */
    List<BusinessUnitE> queryBusinessUnitByTree(@Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper);

    /**
     * 分页查询业务单元list
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<BusinessUnitE> queryBusinessUnitByListPage(Page<BusinessUnitE> page, @Param(Constants.WRAPPER) Wrapper<BusinessUnitE> queryWrapper);

    /**
     * 根据关联id查询业务单元信息
     *
     * @param id   关联id
     * @return {@link List}<>{@link BusinessUnitAccountDto}</>
     */
    List<BusinessUnitAccountDto> queryBusinessUnitWithByStatutoryBodysId(@Param("id") Long id);

    /**
     * 根据业务单元id查询银行账户信息关联
     * @return
     */
    List<BusinessUnitAccountBankDto> queryBusinessUnitAccount(Long id);
}
