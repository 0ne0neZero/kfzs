package com.wishare.finance.domains.configure.chargeitem.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.chargeitem.fo.ChargeItemTreeF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemTreeV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeNameV;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.ChargeItemMapper;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.DataShowedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.utils.TreeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 费项Repository
 *
 * @author yancao
 */
@Repository
public class ChargeItemRepository extends ServiceImpl<ChargeItemMapper, ChargeItemE> {

    /**
     * 根据id获取当前费项以及所有子费项
     *
     * @param idList idList
     * @return List
     */
    public List<ChargeItemE> getChargeItemByPath(List<Long> idList, String tenantId) {
        return baseMapper.getChargeItemByPath(idList, tenantId);
    }

    /**
     * 根据id获取当前费项以及所有子费项
     *
     * @param idList idList
     * @return List
     */
    public List<ChargeItemE> getChargeItemWithRateByPath(List<Long> idList, Wrapper<ChargeItemE> queryWrapper) {
        return baseMapper.getChargeItemWithRateByPath(idList,queryWrapper);
    }

    /**
     * 根据id获取费项和税率信息
     *
     * @param parentId parentId
     * @return ChargeItemE
     */
    public List<ChargeItemE> queryChargeItemByParentId(Long parentId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(parentId != null, ChargeItemE::getParentId, parentId);
        return list(queryWrapper);
    }

    /**
     * 分页查询费项信息
     *
     * @param page         page
     * @param queryWrapper queryWrapper
     * @return Page
     */
    public Page<ChargeItemE> queryChargeItemByPage(Page<ChargeItemE> page, Wrapper<ChargeItemE> queryWrapper) {
        return baseMapper.queryChargeItemByPage(page, queryWrapper);
    }

