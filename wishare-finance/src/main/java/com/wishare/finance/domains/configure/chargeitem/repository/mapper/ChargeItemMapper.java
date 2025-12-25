package com.wishare.finance.domains.configure.chargeitem.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费项mapper
 *
 * @author yancao
 */
public interface ChargeItemMapper extends BaseMapper<ChargeItemE> {
    /**
     * 根据id获取当前费项以及所有子费项
     *
     * @param idList idList
     * @return List
     */
    List<ChargeItemE> getChargeItemByPath(@Param(value = "idList") List<Long> idList, @Param(value = "tenantId") String tenantId);

    /**
     * 根据id获取当前费项以及所有子费项
     *
     * @param idList idList
     * @return List
     */
    List<ChargeItemE> getChargeItemWithRateByPath(@Param(value = "idList") List<Long> idList,@Param(Constants.WRAPPER) Wrapper<ChargeItemE> queryWrapper);

    /**
     * 分页查询费项信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    Page<ChargeItemE> queryChargeItemByPage(Page<ChargeItemE> page, @Param(Constants.WRAPPER) Wrapper<ChargeItemE> queryWrapper);

    /**
     * 获取过滤的费项以及子费项
     *
     * @param filterId filterId
     * @param tenantId tenantId
     * @return List
     */
    List<ChargeItemE> getFilterChargeItemList(@Param(value = "filterId") Long filterId,
                                              @Param(value = "tenantId") String tenantId);

    /**
     * 获取最后一级费项
     *
     * @param tenantId tenantId
     * @return List
     */
    List<ChargeItemE> getLastLevelChargeItemList(@Param(value = "tenantId") String tenantId);

    /**
     * 获取所有末级费项
     *
     * @param typeList      费项类型
     * @param tenantId      tenantId
     * @param attributeList 属性
     * @return List
     */
    List<ChargeItemE> queryLastStage(@Param(value = "typeList") List<String> typeList,
                                     @Param(value = "tenantId") String tenantId,
                                     @Param(value = "attributeList") List<String> attributeList);

    /**
     * 分页获取末级费项
     *
     * @param page         page
     * @param queryWrapper 查询条件
     * @return Page
     */
    Page<ChargeItemE> queryLastStageByPage(Page<ChargeItemE> page, @Param(Constants.WRAPPER) Wrapper<ChargeItemE> queryWrapper);

    Page<ChargeItemE> queryLastStageAndParentByPage(Page<ChargeItemE> page, @Param(Constants.WRAPPER) Wrapper<ChargeItemE> queryWrapper);

    /**
     * 根据父费项id获取所有子费项(包括已删除的)
     *
     * @param parentId parentId
     * @return List
     */
    String queryMaxCodeByParentId(@Param(value = "parentId") Long parentId);

    /**
     * 根据层级和名称获取费项
     *
     * @param level 层级
     * @param name  名称
     * @return ChargeItemE
     */
    ChargeItemE queryChargeByLevelAndName(@Param(value = "currentId") Long currentId, @Param(value = "level") int level, @Param(value = "name") String name);

    /**
     * 分页获取未关联税目的费项
     *
     * @param page 分页参数
     * @param queryModel 查询参数
     * @param lastStage 是否只查询末级
     * @return Page
     */
    Page<ChargeItemE> queryTaxItemNotRelationByPage(Page<ChargeItemE> page,
                                                    @Param(Constants.WRAPPER)QueryWrapper<?> queryModel,
                                                    @Param(value = "lastStage")Integer lastStage);
    /**
     * 获取末级费项信息
     *
     * @param names 费项名称（模糊查询）
     * @return 费项信息
     */
    List<ChargeItemE> lastChargeItems( @Param(value = "names")List<String> names);

    //根据体系ID及属性获取所有单元ID
    List<String> getItemIdList(@Param(value = "tenantId") String tenantId, @Param(value = "attribute")Integer attribute);
}
