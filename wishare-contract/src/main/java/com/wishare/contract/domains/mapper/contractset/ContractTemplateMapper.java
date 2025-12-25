package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.contract.apps.fo.contractset.PageContractTemplateF;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractTemplateMapper extends BaseMapper<ContractTemplateE> {

    IPage<ContractTemplateTreeV> queryByPage(Page<PageContractTemplateF> pageF,
                                             @Param("ew") QueryWrapper<PageContractTemplateF> queryModel,
                                             @Param("tenantId") String tenantId);

    List<ContractTemplateTreeV> queryByWrapper(@Param("ew") QueryWrapper<PageContractTemplateF> queryModel,
                                               @Param("tenantId") String tenantId,
                                               @Param("id") Long id);

    List<ContractTemplateTreeV> queryByPath(@Param("ew") QueryWrapper<PageContractTemplateF> queryModel,
                                            @Param("parentIdList") List<Long> parentIdList,
                                            @Param("tenantId") String tenantId);
}
