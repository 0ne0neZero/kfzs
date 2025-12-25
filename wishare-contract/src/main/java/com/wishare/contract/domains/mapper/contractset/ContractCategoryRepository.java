package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.contractset.ContractCategoryE;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author yancao
 */
@Repository
public class ContractCategoryRepository extends ServiceImpl<ContractCategoryMapper, ContractCategoryE> {

    /**
     * 根据父类型id获取分类
     *
     * @param parentId parentId
     * @param tenantId tenantId
     * @return List
     */
    public List<ContractCategoryE> queryByParentId(Long parentId, String tenantId, Long currentId) {
        LambdaQueryWrapper<ContractCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCategoryE::getParentId, parentId);
        queryWrapper.ne(currentId != null, ContractCategoryE::getId, currentId);
        queryWrapper.eq(StringUtils.hasText(tenantId), ContractCategoryE::getTenantId, tenantId);
        return list(queryWrapper);
    }

    /**
     * 获取当前分类和所有下级分类
     *
     * @param id id
     * @return List
     */
    public List<ContractCategoryE> querySubLevel(Long id) {
        return baseMapper.querySubLevel(id);
    }

    /**
     * 根据租户id获取合同分类
     *
     * @param tenantId tenantId
     * @return List
     */
    public List<ContractCategoryE> queryByTenantId(String tenantId, String keyword) {
        LambdaQueryWrapper<ContractCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCategoryE::getTenantId, tenantId);
        if (Strings.isNotBlank(keyword)) {
            queryWrapper.like(ContractCategoryE::getName, keyword);
        }
        return list(queryWrapper);
    }

    /**
     * 根据id集合更新禁用状态
     *
     * @param idList   idList
     * @param disabled disabled
     */
    public void updateDisabledByIdList(List<Long> idList, Integer disabled) {
        LambdaUpdateWrapper<ContractCategoryE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ContractCategoryE::getId, idList);
        updateWrapper.set(ContractCategoryE::getDisabled, disabled);
        this.update(updateWrapper);
    }

    public List<ContractCategoryE> getByIdList(List<Long> idList) {
        LambdaQueryWrapper<ContractCategoryE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractCategoryE::getId, idList);
        return list(queryWrapper);
    }
}
