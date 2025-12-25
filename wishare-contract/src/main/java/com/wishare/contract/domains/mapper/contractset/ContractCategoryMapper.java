package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.domains.entity.contractset.ContractCategoryE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 合同持久化mapper
 *
 * @author yancao
 */
public interface ContractCategoryMapper extends BaseMapper<ContractCategoryE> {

    /**
     * 获取当前分类和所有下级分类
     *
     * @param id id
     * @return List
     */
    List<ContractCategoryE> querySubLevel(@Param("id") Long id);

    ContractCategoryE queryByBizCode(@Param("bizCode") String bizCode);
}
