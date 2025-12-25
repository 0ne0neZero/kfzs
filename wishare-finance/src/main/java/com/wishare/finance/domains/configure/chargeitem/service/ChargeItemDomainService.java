package com.wishare.finance.domains.configure.chargeitem.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wishare.finance.apps.model.configure.chargeitem.fo.QueryNotRelationChargeItemF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.TaxRateInfoF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeNameV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ShareChargeV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.*;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessSegmentE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemBusinessE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.repository.BusinessSegmentRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemBusinessRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.infrastructure.conts.*;
import com.wishare.finance.infrastructure.easyexcel.BaseExcelDataListener;
import com.wishare.finance.infrastructure.easyexcel.ChargeItemData;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.FailInfo;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 费项service
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChargeItemDomainService {

    private final ChargeItemRepository chargeItemRepository;

    private final TaxRateRepository taxRateRepository;

    private final ChargeItemBusinessRepository chargeItemBusinessRepository;

    private final BusinessSegmentRepository businessSegmentRepository;

    /**
     * 新增费项
     *
     * @param command command
     * @return Long
     */
    @Transactional
    public ChargeItemE create(CreateChargeItemCommand command) {
        createValid(command);
        ChargeItemE chargeItemE = Global.mapperFacade.map(command, ChargeItemE.class);
        if (chargeItemE.getParentId() != null && chargeItemE.getParentId() != 0) {
            //父id不为空时设置path为父费项的path加上当前费项id
            ChargeItemE parentChargeItem = chargeItemRepository.getById(chargeItemE.getParentId());
            List<Long> path = JSON.parseArray(parentChargeItem.getPath(), Long.class);
            path.add(chargeItemE.getId());
            chargeItemE.setPath(JSON.toJSONString(path));

            //校验父费项是否末级
            Integer lastLevel = command.getLastLevel();
            if(lastLevel != null){
                Integer parentLastLevel = parentChargeItem.getLastLevel();
                if(parentLastLevel == 1){
                    throw BizException.throw400(ErrorMessage.CHARGE_NOT_ADD.getErrMsg());
                }
            }
        } else {
            //父id为空设置path为当前费项id
            chargeItemE.setPath(JSON.toJSONString(Collections.singletonList(chargeItemE.getId())));
        }
        if (command.getTaxRateId()!=null && command.getTaxRateId()!=0){
            TaxRateE taxRateE = taxRateRepository.getById(command.getTaxRateId());
            List<Long> taxRatePath = new ArrayList<>();
            taxRatePath.add(taxRateE.getTaxCategoryId());
            taxRatePath.add(command.getTaxRateId());
            chargeItemE.setTaxRatePath(JSON.toJSONString(taxRatePath));
        }
        //判断是否存在分成费项，存在就保存或修改
        saveOrUpdateChargeItemE(chargeItemE);
        chargeItemE.setShowed(DataShowedEnum.显示.getCode());
        chargeItemRepository.save(chargeItemE);
        // 判断是否中交环境  是否有业务类型信息
        if (EnvConst.ZHONGJIAO.equals(EnvData.config) && null != command.getBusinessCommands()){
            saveOrUpdateChargeItemBusiness(chargeItemE, command.getBusinessCommands());
        }
        // 拈花湾环境设置业务板块
        if (EnvConst.NIANHUAWAN.equals(EnvData.config) && StringUtils.isNotBlank(command.getBusinessSegmentCode())){
            saveOrUpdateBusinessSegment(chargeItemE, command.getBusinessSegmentCode(), command.getBusinessSegmentName());
        }
        return chargeItemE;
    }


    public void createValid(CreateChargeItemCommand command) {
        // 校验是否是违约金 若是则校验是否存在，是否允许新增
        if (null != command.getIsOverdue() && command.getIsOverdue().equals(1)) {
            ChargeItemE chargeItemIsOverdue = chargeItemRepository.getChargeItemIsOverdue();
            if (null != chargeItemIsOverdue) {
                throw BizException.throw400(ErrorMessage.CHARGE_OVERDUE_EXIST.getErrMsg() + "，费项名称:" + chargeItemIsOverdue.getName());
            }
        }
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            if (null != command.getBusinessFlag() && command.getBusinessFlag().equals("0")) {
                ChargeItemE chargeItemBusinessFlag = chargeItemRepository.getChargeItemBusinessFlag();
                if (null != chargeItemBusinessFlag) {
                    throw BizException.throw400(ErrorMessage.CHARGE_BUSINESS_FLAG_EXIST.getErrMsg() + "，费项名称:" + chargeItemBusinessFlag.getName());
                }
            }
        }
        //校验费项编码是否已存在
        checkCodeIsExist(null, command.getCode(), command.getTenantId());
        //检验同级费项名称是否重复
        checkNameIsExist(null, command.getName(),command.getParentId());
        // 拈花湾校验只有末级费项可以设置业务板块
        if (EnvConst.NIANHUAWAN.equals(EnvData.config)) {
            if (command.getLastLevel() != 1 && StringUtils.isNotBlank(command.getBusinessSegmentCode())) {
                throw BizException.throw400(ErrorMessage.CHARGE_BUSINESS_SEGMENT_LIMIT.getErrMsg());
            }
        }
    }

    public void saveOrUpdateBusinessSegment(ChargeItemE chargeItemE, String businessSegmentCode,
            String businessSegmentName) {
        BusinessSegmentE businessSegmentE = new BusinessSegmentE();
        businessSegmentE.setId(chargeItemE.getId());
        businessSegmentE.setBusinessSegmentCode(businessSegmentCode);
        businessSegmentE.setBusinessSegmentName(businessSegmentName);
        businessSegmentRepository.saveOrUpdate(businessSegmentE);
    }


    public void saveOrUpdateChargeItemBusiness(ChargeItemE chargeItemE, List<ChargeItemBusinessCommand> businessCommands) {
        chargeItemBusinessRepository.removeByChargeItemId(chargeItemE.getId());
        List<ChargeItemBusinessE> list = new ArrayList<>();
        for (ChargeItemBusinessCommand businessCommand : businessCommands) {
            ChargeItemBusinessE chargeItemBusinessE = getChargeItemBusinessE(chargeItemE, businessCommand);
            list.add(chargeItemBusinessE);
        }
        chargeItemBusinessRepository.saveBatch(list);
        // 如果不是末级
        List<ChargeItemE> childById = getChildById(chargeItemE.getId());
        if (CollectionUtils.isEmpty(childById)) {
            return;
        }
        for (ChargeItemE itemE : childById) {
            // 判断是否有业务类型
            List<ChargeItemBusinessE> chargeItemBusinessEList = chargeItemBusinessRepository.getChargeItemBusinessE(itemE.getId());
            if (CollectionUtils.isEmpty(chargeItemBusinessEList)){
                saveOrUpdateChargeItemBusiness(itemE, businessCommands);
            }
        }

    }

    @NotNull
    private static ChargeItemBusinessE getChargeItemBusinessE(ChargeItemE chargeItemE, ChargeItemBusinessCommand businessCommand) {
        ChargeItemBusinessE chargeItemBusinessE = new ChargeItemBusinessE();
        chargeItemBusinessE.setChargeItemId(chargeItemE.getId());
        chargeItemBusinessE.setChargeItemCode(chargeItemE.getCode());
        chargeItemBusinessE.setChargeItemName(chargeItemE.getName());
        chargeItemBusinessE.setDocumentId(businessCommand.getDocumentId());
        chargeItemBusinessE.setDocumentCode(businessCommand.getDocumentCode());
        chargeItemBusinessE.setDocumentName(businessCommand.getDocumentName());
        chargeItemBusinessE.setBusinessTypeId(businessCommand.getBusinessTypeId().toString());
        chargeItemBusinessE.setBusinessTypeCode(businessCommand.getBusinessTypeCode().toString());
        chargeItemBusinessE.setBusinessTypeName(businessCommand.getBusinessTypeName().toString());
        chargeItemBusinessE.setChangeName(businessCommand.getChangeName());
        chargeItemBusinessE.setChangeCode(businessCommand.getChangeCode());
        chargeItemBusinessE.setPaymentId(businessCommand.getPaymentId());
        chargeItemBusinessE.setPaymentName(businessCommand.getPaymentName());
        chargeItemBusinessE.setPaymentCode(businessCommand.getPaymentCode());
        chargeItemBusinessE.setSignedPaymentId(businessCommand.getSignedPaymentId());
        chargeItemBusinessE.setSignedPaymentName(businessCommand.getSignedPaymentName());
        chargeItemBusinessE.setSignedPaymentCode(businessCommand.getSignedPaymentCode());
        chargeItemBusinessE.setCostPaymentId(businessCommand.getCostPaymentId());
        chargeItemBusinessE.setCostPaymentName(businessCommand.getCostPaymentName());
        chargeItemBusinessE.setCostPaymentCode(businessCommand.getCostPaymentCode());
        return chargeItemBusinessE;
    }


    /**
     * 根据ID查费项名称
     *
     * @param chargeId chargeId
     * @return String
     */
    public String getNameById(Long chargeId) {
        ChargeItemE chargeItemE = chargeItemRepository.getById(chargeId);
        if (chargeItemE != null) {
            List<Long> idList = JSON.parseArray(chargeItemE.getPath(), Long.class);
            if (!CollectionUtils.isEmpty(idList)) {
                List<ChargeItemE> parentChargeItemList = chargeItemRepository.listByIds(idList);
                if (parentChargeItemList.size() != idList.size()) {
                    return "";
                }
                StringBuilder result = new StringBuilder();
                for (Long id : idList) {
                    List<ChargeItemE> collect = parentChargeItemList.stream().filter(s -> s.getId().equals(id)).collect(Collectors.toList());
                    result.append(result.length() == 0 ? collect.get(0).getName() : "-" + collect.get(0).getName());
                }
                return result.toString();
            }
            return chargeItemE.getName();
        }
        return "";
    }


    /**
     * 根据ID集合查费项名称
     *
     * @param itemIds itemIds
     * @return List<ChargeItemE>
     */
    public List<ChargeItemE> getNameByIds(List<Long> itemIds) {
        List<ChargeItemE> chargeItemEList = chargeItemRepository.listByIds(itemIds);
        for(ChargeItemE chargeItemE : chargeItemEList){
            if (chargeItemE != null) {
                List<Long> idList = JSON.parseArray(chargeItemE.getPath(), Long.class);
                if (!CollectionUtils.isEmpty(idList)) {
                    List<ChargeItemE> parentChargeItemList = chargeItemRepository.listByIds(idList);
                    if (parentChargeItemList.size() != idList.size()) {
                        continue;
                    }
                    StringBuilder result = new StringBuilder();
                    for (Long id : idList) {
                        List<ChargeItemE> collect = parentChargeItemList.stream().filter(s -> s.getId().equals(id)).collect(Collectors.toList());
                        result.append(result.length() == 0 ? collect.get(0).getName() : "-" + collect.get(0).getName());
                    }
                    chargeItemE.setName(result.toString());
                }
            }
        }
        return chargeItemEList;
    }


    /**
     * 根据ID查目标费项名称
     *
     * @param chargeId chargeId
     * @return String
     */
    public String getSimpleNameById(Long chargeId) {
        ChargeItemE chargeItemE = chargeItemRepository.getById(chargeId);
        if (chargeItemE != null) {
            return chargeItemE.getName();
        }
        return "";
    }

    /**
     * 获取所有正常的费项并过滤传入的费项
     *
     * @return List
     */
    public List<ChargeItemE> queryChargeItemList(Long filterId, List<String> attribute, List<String> type, String tenantId,
                                                 List<String> parentIdList, List<String> specialParentIdList) {
        //获取过滤的费项
        List<ChargeItemE> filterChargeItemList;
        if (filterId != null) {
            filterChargeItemList = chargeItemRepository.getFilterChargeItemList(filterId, tenantId);
        } else {
            filterChargeItemList = new ArrayList<>();
        }
        List<Long> filterIdList = filterChargeItemList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        return chargeItemRepository.getNormalChargeItem(filterIdList,attribute,type,tenantId, parentIdList, specialParentIdList);
    }

    /**
     * 获取所有正常的费项并过滤传入的费项
     *
     * @return List
     */
    public List<ChargeItemE> queryLevelChargeItemList(List<String> attribute, List<String> type, String tenantId) {
        //获取过滤的费项
        List<ChargeItemE> filterChargeItemList = chargeItemRepository.getLastLevelChargeItemList( tenantId);
        List<Long> filterIdList = filterChargeItemList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        return chargeItemRepository.getNormalChargeItem(filterIdList,attribute,type,tenantId, null, null);
    }

    /**
     * 更新费项
     *
     * @param command command
     */
    @Transactional
    public Boolean update(UpdateChargeItemCommand command) {

        if (null != command.getIsOverdue() && command.getIsOverdue().equals(1)) {
            ChargeItemE chargeItemIsOverdue = chargeItemRepository.getChargeItemIsOverdue();
            if (null != chargeItemIsOverdue && !chargeItemIsOverdue.getId().equals(command.getId())) {
                throw BizException.throw400(ErrorMessage.CHARGE_OVERDUE_EXIST.getErrMsg()  + "，费项名称:" + chargeItemIsOverdue.getName());
            }
        }
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            if (null != command.getBusinessFlag() && command.getBusinessFlag().equals("0")){
                ChargeItemE chargeItemBusinessFlag = chargeItemRepository.getChargeItemBusinessFlag();
                if (null != chargeItemBusinessFlag && !chargeItemBusinessFlag.getId().equals(command.getId())) {
                    throw BizException.throw400(ErrorMessage.CHARGE_BUSINESS_FLAG_EXIST.getErrMsg() + "，费项名称:" + chargeItemBusinessFlag.getName());
                }
            }
        }
        // 拈花湾校验只有末级费项可以设置业务板块
        if (EnvConst.NIANHUAWAN.equals(EnvData.config)) {
            if (command.getLastLevel() != 1 && StringUtils.isNotBlank(command.getBusinessSegmentCode())) {
                throw BizException.throw400(ErrorMessage.CHARGE_BUSINESS_SEGMENT_LIMIT.getErrMsg());
            }
        }
        Long currentId = command.getId();
        String tenantId = command.getTenantId();
        //旧的费项信息
        ChargeItemE oldChargeItem = chargeItemRepository.getById(currentId);
        //新的费项信息
        ChargeItemE newChargeItem = Global.mapperFacade.map(command, ChargeItemE.class);

        //改为末级节点时校验是否存在子级
        if(command.getLastLevel() !=null && command.getLastLevel() == 1){
            ChargeItemE existChargeItemE = chargeItemRepository.getChargeItemByParent(currentId, tenantId);
            //校验当前费项是否存在子费项
            if (existChargeItemE != null) {
                throw BizException.throw400(ErrorMessage.CHARGE_NOT_UPDATE_LAST_LEVEL.getErrMsg());
            }
        }

        //校验费项编码是否已存在
        checkCodeIsExist(currentId, command.getCode(), command.getTenantId());
        //检验同级费项名称是否重复,当名字没有修改时不做校验
        if (!oldChargeItem.getName().equals(newChargeItem.getName())) {
            checkNameIsExist(currentId, command.getName(),command.getParentId());
        }

        //parent不为空时校验当前费项是否存在子费项
        if (newChargeItem.getParentId() != null) {
            if (!newChargeItem.getParentId().equals(oldChargeItem.getParentId())) {
                ChargeItemE existChargeItemE = chargeItemRepository.getChargeItemByParent(currentId, tenantId);
                //校验当前费项是否存在子费项
                if (existChargeItemE != null) {
                    throw BizException.throw400(ErrorMessage.CHARGE_EXIST_SUB.getErrMsg());
                }
                //获取目标费项并设置新费项的path
                ChargeItemE targetChargeItem = chargeItemRepository.getById(newChargeItem.getParentId());
                List<Long> targetPath = JSON.parseArray(targetChargeItem.getPath(), Long.class);
                targetPath.add(newChargeItem.getId());
                newChargeItem.setPath(JSON.toJSONString(targetPath));

                //校验新的父费项是否末级费项
                Integer lastLevel = command.getLastLevel();
                if(lastLevel != null){
                    Integer parentLastLevel = targetChargeItem.getLastLevel();
                    if(parentLastLevel == 1){
                        throw BizException.throw400(ErrorMessage.CHARGE_PARENT_NOT_ADD.getErrMsg());
                    }
                }
            }
        } else {
            //parentId为空时，将节点更新为根节点
            newChargeItem.setParentId(0L);
            newChargeItem.setPath(JSON.toJSONString(Collections.singletonList(newChargeItem.getId())));
        }

        //disabled不为空时同时更新子费项
        if (newChargeItem.getDisabled() != null) {
            enableOrDisabledCharge(newChargeItem);
        }
        if (command.getTaxRateId()!=null && command.getTaxRateId()!=0){
            TaxRateE taxRateE = taxRateRepository.getById(command.getTaxRateId());
            List<Long> taxRatePath = new ArrayList<>();
            taxRatePath.add(taxRateE.getTaxCategoryId());
            taxRatePath.add(command.getTaxRateId());
            newChargeItem.setTaxRatePath(JSON.toJSONString(taxRatePath));
        }else {
            newChargeItem.setTaxRatePath("[]");
        }
        //判断是否存在分成费项，存在就保存或修改
        saveOrUpdateChargeItemE(newChargeItem);
        //判断唯一性校验是否更改,更新子费项唯一性校验字段
        if (!Objects.equals(newChargeItem.getIsUnique(), oldChargeItem.getIsUnique())){
            updateChargeItemEByParent(newChargeItem.getId(),newChargeItem.getIsUnique());
        }
        if (EnvConst.ZHONGJIAO.equals(EnvData.config) && null != command.getBusinessCommands()){
            saveOrUpdateChargeItemBusiness(newChargeItem, command.getBusinessCommands());
        }
        // 拈花湾环境设置业务板块
        if (EnvConst.NIANHUAWAN.equals(EnvData.config) && StringUtils.isNotBlank(command.getBusinessSegmentCode())){
            saveOrUpdateBusinessSegment(newChargeItem, command.getBusinessSegmentCode(), command.getBusinessSegmentName());
        }
        return chargeItemRepository.updateById(newChargeItem);
    }

    private void updateChargeItemEByParent(Long id,Integer isUnique) {
        QueryWrapper<ChargeItemE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id).eq("deleted", 0);
        List<ChargeItemE> chargeItemES = chargeItemRepository.getBaseMapper().selectList(queryWrapper);
        if (CollectionUtil.isEmpty(chargeItemES)) {
            return;
        }
        for (ChargeItemE chargeItemE : chargeItemES) {
            chargeItemE.setIsUnique(isUnique);
            chargeItemRepository.updateById(chargeItemE);
            updateChargeItemEByParent(chargeItemE.getId(),chargeItemE.getIsUnique());
        }

    }

    private void saveOrUpdateChargeItemE(ChargeItemE newChargeItem) {
        chargeItemRepository.updateChargeItemShareParentId(newChargeItem.getId());
        if(StringUtils.isNotBlank(newChargeItem.getShareChargeId())){
            String[] ShareCharges = newChargeItem.getShareChargeId().split(",");
            if(ShareCharges.length > 2){
                throw BizException.throw400(ErrorMessage.CHARGE_CODE_MAX_TWO.getErrMsg());
            }
            for(String s : ShareCharges){
                ChargeItemE chargeItemE =new ChargeItemE();
                chargeItemE.setId(Long.parseLong(s));
                chargeItemE.setOperator(newChargeItem.getOperator());
                chargeItemE.setOperatorName(newChargeItem.getOperatorName());
                chargeItemE.setGmtModify(newChargeItem.getGmtModify());
                chargeItemE.setTenantId(newChargeItem.getTenantId());
                chargeItemE.setShareParentId(newChargeItem.getId());
                chargeItemE.setIsUnique(newChargeItem.getIsUnique());
                chargeItemRepository.updateById(chargeItemE);
            }
        }
    }

    /**
     * 隐藏/显示费项
     *
     * @param command command
     * @return Boolean
     */
    public Boolean showOrHide(ShowedChargeItemCommand command) {
        LambdaUpdateWrapper<ChargeItemE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ChargeItemE::getOperator, command.getOperator());
        updateWrapper.set(ChargeItemE::getOperatorName, command.getOperatorName());
        updateWrapper.set(ChargeItemE::getGmtModify, command.getGmtModify());
        List<Long> queryIdList = command.getIdList();
        List<ChargeItemE> childChargeItemList = chargeItemRepository.getChargeItemByPath(queryIdList, command.getTenantId());
        List<Long> updateIdList = childChargeItemList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        if (command.getShowed() == DataShowedEnum.隐藏.getCode()) {
            updateWrapper.set(ChargeItemE::getShowed, DataShowedEnum.隐藏.getCode());
        } else if (command.getShowed() == DataShowedEnum.显示.getCode()) {
            updateWrapper.set(ChargeItemE::getShowed, DataShowedEnum.显示.getCode());
            List<ChargeItemE> collect = childChargeItemList.stream().filter(s -> command.getIdList().contains(s.getId())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(collect)){
                for (ChargeItemE chargeItemE : collect) {
                    List<Long> pathList = JSON.parseArray(chargeItemE.getPath(), Long.class);
                    updateIdList.addAll(pathList);
                }
            }
        }
        updateWrapper.in(ChargeItemE::getId, updateIdList);
        return chargeItemRepository.update(updateWrapper);
    }

    /**
     * 删除费项
     *
     * @param command command
     * @return Boolean
     */
    public Boolean delete(DeletedChargeItemCommand command) {
        Long currentId = command.getId();
        List<Long> queryIdList = Collections.singletonList(currentId);
        List<ChargeItemE> childChargeItemList = chargeItemRepository.getChargeItemByPath(queryIdList, command.getTenantId());
        List<Long> deleteIdList = childChargeItemList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        for (ChargeItemE chargeItemE : childChargeItemList) {
            if(Optional.ofNullable(chargeItemE.getShareChargeId()).isPresent()){
                chargeItemRepository.updateChargeItemShareParentId(chargeItemE.getId());
            }
        }
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            chargeItemBusinessRepository.removeByIds(deleteIdList);
        }
        if (EnvConst.NIANHUAWAN.equals(EnvData.config)){
            businessSegmentRepository.removeByIds(deleteIdList);
        }
        return chargeItemRepository.removeByIds(deleteIdList);
    }

    /**
     * 根据ID查费项数据
     *
     * @param id query
     * @return ChargeItemE
     */
    public ChargeItemE getById(Long id) {
        return chargeItemRepository.getById(id);
    }

    public ChargeItemV getChargeItemV(Long id) {
        ChargeItemE chargeItemE = getById(id);
        ErrorAssertUtil.notNullThrow300(chargeItemE, ErrorMessage.CHARGE_NOT_EXIST);
        ChargeItemV chargeItemV = Global.mapperFacade.map(chargeItemE, ChargeItemV.class);
        if (StringUtils.isNotBlank(chargeItemE.getTaxRateInfo())) {
            List<TaxRateInfoF> taxRateInfos = JSON.parseArray(chargeItemE.getTaxRateInfo(), TaxRateInfoF.class);
            chargeItemV.setTaxRateInfos(taxRateInfos);
        }
        //组装费项路径
        List<Long> chargePath = JSON.parseArray(chargeItemE.getPath(), Long.class);
        chargePath.remove(chargePath.size() - 1);
        chargeItemV.setChargePath(chargePath);
        chargeItemV.setRatePathList(JSON.parseArray(chargeItemE.getTaxRatePath(), Long.class));
        if (StringUtils.isNotBlank(chargeItemV.getShareChargeId())) {
            String[] split = chargeItemV.getShareChargeId().split(",");
            List<ShareChargeV> shareChargeVList = new ArrayList<>();
            for (String ShareChargeId : split) {
                ChargeItemE ShareChargeInfo = getById(Long.parseLong(ShareChargeId));
                ShareChargeV shareChargeV = new ShareChargeV();
                shareChargeV.setShareChargeId(ShareChargeId);
                shareChargeV.setShareChargeName(ShareChargeInfo.getName());
                shareChargeVList.add(shareChargeV);
            }
            chargeItemV.setShareChargeVList(shareChargeVList);
        }
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            List<ChargeItemBusinessE> chargeItemBusinessE = chargeItemBusinessRepository.getChargeItemBusinessE(chargeItemV.getId());
            List<ChargeItemBusinessCommand> chargeItemBusinessCommands = new ArrayList<>();
            if (!org.springframework.util.CollectionUtils.isEmpty(chargeItemBusinessE)){
                for (ChargeItemBusinessE itemBusinessE : chargeItemBusinessE) {
                    ChargeItemBusinessCommand chargeItemBusinessCommand = getChargeItemBusinessCommand(itemBusinessE);
                    chargeItemBusinessCommands.add(chargeItemBusinessCommand);
                }

            }
            chargeItemV.setBusinessCommands(chargeItemBusinessCommands);
        }
        // 拈花湾添加业务板块信息
        if (EnvConst.NIANHUAWAN.equals(EnvData.config)){
            BusinessSegmentE businessSegmentE = businessSegmentRepository.getById(id);
            if (Objects.nonNull(businessSegmentE)) {
                chargeItemV.setBusinessSegmentCode(businessSegmentE.getBusinessSegmentCode());
                chargeItemV.setBusinessSegmentName(businessSegmentE.getBusinessSegmentName());
            }
        }
        return chargeItemV;
    }

    @NotNull
    public ChargeItemBusinessCommand getChargeItemBusinessCommand(ChargeItemBusinessE itemBusinessE) {
        ChargeItemBusinessCommand chargeItemBusinessCommand = new ChargeItemBusinessCommand();

        chargeItemBusinessCommand.setBusinessTypeId(List.of(new Gson().fromJson(itemBusinessE.getBusinessTypeId(), String[].class)));
        chargeItemBusinessCommand.setBusinessTypeCode(List.of(new Gson().fromJson(itemBusinessE.getBusinessTypeCode(), String[].class)));
        chargeItemBusinessCommand.setBusinessTypeName(List.of(new Gson().fromJson(itemBusinessE.getBusinessTypeName(), String[].class)));
        chargeItemBusinessCommand.setDocumentId(itemBusinessE.getDocumentId());
        chargeItemBusinessCommand.setDocumentCode(itemBusinessE.getDocumentCode());
        chargeItemBusinessCommand.setDocumentName(itemBusinessE.getDocumentName());
        chargeItemBusinessCommand.setChangeName(itemBusinessE.getChangeName());
        chargeItemBusinessCommand.setChangeCode(itemBusinessE.getChangeCode());
        chargeItemBusinessCommand.setPaymentId(itemBusinessE.getPaymentId());
        chargeItemBusinessCommand.setPaymentName(itemBusinessE.getPaymentName());
        chargeItemBusinessCommand.setPaymentCode(itemBusinessE.getPaymentCode());
        chargeItemBusinessCommand.setSignedPaymentId(itemBusinessE.getSignedPaymentId());
        chargeItemBusinessCommand.setSignedPaymentName(itemBusinessE.getSignedPaymentName());
        chargeItemBusinessCommand.setSignedPaymentCode(itemBusinessE.getSignedPaymentCode());
        chargeItemBusinessCommand.setCostPaymentId(itemBusinessE.getCostPaymentId());
        chargeItemBusinessCommand.setCostPaymentName(itemBusinessE.getCostPaymentName());
        chargeItemBusinessCommand.setCostPaymentCode(itemBusinessE.getCostPaymentCode());
        return chargeItemBusinessCommand;
    }

    /**
     * 根据id查询子费项列表
     *
     * @param parentId parentId
     * @return List
     */
    public List<ChargeItemE> getChildById(Long parentId) {
        return chargeItemRepository.queryChargeItemByParentId(parentId==null ? 0: parentId);
    }

    /**
     * 校验编码是否存在
     *
     * @param currentId currentId
     * @param code      code
     * @param tenantId  tenantId
     */
    private void checkCodeIsExist(Long currentId, String code, String tenantId) {
        ChargeItemE existChargeItemE = chargeItemRepository.getChargeItemByCode(currentId, code, tenantId);
        if (existChargeItemE != null) {
            throw new BizException(HttpStatus.BAD_REQUEST.value(), new FailInfo(66,ErrorMessage.CHARGE_CODE_EXIST.getErrMsg()));
        }
    }


    private void checkOverdueIsExist(){

    }

    /**
     * 分页查询费项信息
     *
     * @param queryChargeItemPageF queryChargeItemPageF
     * @return PageV
     */
    public Page<ChargeItemE> queryByPage(PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        QueryWrapper<ChargeItemE> queryWrapper = queryChargeItemPageF.getConditions().getQueryModel();
//        queryWrapper.eq("ci.parent_id", Const.State._0);
        queryWrapper.eq("deleted", DataDeletedEnum.NORMAL.getCode());
        Page<ChargeItemE> page = new Page<>(queryChargeItemPageF.getPageNum(), queryChargeItemPageF.getPageSize());
        Page<ChargeItemE> chargeItemDoPage = chargeItemRepository.queryChargeItemByPage(page, queryWrapper);
        List<ChargeItemE> recordList = chargeItemDoPage.getRecords();
        //获取子费项
        List<Long> idList = recordList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        List<ChargeItemE> childChargeItemList = new ArrayList<>(recordList);
        if (!CollectionUtils.isEmpty(idList)) {
            QueryWrapper<ChargeItemE> queryModel = queryChargeItemPageF.getConditions().getQueryModel();
            queryModel.notIn("ci.id",idList);
            queryWrapper.eq("ci.deleted", DataDeletedEnum.NORMAL.getCode());
            childChargeItemList.addAll(chargeItemRepository.getChargeItemWithRateByPath(idList,queryModel));
        }
        // 获取所有路径的费项
        List<Long> chargeItemIds = new ArrayList<>();
        for (ChargeItemE item : childChargeItemList) {
            if (StringUtils.isBlank(item.getPath())) {
                continue;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> chargeIds = null;
            try {
                chargeIds = objectMapper.readValue(item.getPath(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (CollectionUtils.isNotEmpty(chargeIds)) {
                chargeItemIds.addAll(chargeIds);
            }
        }
        List<Long> collect = chargeItemIds.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(chargeItemIds)) {
            childChargeItemList=chargeItemRepository.list(new LambdaQueryWrapper<ChargeItemE>()
                    .in(ChargeItemE::getId,collect)
                    .last("ORDER BY CONVERT(code, SIGNED),code"));
        }
        chargeItemDoPage.setRecords(childChargeItemList);
        return chargeItemDoPage;
    }

    /**
     * 分页查询所有费项
     *
     * @param queryChargeItemPageF 分页入参
     * @return PageV
     */
    public Page<ChargeItemE> queryAllByPage(PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        List<Field> fields = queryChargeItemPageF.getConditions().getFields();
        List<Field> lastStageFieldList = fields.stream().filter(s -> "lastStage".equals(s.getName())).collect(Collectors.toList());
        Page<ChargeItemE> page = Page.of(queryChargeItemPageF.getPageNum(), queryChargeItemPageF.getPageSize());
        if(CollectionUtils.isEmpty(lastStageFieldList)){
            //分页查询所有费项
            QueryWrapper<ChargeItemE> queryModel = queryChargeItemPageF.getConditions().getQueryModel();
            return chargeItemRepository.page(page, queryModel);
        }else{
            //分页查询最后一级费项
            fields.removeAll(lastStageFieldList);
            QueryWrapper<ChargeItemE> queryModel = queryChargeItemPageF.getConditions().getQueryModel();
            queryModel.eq("ci.deleted", DataDeletedEnum.NORMAL.getCode());
            queryModel.eq("ci.disabled", DataDisabledEnum.启用.getCode());
            return chargeItemRepository.queryLastStageByPage(page,queryModel);
        }
    }

    /**
     * 分页查询未关联税目的费项
     *
     * @param queryF 查询入参
     * @return PageV
     */
    public Page<ChargeItemE> queryTaxItemNotRelationByPage(QueryNotRelationChargeItemF queryF) {
        PageF<SearchF<?>> query = queryF.getQuery();
        Integer lastStage = queryF.getLastStage();
        Long externalId = queryF.getExternalId();
        QueryWrapper<?> queryModel = query.getConditions().getQueryModel();
        Page<ChargeItemE> page = Page.of(query.getPageNum(), query.getPageSize());
        queryModel.eq("ci.deleted", DataDeletedEnum.NORMAL.getCode());
        queryModel.eq("ci.disabled", DataDisabledEnum.启用.getCode());
        if(Objects.nonNull(externalId)){
            queryModel.and(wrapper -> wrapper.isNull("tcir.id").or().eq("tcir.tax_item_goods_id",externalId));
        }else{
            queryModel.isNull("tcir.id");
        }
        return chargeItemRepository.queryTaxItemNotRelationByPage(page,queryModel,lastStage);
    }

    /**
     * 根据id集合获取费项
     *
     * @param idList idList
     * @return List
     */
    public List<ChargeItemE> getNameByIdList(List<Long> idList) {
        //获取目标费项
        List<ChargeItemE> chargeItemList = chargeItemRepository.listByIds(idList);
        //根据获取的目标费项获取所有父费项
        HashSet<Long> pathIdSet = new HashSet<>();
        for (ChargeItemE chargeItemE : chargeItemList) {
            List<Long> path = JSON.parseArray(chargeItemE.getPath(), Long.class);
            pathIdSet.addAll(path);
        }
        List<ChargeItemE> parentChargeItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(pathIdSet)) {
            parentChargeItemList = chargeItemRepository.listByIds(pathIdSet);
        }

        //重新拼接费项名称
        for (ChargeItemE chargeItemE : chargeItemList) {
            List<Long> path = JSON.parseArray(chargeItemE.getPath(), Long.class);
            StringBuilder newName = new StringBuilder();
            for (Long id : path) {
                List<ChargeItemE> collect = parentChargeItemList.stream().filter(s -> s.getId().equals(id)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)){
                    newName.append(newName.length() == 0 ? collect.get(0).getName() : "-" + collect.get(0).getName());
                }
            }
            chargeItemE.setName(newName.toString());
        }
        return chargeItemList;
    }

    /**
     * 根据费项名称获取费项
     *
     * @param nameList nameList
     * @param tenantId tenantId
     * @return List
     */
    public List<ChargeItemE> getIsLeafByIdNameList(List<String> nameList, String tenantId) {
        return chargeItemRepository.getChargeItemByNameList(nameList, tenantId);
    }

    /**
     * 根据parentId集合获取费项
     *
     * @param parentIdList parentIdList
     * @param tenantId     tenantId
     * @return List
     */
    public List<ChargeItemE> getChargeItemByParentIdList(List<Long> parentIdList, String tenantId) {
        return chargeItemRepository.getChargeItemByParentIdList(parentIdList, tenantId);
    }

    /**
     * 根据费项id获取费项路径
     *
     * @param id id
     * @return List
     */
    public List<ChargeItemE> getPathById(Long id) {
        ChargeItemE chargeItemE = chargeItemRepository.getById(id);
        ArrayList<ChargeItemE> result = new ArrayList<>();
        if (chargeItemE == null) {
            return result;
        }
        List<Long> path = JSON.parseArray(chargeItemE.getPath(), Long.class);
        List<ChargeItemE> chargeItemList = chargeItemRepository.listByIds(path);
        for (Long currentId : path) {
            result.addAll(chargeItemList.stream().filter(s -> s.getId().equals(currentId)).collect(Collectors.toList()));
        }
        return result;
    }

    /**
     * 导入费项
     *
     * @param file         file
     * @param identityInfo identityInfo
     * @return Boolean
     */
    public Boolean importCharge(MultipartFile file, IdentityInfo identityInfo) {
        try {
            InputStream inputStream = file.getInputStream();
            ArrayList<ChargeItemData> chargeItemDataList = new ArrayList<>();
            //读取excel文件数据
            BaseExcelDataListener<ChargeItemData> baseDataListener = new BaseExcelDataListener<>(chargeItemDataList::addAll);
            EasyExcel.read(inputStream, ChargeItemData.class, baseDataListener).sheet().doRead();
            //校验编码是否存在
            List<String> codeList = chargeItemDataList.stream().map(ChargeItemData::getCode).collect(Collectors.toList());
            List<ChargeItemE> existChargeItemList = chargeItemRepository.queryByCodeList(codeList);
            if (!CollectionUtils.isEmpty(existChargeItemList)) {
                String msg = existChargeItemList.stream().map(ChargeItemE::getCode).collect(Collectors.joining(","));
                throw BizException.throw400(ErrorMessage.CHARGE_CODE_EXIST.getErrMsg() + msg);
            }

            ArrayList<ChargeItemE> chargeItemList = new ArrayList<>();
            for (ChargeItemData object : chargeItemDataList) {
                ChargeItemE chargeItemE = new ChargeItemE();
                chargeItemE.setId(IdentifierFactory.getInstance().generateLongIdentifier("chargeItemId"));
                chargeItemE.setCode(object.getCode());
                chargeItemE.setName(object.getName());
                if(Objects.nonNull(object.getAttribute())){
                    switch (object.getAttribute()) {
                        case "收入":
                            chargeItemE.setAttribute(1);
                            break;
                        case "支出":
                            chargeItemE.setAttribute(2);
                            break;
                        case "代收代付及其他":
                            chargeItemE.setAttribute(3);
                            break;
                        default:
                            break;
                    }
                }
                if(Objects.nonNull(object.getType())){
                    switch (object.getType()) {
                        case "常规收费类型":
                            chargeItemE.setType(1);
                            break;
                        case "临时收费类型":
                            chargeItemE.setType(2);
                            break;
                        case "押金收费类型":
                            chargeItemE.setType(3);
                            break;
                        case "常规付费类型":
                            chargeItemE.setType(4);
                            break;
                        case "押金付费类型":
                            chargeItemE.setType(5);
                            break;
                        default:
                            break;
                    }
                }

                chargeItemE.setDisabled(0);
                chargeItemE.setRemark(object.getRemark());
                chargeItemE.setParentCode(object.getParentCode() == null ? "0" : object.getParentCode());
                chargeItemE.setTenantId(identityInfo.getTenantId());
                chargeItemE.setEstimated(0);
                chargeItemE.setShowed(1);
                LocalDateTime now = LocalDateTime.now();
                chargeItemE.setCreator(identityInfo.getUserId());
                chargeItemE.setCreatorName(identityInfo.getUserName());
                chargeItemE.setGmtCreate(now);
                chargeItemE.setOperator(identityInfo.getUserId());
                chargeItemE.setOperatorName(identityInfo.getUserName());
                chargeItemE.setGmtModify(now);
                chargeItemList.add(chargeItemE);
            }

            //根据parentCode进行分组
            Map<String, List<ChargeItemE>> collect = chargeItemList.stream().collect(Collectors.groupingBy(ChargeItemE::getParentCode));
            List<ChargeItemE> levelOneList = collect.get("0");
            for (ChargeItemE chargeItemE : levelOneList) {
                chargeItemE.setParentId(0L);
                chargeItemE.setPath(JSON.toJSONString(Collections.singletonList(chargeItemE.getId())));
                setSubChargeItem(collect, chargeItemE);
            }
            chargeItemRepository.saveBatch(chargeItemList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 递归设置子费项的path和parentId
     *
     * @param collect           collect
     * @param currentChargeItem 父费项
     */
    public void setSubChargeItem(Map<String, List<ChargeItemE>> collect, ChargeItemE currentChargeItem) {
        List<ChargeItemE> chargeItemList = collect.get(currentChargeItem.getCode());
        if (CollectionUtils.isEmpty(chargeItemList)) {
            return;
        }
        for (ChargeItemE chargeItemE : chargeItemList) {
            chargeItemE.setParentId(currentChargeItem.getId());
            List<Long> path = JSON.parseArray(currentChargeItem.getPath(), Long.class);
            path.add(chargeItemE.getId());
            chargeItemE.setPath(JSON.toJSONString(path));
            setSubChargeItem(collect, chargeItemE);
        }
    }

    /**
     * 获取末级费项(最后一级费项)
     *
     * @param type 费项类型
     * @param attribute 费项属性
     * @return List
     */
    public List<ChargeItemE> getLastStageChargeItem(String type ,String tenantId, String attribute) {
        List<String> typeList = new ArrayList<>();
        List<String> attributeList = new ArrayList<>();
        if(StringUtils.isNotEmpty(type)){
            typeList = Arrays.asList(type.split(","));
        }
        if(StringUtils.isNotEmpty(attribute)){
            attributeList = Arrays.asList(attribute.split(","));
        }
        return chargeItemRepository.queryLastStage(typeList,tenantId,attributeList);
    }

    /**
     * 根据是否末级获取末级费项
     *
     * @return List
     */
    public List<ChargeItemE> getLastStageChargeItemByLastLevel(Integer lastLevel, String tenantId) {
        return chargeItemRepository.queryByLastLevel(lastLevel,tenantId);
    }

    /**
     * 启用或禁用费项
     *
     * @param command command
     * @return Boolean
     */
    public Boolean enableOrDisabled(UpdateChargeItemCommand command) {
        ChargeItemE newChargeItem = Global.mapperFacade.map(command, ChargeItemE.class);
        enableOrDisabledCharge(newChargeItem);
        return true;
    }

    /**
     * 根据费项id生成费项编码
     *
     * @param parentId parentId
     * @return String
     */
    public String generateCode(Long parentId) {
        if(parentId == 0){
            return null;
        }
        ChargeItemE parentChargeItemE = chargeItemRepository.getById(parentId);
        if(Objects.isNull(parentChargeItemE)){
            return null;
        }
        String code = parentChargeItemE.getCode();
        //获取该费项下子费项(包括已删除)
        String maxCode = chargeItemRepository.queryMaxCodeByParentId(parentId);
        String resultCode;
        String prefixCode = "";
        if(StringUtils.isNotEmpty(maxCode)){
            //判断是否存在字符
            char c;
            for (int i = 0; i < maxCode.length(); i++) {
                c = maxCode.charAt(i);
                if(!Character.isDigit(c)){
                    return null;
                }
                if (c == '0'){
                    prefixCode += 0;
                }
            }
            BigDecimal maxCodeDecimal = new BigDecimal(maxCode);
            //当前费项下存在子级，则返回最大编码加一
            BigDecimal one = new BigDecimal(1);
            resultCode = maxCodeDecimal.add(one).toString();
            if (maxCode.startsWith("0")){
                resultCode = "0".repeat(maxCode.length()-resultCode.length()) + resultCode;
            }
        }else{
            //当前父费项下无子级从1开始
            resultCode = code + "0".repeat(2) + 1;
        }
        if(resultCode.length() > 20){
            throw BizException.throw400(ErrorMessage.CHARGE_CODE_MAX_LENGTH.getErrMsg());
        }
        return resultCode;
    }


    /**
     * 根据ID集合查费项数据
     *
     * @param idList 费项id集合
     * @return List
     */
    public List<ChargeItemE> getByIdList(List<Long> idList) {
        return chargeItemRepository.listByIds(idList);
    }

    /**
     * 校验名称是否存在
     *
     * @param currentId currentId
     * @param name      name
     * @param tenantId  tenantId
     */
    private void checkNameIsExist(Long currentId, String name, String tenantId) {
        ChargeItemE existChargeItemE = chargeItemRepository.getChargeItemByName(currentId, name, tenantId);
        if (existChargeItemE != null) {
            throw BizException.throw400(ErrorMessage.CHARGE_NAME_EXIST.getErrMsg());
        }
    }

    /**
     * 检验同级费项名称是否重复
     *
     * @param name     名称
     * @param parentId 父费项id
     */
    private void checkNameIsExist(Long currentId,String name, Long parentId) {
        int level;
        parentId = parentId == null ? 0 : parentId;
        //获取费项层级
        if(parentId == 0){
            level = 1;
        }else{
            ChargeItemE parentChargeItem = chargeItemRepository.getById(parentId);
            List<Long> pathList = JSON.parseArray(parentChargeItem.getPath(), Long.class);
            level = pathList.size() + 1;
        }
        //根据层级和名称获取费项
        ChargeItemE existNameChargeItemE = chargeItemRepository.queryChargeByLevelAndName(currentId,level, name);
        ErrorAssertUtil.isNullThrow300(existNameChargeItemE, ErrorMessage.CHARGE_NAME_EXIST);
    }

    /**
     * 启用或禁用费项
     *
     * @param chargeItemE chargeItemE
     */
    private void enableOrDisabledCharge(ChargeItemE chargeItemE) {
        Long currentId = chargeItemE.getId();
        String tenantId = chargeItemE.getTenantId();
        Integer disabled = chargeItemE.getDisabled();
        List<Long> queryIdList = Collections.singletonList(currentId);

        List<ChargeItemE> childChargeItemList = chargeItemRepository.getChargeItemByPath(queryIdList, tenantId);
        if (!CollectionUtils.isEmpty(childChargeItemList)) {
            List<Long> updateIdList = childChargeItemList.stream()
                    .map(ChargeItemE::getId)
                    .collect(Collectors.toList());
            LambdaUpdateWrapper<ChargeItemE> updateWrapper = new LambdaUpdateWrapper<>();
            if (disabled == DataDisabledEnum.禁用.getCode()) {
                updateWrapper.set(ChargeItemE::getDisabled, DataDisabledEnum.禁用.getCode());
            } else if (disabled == DataDisabledEnum.启用.getCode()) {
                updateWrapper.set(ChargeItemE::getDisabled, DataDisabledEnum.启用.getCode());
                //启用同时更新父节点的状态未启用
                List<ChargeItemE> collect = childChargeItemList.stream().filter(s -> s.getId().equals(currentId)).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(collect)){
                    List<Long> pathList = JSON.parseArray(collect.get(0).getPath(), Long.class);
                    updateIdList.addAll(pathList);
                }
            }
            if (!CollectionUtils.isEmpty(updateIdList)) {
                updateWrapper.in(ChargeItemE::getId, updateIdList);
                updateWrapper.set(ChargeItemE::getOperator, chargeItemE.getOperator());
                updateWrapper.set(ChargeItemE::getOperatorName, chargeItemE.getOperatorName());
                updateWrapper.set(ChargeItemE::getGmtModify, chargeItemE.getGmtModify());
                chargeItemRepository.update(updateWrapper);
            }
        }
    }

    /**
     * 根据编码集合查费项数据
     *
     * @param codeList 编码集合
     * @return List
     */
    public List<ChargeItemE> getByCodeList(List<String> codeList) {
        return chargeItemRepository.queryByCodeList(codeList);
    }

    /**
     * 根据code获取费项信息
     *
     * @param code 费项Code
     * @return ChargeItemE
     */
    public ChargeItemE getByCode(String code) {
        if(StringUtils.isBlank(code)){
            return null;
        }
        //根据层级和名称获取费项
        return chargeItemRepository.getChargeItemByCode(code);
    }

    /**
     * 根据费项父code获取费项信息
     *
     * @return ChargeItemE
     */
    public List<ChargeNameV> getShareChargeItemInfoList() {
        // 获取名称带有业主共有和物业分成的末级费项
        return chargeItemRepository.getChargeItemInfoList(Arrays.asList("业主共有", "物业分成"));
    }

    /**
     * 获取违约金费项
     * @return
     */
    public ChargeItemE getOverdueChargeItem() {
        return chargeItemRepository.getOne(new LambdaUpdateWrapper<ChargeItemE>()
                .eq(ChargeItemE::getIsOverdue, 1)
                .eq(ChargeItemE::getDeleted, 0)
                .eq(ChargeItemE::getDisabled, 0));
    }
}
