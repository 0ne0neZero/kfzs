package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractTemplateF;
import com.wishare.contract.apps.fo.contractset.PageContractTemplateF;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContractTemplateRepository extends ServiceImpl<ContractTemplateMapper, ContractTemplateE> {

    /**
     * 根据范本名称查询范本集
     */
    public List<ContractTemplateE> queryByName(String name, String tenantId) {
        LambdaQueryWrapper<ContractTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractTemplateE::getName, name);
        queryWrapper.eq(StringUtils.hasText(tenantId), ContractTemplateE::getTenantId, tenantId);
        return list(queryWrapper);
    }

    /**
     * 根据id集更新parentId
     */
    public void updateParentIdByIdList(List<Long> idList, Long parentId) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ContractTemplateE::getId, idList);
        updateWrapper.set(ContractTemplateE::getParentId, parentId);
        update(updateWrapper);
    }

    /**
     * 根据id集更新path
     */
    public void updatePathById(Long id, String path) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContractTemplateE::getId, id);
        updateWrapper.set(ContractTemplateE::getPath, path);
        update(updateWrapper);
    }

    /**
     * 分页查询范本列表
     */
    public IPage<ContractTemplateTreeV> queryByPage(Page<PageContractTemplateF> pageF,
                                                    QueryWrapper<PageContractTemplateF> queryModel,
                                                    String tenantId) {
        return baseMapper.queryByPage(pageF, queryModel, tenantId);
    }

    /**
     * 获取当前范本与下级所有范本
     */
    public List<ContractTemplateTreeV> queryByWrapper(QueryWrapper<PageContractTemplateF> queryModel,
                                                      String tenantId,
                                                      Long id) {
        return baseMapper.queryByWrapper(queryModel, tenantId, id);
    }

    /**
     * 根据分类id查询范本
     */
    public List<ContractTemplateE> queryByCategoryId(Long categoryId) {
        LambdaQueryWrapper<ContractTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractTemplateE::getCategoryId, categoryId);
        return list(queryWrapper);
    }

    /**
     * 根据范本id查询
     */
    public ContractTemplateE queryById(Long id) {
        LambdaQueryWrapper<ContractTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractTemplateE::getId, id);
        return getOne(queryWrapper);
    }

    /**
     * 根据id更新范本状态
     */
    public void updateStatusById(Long id, Integer status) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContractTemplateE::getId, id);
        updateWrapper.set(ContractTemplateE::getStatus, status);
        update(updateWrapper);
    }

    /**
     * 根据path包含查询
     */
    public List<ContractTemplateTreeV> queryByPath(QueryWrapper<PageContractTemplateF> queryModel, List<Long> parentIdList, String tenantId) {
        return baseMapper.queryByPath(queryModel, parentIdList, tenantId);
    }

    /**
     * 根据id更新 parentId、path
     */
    public void updateParentIdPathById(Long id, Long parentId, String path) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContractTemplateE::getId, id);
        updateWrapper.set(ContractTemplateE::getParentId, parentId);
        updateWrapper.set(ContractTemplateE::getPath, path);
        update(updateWrapper);
    }

    /**
     * 更新操作人
     */
    public void updateOperator(Long id, LocalDateTime time, String userId, String userName) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContractTemplateE::getId, id);
        updateWrapper.set(ContractTemplateE::getOperator, userId);
        updateWrapper.set(ContractTemplateE::getOperatorName, userName);
        updateWrapper.set(ContractTemplateE::getGmtModify, time);
        update(updateWrapper);
    }

    /**
     * 查询范本集
     */
    public List<ContractTemplateE> query(ContractTemplateF contractTemplateF, String tenantId) {
        LambdaQueryWrapper<ContractTemplateE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractTemplateE::getTenantId, tenantId);
        queryWrapper.eq(ContractTemplateE::getStatus, contractTemplateF.getStatus());
        queryWrapper.eq(ContractTemplateE::getCategoryId, contractTemplateF.getCategoryId());
        queryWrapper.like(ContractTemplateE::getName, contractTemplateF.getName());
        return list(queryWrapper);
    }

    /**
     * 根据分类id更新合同分类路径名
     */
    public void updateCategoryPathNameByCategoryId(String categoryPathName, Long categoryId) {
        LambdaUpdateWrapper<ContractTemplateE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ContractTemplateE::getCategoryId, categoryId);
        updateWrapper.set(ContractTemplateE::getCategoryPathName, categoryPathName);
        update(updateWrapper);
    }

}