    /**
     * 根据费项名称获取费项
     *
     * @param currentId currentId
     * @param name      name
     * @param tenantId  tenantId
     * @return ChargeItemE
     */
    public ChargeItemE getChargeItemByName(Long currentId, String name, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getName, name);
        queryWrapper.ne(currentId != null, ChargeItemE::getId, currentId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 根据费项编码获取费项
     *
     * @param currentId currentId
     * @param code      code
     * @param tenantId  tenantId
     * @return ChargeItemE
     */
    public ChargeItemE getChargeItemByCode(Long currentId, String code, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getCode, code);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.ne(currentId != null, ChargeItemE::getId, currentId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 获取正常费项
     *
     * @param filterIdList 过滤id
     * @param attribute    费项属性
     * @param tenantId     租户id
     * @return List
     */
    public List<ChargeItemE> getNormalChargeItem(List<Long> filterIdList, List<String> attribute,List<String> type, String tenantId,
                                                 List<String> parentIdList, List<String> specialParentIdList) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getShowed, DataShowedEnum.显示.getCode());
        queryWrapper.eq(ChargeItemE::getDisabled, DataDisabledEnum.启用.getCode());
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.notIn(!CollectionUtils.isEmpty(filterIdList), ChargeItemE::getId, filterIdList);
        queryWrapper.in(!CollectionUtils.isEmpty(attribute), ChargeItemE::getAttribute, attribute);
        queryWrapper.in(!CollectionUtils.isEmpty(type) , ChargeItemE::getType, type);
        if (!CollectionUtils.isEmpty(parentIdList) && !CollectionUtils.isEmpty(specialParentIdList)) {
            // 立项管理特殊逻辑,展示 二三级费项,并支持特殊费项展示第四级
            queryWrapper.and(x -> x.in(ChargeItemE::getId, parentIdList)
                    .or()
                    .in(ChargeItemE::getParentId, parentIdList)
                    .apply("JSON_LENGTH(path) > 1 AND JSON_LENGTH(path) <= 3")
                    .or()
                    .in(ChargeItemE::getParentId, specialParentIdList)
            );
        }
        List<ChargeItemE> list = this.list(queryWrapper);
        //attribute = 6 合同清单录入费项支持往来款
        if(!CollectionUtils.isEmpty(attribute) && attribute.contains("6")){
            LambdaQueryWrapper<ChargeItemE> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ChargeItemE::getShowed, DataShowedEnum.显示.getCode());
            queryWrapper1.eq(ChargeItemE::getDisabled, DataDisabledEnum.启用.getCode());
            queryWrapper1.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
            queryWrapper1.eq(ChargeItemE::getDeleted,0);
            queryWrapper1.like(ChargeItemE::getPath,"103");
            queryWrapper1.eq(ChargeItemE::getAttribute,3);
            List<ChargeItemE> list1 = this.list(queryWrapper1);
            if(ObjectUtils.isNotEmpty(list1)){
                list.addAll(list1);
            }
        }
        return list;
    }

    /**
     * 根据parentId获取费项
     *
     * @param currentId 费项id
     * @param tenantId  租户id
     * @return ChargeItemE
     */
    public ChargeItemE getChargeItemByParent(Long currentId, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getParentId, currentId);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    /**
     * 根据parentId集合获取费项
     *
     * @param parentIdList 费项id
     * @param tenantId     租户id
     * @return ChargeItemE
     */
    public List<ChargeItemE> getChargeItemByParentIdList(List<Long> parentIdList, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!CollectionUtils.isEmpty(parentIdList), ChargeItemE::getParentId, parentIdList);
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        return this.list(queryWrapper);
    }

    /**
     * 获取过滤的费项以及子费项
     *
     * @param filterId filterId
     * @return List
     */
    public List<ChargeItemE> getFilterChargeItemList(Long filterId, String tenantId) {
        return baseMapper.getFilterChargeItemList(filterId, tenantId);
    }

    public List<ChargeItemE> getLastLevelChargeItemList(String tenantId) {
        return baseMapper.getLastLevelChargeItemList(tenantId);
    }

    /**
     * 根据名称集合获取费项
     *
     * @param nameList nameList
     * @param tenantId tenantId
     * @return List
     */
    public List<ChargeItemE> getChargeItemByNameList(List<String> nameList, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.in(!CollectionUtils.isEmpty(nameList), ChargeItemE::getName, nameList);
        queryWrapper.eq(ChargeItemE::getDisabled, DataDisabledEnum.启用.getCode());
        return list(queryWrapper);
    }

    /**
     * 获取所有末级费项
     *
     *
     * @param typeList 费项类型
     * @param attributeList 费项属性
     * @return List
     */
    public List<ChargeItemE> queryLastStage(List<String> typeList,String tenantId, List<String> attributeList) {
        return baseMapper.queryLastStage(typeList,tenantId, attributeList);
    }

    /**
     * 根据编码集合获取费项
     *
     * @param codeList codeList
     * @return List
     */
    public List<ChargeItemE> queryByCodeList(List<String> codeList) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(!CollectionUtils.isEmpty(codeList), ChargeItemE::getCode, codeList);
        return list(queryWrapper);
    }

    /**
     * 根据是否末级字段获取费项
     *
     * @param lastLevel lastLevel
     * @param tenantId  tenantId
     * @return List
     */
    public List<ChargeItemE> queryByLastLevel(Integer lastLevel, String tenantId) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(tenantId), ChargeItemE::getTenantId, tenantId);
        queryWrapper.eq(ChargeItemE::getLastLevel, lastLevel);
        queryWrapper.eq(ChargeItemE::getDisabled, DataDisabledEnum.启用.getCode());
        return list(queryWrapper);
    }

    /**
     * 根据层级和名称获取费项
     *
     * @param level 层级
     * @param name  名称
     * @return ChargeItemE
     */
    public ChargeItemE queryChargeByLevelAndName(Long currentId, int level, String name) {
        return baseMapper.queryChargeByLevelAndName(currentId,level, name);
    }

    /**
     * 根据父费项获取最大费项编码
     *
     * @param parentId 父费项id
     * @return String
     */
    public String queryMaxCodeByParentId(Long parentId) {
        return baseMapper.queryMaxCodeByParentId(parentId);
    }

    /**
     * 分页获取末级费项
     *
     * @param page page
     * @param queryModel 查询条件
     * @return Page
     */
    public Page<ChargeItemE> queryLastStageByPage(Page<ChargeItemE> page, QueryWrapper<ChargeItemE> queryModel) {
        return baseMapper.queryLastStageByPage(page, queryModel);
    }

    /**
     * 分页获取未关联税目的费项
     *
     * @param page 分页参数
     * @param queryModel 查询参数
     * @param lastStage 是否只查询末级
     * @return Page
     */
    public Page<ChargeItemE> queryTaxItemNotRelationByPage(Page<ChargeItemE> page, QueryWrapper<?> queryModel, Integer lastStage) {
        return baseMapper.queryTaxItemNotRelationByPage(page,queryModel,lastStage);
    }


    /**
     * 获取费项树
     * @param chargeItemTreeF
     * @return
     */
    public List<ChargeItemTreeV> getTree(ChargeItemTreeF chargeItemTreeF) {
        List<ChargeItemE> chargeItems = list(new LambdaQueryWrapper<ChargeItemE>().like(ChargeItemE::getCode, chargeItemTreeF.getCode())
                .like(ChargeItemE::getName, chargeItemTreeF.getName())
                .in(ChargeItemE::getAttribute, chargeItemTreeF.getAttribute())
                .in(ChargeItemE::getType, chargeItemTreeF.getType()));
        ArrayList<ChargeItemTreeV> chargeItemTreeVoList = new ArrayList<>();
        for (ChargeItemE chargeItemE : chargeItems) {
            ChargeItemTreeV chargeItemTreeV = Global.mapperFacade.map(chargeItemE, ChargeItemTreeV.class);
            if (chargeItemE.getParentId() != null) {
                chargeItemTreeV.setPid(chargeItemE.getParentId());
            }
            chargeItemTreeVoList.add(chargeItemTreeV);
        }
        return TreeUtil.treeing(chargeItemTreeVoList);
    }

    /**
     * 根据查询条件获取费项集合
     * @return
     */
    public List<ChargeNameV> getChargeItemInfoList(List<String> nameList, List<Byte> attributeList, List<String> codeList) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        if(!CollectionUtils.isEmpty(codeList)){
            queryWrapper.in(ChargeItemE::getCode, codeList);
        }
        if(!CollectionUtils.isEmpty(attributeList)){
            queryWrapper.in(ChargeItemE::getAttribute, attributeList);
        }
        if(!CollectionUtils.isEmpty(nameList)){
            queryWrapper.in(ChargeItemE::getName, nameList);
        }
        List<ChargeItemE> chargeItems = list(queryWrapper);
        List<ChargeNameV> chargeNameVList = Global.mapperFacade.mapAsList(chargeItems, ChargeNameV.class);
        return chargeNameVList;

    }

    /**
     * 根据code获取费项信息
     *
     * @param code 费项code
     * @return 费项信息
     */
    public ChargeItemE getChargeItemByCode(String code) {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(code), ChargeItemE::getCode, code);
        return this.getOne(queryWrapper);
    }

    /**
     * 根据名称模糊查询末级费项信息
     *
     * @param names 费项code
     * @return 费项信息
     */
    public List<ChargeNameV> getChargeItemInfoList(List<String> names) {
        List<ChargeItemE> chargeItemInfoList = baseMapper.lastChargeItems(names);
        return Global.mapperFacade.mapAsList(chargeItemInfoList, ChargeNameV.class);
    }

    /**
     * 存在分成费项ShareParentId修改为空
     *
     * @param ShareParentId 分成费项父id
     */
    public void updateChargeItemShareParentId(Long ShareParentId) {
        LambdaUpdateWrapper<ChargeItemE> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(null != ShareParentId,ChargeItemE::getShareParentId, ShareParentId);
        wrapper.set(ChargeItemE::getShareParentId,null);
        this.update(wrapper);
    }


    /**
     * 查询是否有带违约金标识的费项
     * @return
     */
    public ChargeItemE getChargeItemIsOverdue() {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getIsOverdue, 1);
        queryWrapper.eq(ChargeItemE::getDeleted, 0);
        queryWrapper.eq(ChargeItemE::getDisabled, 0);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }


    public ChargeItemE getChargeItemBusinessFlag() {
        LambdaQueryWrapper<ChargeItemE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChargeItemE::getBusinessFlag, "0");
        queryWrapper.eq(ChargeItemE::getDeleted, 0);
        queryWrapper.eq(ChargeItemE::getDisabled, 0);
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }

    public Page<ChargeItemE> queryLastStageAndParentByPage(Page<ChargeItemE> page, QueryWrapper<ChargeItemE> queryModel) {
        return baseMapper.queryLastStageAndParentByPage(page, queryModel);
    }

    //根据体系ID及属性获取所有单元ID
    public List<String> getItemIdList(String tenantId, Integer attribute) {
        return baseMapper.getItemIdList(tenantId, attribute);
    }
}
