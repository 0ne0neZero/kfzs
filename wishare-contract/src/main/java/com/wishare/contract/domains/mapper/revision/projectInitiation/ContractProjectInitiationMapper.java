package com.wishare.contract.domains.mapper.revision.projectInitiation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectInitiationPageF;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectInitiationE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractProjectInitiationMapper extends BaseMapper<ContractProjectInitiationE> {

    IPage<ContractProjectInitiationE> selectFrontPage(Page<ContractProjectInitiationE> pageF, @Param("ew") QueryWrapper<ContractProjectInitiationPageF> queryModel);

}