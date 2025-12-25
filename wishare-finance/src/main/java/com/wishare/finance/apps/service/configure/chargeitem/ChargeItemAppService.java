package com.wishare.finance.apps.service.configure.chargeitem;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.wishare.finance.apps.model.configure.chargeitem.fo.*;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemTreeV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeItemV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ChargeNameV;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ShareChargeV;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.*;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemBusinessE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemBusinessRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.service.ChargeItemDomainService;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.TreeUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 费项应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChargeItemAppService implements ApiBase {

    private final ChargeItemDomainService chargeItemDomainService;
    private final ChargeItemRepository chargeItemRepository;
    private final ChargeItemBusinessRepository chargeItemBusinessRepository;

    /**
     * 新增费项
     *
     * @param createChargeItemF createChargeItemF
     * @return Long
     */
    public Long create(CreateChargeItemF createChargeItemF) {
        CreateChargeItemCommand command = createChargeItemF.getCreateChargeItemCommand(curIdentityInfo());
        command.setIsUnique(createChargeItemF.getIsUnique() == null ? 0 : createChargeItemF.getIsUnique());
        return chargeItemDomainService.create(command).getId();
    }

    /**
     * 更新费项
     *
     * @param updateChargeItemF updateChargeItemF
     * @return Boolean
     */
    public Boolean update(UpdateChargeItemF updateChargeItemF) {
        UpdateChargeItemCommand command = updateChargeItemF.getUpdateChargeItemCommand(curIdentityInfo());
        command.setIsUnique(updateChargeItemF.getIsUnique() == null ? 0 : updateChargeItemF.getIsUnique());
        return chargeItemDomainService.update(command);
    }

    /**
     * 启用费项
     *
     * @param id id
     * @return Boolean
     */
    public Boolean enable(Long id) {
        UpdateChargeItemF updateChargeItemF = new UpdateChargeItemF();
        updateChargeItemF.setId(id);
        updateChargeItemF.setDisabled(DataDisabledEnum.启用.getCode());
        UpdateChargeItemCommand command = updateChargeItemF.getUpdateChargeItemCommand(curIdentityInfo());
        // 检查该费项是否是违约金费项 若是违约金, 则判断是否有其他启用的违约
        ChargeItemE chargeItemE = chargeItemRepository.getById(id);
        if (chargeItemE.getIsOverdue().equals(1)) {
            ChargeItemE chargeItemIsOverdue = chargeItemRepository.getChargeItemIsOverdue();
            if (null != chargeItemIsOverdue) {
                throw BizException.throw400(ErrorMessage.CHARGE_OVERDUE_EXIST.getErrMsg() + "，费项名称:" + chargeItemIsOverdue.getName());
            }
        }
        return chargeItemDomainService.enableOrDisabled(command);
    }

    /**
     * 禁用费项
     *
     * @param id id
     * @return Boolean
     */
    public Boolean disable(Long id) {
        UpdateChargeItemF updateChargeItemF = new UpdateChargeItemF();
        updateChargeItemF.setId(id);
        updateChargeItemF.setDisabled(DataDisabledEnum.禁用.getCode());
        UpdateChargeItemCommand command = updateChargeItemF.getUpdateChargeItemCommand(curIdentityInfo());
        return chargeItemDomainService.enableOrDisabled(command);
    }

    /**
     * 根据id显示或隐藏费项
     *
     * @param showOrHideChargeItemF showOrHideChargeItemF
     * @return Boolean
     */
    public Boolean showed(ShowOrHideChargeItemF showOrHideChargeItemF) {
        ShowedChargeItemCommand command = showOrHideChargeItemF.getShowedChargeItemCommand(curIdentityInfo());
        return chargeItemDomainService.showOrHide(command);
    }

    /**
     * 删除费项
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        DeletedChargeItemCommand command = new DeletedChargeItemCommand();
        command.setId(id);
        command.setTenantId(curIdentityInfo().getTenantId());
        return chargeItemDomainService.delete(command);
    }

    /**
     * 根据ID查费项数据
     *
     * @param id id
     * @return ChargeItemV
     */
    public ChargeItemV getById(Long id) {
        return chargeItemDomainService.getChargeItemV(id);
    }

    /**
     * 根据id查询子费项列表
     *
     * @param id id
     * @return List
     */
    public List<ChargeItemV> queryChildById(Long id) {
        return Global.mapperFacade.mapAsList(chargeItemDomainService.getChildById(id), ChargeItemV.class);
    }

    /**
     * 分页查询费项
     *
     * @param queryChargeItemPageF queryChargeItemPageF
     * @return PageV
     */
    public PageV<ChargeItemV> queryByPage(PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        Page<ChargeItemE> queryByPage = chargeItemDomainService.queryByPage(queryChargeItemPageF);
        List<ChargeItemE> records = queryByPage.getRecords();
        List<ChargeItemV> chargeItemVoList = Global.mapperFacade.mapAsList(records, ChargeItemV.class);

        // 处理业务明细
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)) {
            chargeItemVoList.forEach( i ->{
                List<ChargeItemBusinessE> chargeItemBusinessE = chargeItemBusinessRepository.getChargeItemBusinessE(i.getId());
                ArrayList<ChargeItemBusinessCommand> chargeItemBusinessCommands = new ArrayList<>();
                if (!CollectionUtils.isEmpty(chargeItemBusinessE)){
                    for (ChargeItemBusinessE itemBusinessE : chargeItemBusinessE) {
                        ChargeItemBusinessCommand chargeItemBusinessCommand = chargeItemDomainService.getChargeItemBusinessCommand(itemBusinessE);
                        chargeItemBusinessCommands.add(chargeItemBusinessCommand);
                    }

                }
                i.setBusinessCommands(chargeItemBusinessCommands);
            });
        }
        //组装树结构
        for (ChargeItemV chargeItemV : chargeItemVoList) {
            if (chargeItemV.getParentId() != null) {
                chargeItemV.setPid(chargeItemV.getParentId());
            }
        }
        List<ChargeItemV> treeing = TreeUtil.treeing(chargeItemVoList);
        return PageV.of(queryChargeItemPageF, queryByPage.getTotal(), treeing);
    }

    /**
     * 分页查询所有费项
     *
     * @param queryChargeItemPageF 分页入参
     * @return PageV
     */
    public PageV<ChargeItemV> queryAllByPage(PageF<SearchF<ChargeItemE>> queryChargeItemPageF) {
        Page<ChargeItemE> queryByPage = chargeItemDomainService.queryAllByPage(queryChargeItemPageF);
        return PageV.of(queryChargeItemPageF, queryByPage.getTotal(), Global.mapperFacade.mapAsList(queryByPage.getRecords(), ChargeItemV.class));
    }


    /**
     * 分页查询未关联外部数据的费项
     *
     * @param queryF 查询入参
     * @return PageV
     */
    public PageV<ChargeItemV> queryNotRelationByPage(QueryNotRelationChargeItemF queryF) {
        if (queryF.getExternalType() == 1) {
            //分页查询未关联税目的费项
            Page<ChargeItemE> queryByPage = chargeItemDomainService.queryTaxItemNotRelationByPage(queryF);
            return PageV.of(queryF.getQuery(), queryByPage.getTotal(), Global.mapperFacade.mapAsList(queryByPage.getRecords(), ChargeItemV.class));
        } else {
            return PageV.of(queryF.getQuery(), 0, new ArrayList<>());
        }
    }

    /**
     * 根据ID查费项全路径名称
     *
     * @param chargeId chargeId
     * @return String
     */
    public String getNameById(Long chargeId) {
        return chargeItemDomainService.getNameById(chargeId);
    }

    /**
     * 根据ID查费项全路径名称
     *
     * @param itemIds
     * @return List<ChargeItemE>
     */
    public List<ChargeItemE> getNameByIds(List<Long> itemIds) {
        return chargeItemDomainService.getNameByIds(itemIds);
    }


    /**
     * 根据ID查目标费项名称
     *
     * @param chargeId chargeId
     * @return String
     */
    public String getSimpleNameById(Long chargeId) {
        return chargeItemDomainService.getSimpleNameById(chargeId);
    }

    /**
     * 获取费项树
     *
     * @return List
     */
    public List<ChargeItemTreeV> queryTree(Long filterId, String attribute, String type, String parentIds, String specialParentIds) {
        List<String> typeList = new ArrayList<>();
        List<String> attributeList = new ArrayList<>();
        List<String> parentIdList = new ArrayList<>();
        List<String> specialParentIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            typeList = Arrays.asList(type.split(","));
        }
        if (StringUtils.isNotEmpty(attribute)) {
            attributeList = Arrays.asList(attribute.split(","));
        }
        if (StringUtils.isNotEmpty(parentIds)) {
            parentIdList = Arrays.asList(parentIds.split(","));
        }
        if (StringUtils.isNotEmpty(specialParentIds)) {
            specialParentIdList = Arrays.asList(specialParentIds.split(","));
        }
        //获取满足条件的所有费项
        List<ChargeItemE> chargeItemTreeList = chargeItemDomainService.queryChargeItemList(filterId, attributeList, typeList, curIdentityInfo().getTenantId(), parentIdList, specialParentIdList);
        ArrayList<ChargeItemTreeV> chargeItemTreeVoList = new ArrayList<>();
        for (ChargeItemE chargeItemE : chargeItemTreeList) {
            ChargeItemTreeV chargeItemTreeV = Global.mapperFacade.map(chargeItemE, ChargeItemTreeV.class);
            if (chargeItemE.getParentId() != null) {
                chargeItemTreeV.setPid(chargeItemE.getParentId());
            }
            chargeItemTreeVoList.add(chargeItemTreeV);
        }
        return TreeUtil.treeing(chargeItemTreeVoList);
    }

    public List<ChargeItemTreeV> queryTreeLevel(Long filterId, String attribute, String type) {
        List<String> typeList = new ArrayList<>();
        List<String> attributeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            typeList = Arrays.asList(type.split(","));
        }
        if (StringUtils.isNotEmpty(attribute)) {
            attributeList = Arrays.asList(attribute.split(","));
        }
        //获取满足条件的所有费项
        List<ChargeItemE> chargeItemTreeList = chargeItemDomainService.queryLevelChargeItemList(attributeList, typeList, curIdentityInfo().getTenantId());
        ArrayList<ChargeItemTreeV> chargeItemTreeVoList = new ArrayList<>();
        for (ChargeItemE chargeItemE : chargeItemTreeList) {
            ChargeItemTreeV chargeItemTreeV = Global.mapperFacade.map(chargeItemE, ChargeItemTreeV.class);
            if (chargeItemE.getParentId() != null) {
                chargeItemTreeV.setPid(chargeItemE.getParentId());
            }
            chargeItemTreeVoList.add(chargeItemTreeV);
        }
        return TreeUtil.treeing(chargeItemTreeVoList);
    }

    /**
     * 根据ID集合查费项名称
     *
     * @param idList idList
     * @return List
     */
    public List<ChargeNameV> getNameByIdList(List<Long> idList) {
        List<ChargeItemE> chargeItemList;
        if (!CollectionUtils.isEmpty(idList)) {
            chargeItemList = chargeItemDomainService.getNameByIdList(idList);
        } else {
            chargeItemList = new ArrayList<>();
        }
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeNameV.class);
    }

    /**
     * 根据名称集合查费项是否末级
     *
     * @param nameList nameList
     * @return List
     */
    public List<ChargeNameV> getIsLeafByIdNameList(List<String> nameList) {
        List<ChargeItemE> chargeItemList;
        //根据费项名称获取数据
        if (!CollectionUtils.isEmpty(nameList)) {
            chargeItemList = chargeItemDomainService.getIsLeafByIdNameList(nameList, curIdentityInfo().getTenantId());
        } else {
            return new ArrayList<>();
        }
        //构建返回值（设置费项状态：0不存在,1末级节点,2非末级节点）
        ArrayList<ChargeNameV> resultList = new ArrayList<>();
        //根据费项id获取下级费项（存在下级为2， 不存在为1）
        List<Long> parentIdList = chargeItemList.stream().map(ChargeItemE::getId).collect(Collectors.toList());
        List<ChargeItemE> chargeItemByParentIdList = chargeItemDomainService.getChargeItemByParentIdList(parentIdList, curIdentityInfo().getTenantId());
        for (String name : nameList) {
            ChargeNameV chargeNameV = new ChargeNameV();
            chargeNameV.setName(name);
            List<ChargeItemE> collect = chargeItemList.stream().filter(s -> s.getName().equals(name)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                chargeNameV.setStatus(0);
            } else {
                ChargeItemE chargeItemE = collect.get(0);
                Long id = chargeItemE.getId();
                chargeNameV.setId(id);
                chargeNameV.setCode(chargeItemE.getCode());
                List<ChargeItemE> parentCollect = chargeItemByParentIdList.stream().filter(s -> s.getParentId().equals(id)).collect(Collectors.toList());
                if (parentCollect.size() == 0) {
                    chargeNameV.setStatus(1);
                } else {
                    chargeNameV.setStatus(2);
                }
            }
            resultList.add(chargeNameV);
        }
        return resultList;
    }

    /**
     * 根据费项id获取费项路径
     *
     * @param id id
     * @return List
     */
    public List<ChargeNameV> getPathById(Long id) {
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getPathById(id);
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeNameV.class);
    }

    /**
     * 导入费项
     *
     * @param file file
     * @return Boolean
     */
    public Boolean importCharge(MultipartFile file) {
        return chargeItemDomainService.importCharge(file, curIdentityInfo());
    }

    /**
     * 获取末级费项(最后一级费项)
     *
     * @param chargeType 费项类型
     * @return List
     */
    public List<ChargeNameV> getLastStageChargeItem(String chargeType, String attribute) {
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getLastStageChargeItem(chargeType, curIdentityInfo().getTenantId(), attribute);
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeNameV.class);
    }


    /**
     * 获取末级费项(全路径)
     *
     * @param chargeType 费项类型
     * @return List
     */
    public List<ChargeNameV> getLastStageChargeItemAllPath(String chargeType, String attribute) {
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getLastStageChargeItem(chargeType, curIdentityInfo().getTenantId(), attribute);
        //-- 补充完整路径名称
        for (ChargeItemE chargeItemE : chargeItemList) {
            String pathName = getNameById(chargeItemE.getId());
            chargeItemE.setName(pathName);
        }
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeNameV.class);
    }


    /**
     * 根据是否末级字段获取末级费项
     *
     * @return List
     */
    public List<ChargeNameV> getLastStageChargeItemByLastLevel() {
        //获取末级费项
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getLastStageChargeItemByLastLevel(1, curIdentityInfo().getTenantId());
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeNameV.class);
    }

    /**
     * 根据费项id生成费项编码
     *
     * @param parentId 父费项id
     * @return String
     */
    public String generateCode(Long parentId) {
        return chargeItemDomainService.generateCode(parentId);
    }

    /**
     * 根据ID集合查费项数据
     *
     * @param idList 费项id集合
     * @return List
     */
    public List<ChargeItemV> getByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new ArrayList<>();
        }
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getByIdList(idList);
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeItemV.class);
    }

    /**
     * 根据编码集合查费项数据
     *
     * @param codeList 编码集合
     * @return List
     */
    public List<ChargeItemV> getByCodeList(List<String> codeList) {
        if (CollectionUtils.isEmpty(codeList)) {
            return new ArrayList<>();
        }
        List<ChargeItemE> chargeItemList = chargeItemDomainService.getByCodeList(codeList);
        return Global.mapperFacade.mapAsList(chargeItemList, ChargeItemV.class);
    }

    /**
     * 根据费项code获取费项信息
     *
     * @param code 费项code
     * @return 费项信息
     */
    public ChargeItemV getByFeeCode(String code) {
        ChargeItemE chargeItemE = chargeItemDomainService.getByCode(code);
        return Global.mapperFacade.map(chargeItemE, ChargeItemV.class);
    }

    /**
     * 获取末级分成费项信息
     *
     * @return 费项信息
     */
    public List<ChargeNameV> getChargeItemInfoList() {
        return chargeItemDomainService.getShareChargeItemInfoList();
    }

    /**
     * 根据费项类型获取费项树
     *
     * @return List
     */
    public List<ChargeItemTreeV> queryTreeByType(Long filterId, String attribute, String type) {
        List<String> typeList = new ArrayList<>();
        List<String> attributeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            typeList = Arrays.asList(type.split(","));
        }
        if (StringUtils.isNotEmpty(attribute)) {
            attributeList = Arrays.asList(attribute.split(","));
        }
        //获取满足条件的所有费项
        List<ChargeItemE> chargeItemTreeList = chargeItemDomainService.queryChargeItemList(filterId, attributeList, typeList, curIdentityInfo().getTenantId(), null, null);
        List<String> pathLists = new ArrayList<>();
        // 根据费项子类找到所有父类
        for (ChargeItemE chargeItemE : chargeItemTreeList) {
            pathLists.addAll(JSON.parseArray(chargeItemE.getPath(), String.class));
        }
        List<ChargeItemE> chargeItemTreeLists = chargeItemDomainService.getByIdList(pathLists.stream().distinct().map(Long::parseLong).collect(Collectors.toList()));
        List<ChargeItemTreeV> chargeItemTreeVoList = new ArrayList<>();
        for (ChargeItemE chargeItemE : chargeItemTreeLists) {
            ChargeItemTreeV chargeItemTreeV = Global.mapperFacade.map(chargeItemE, ChargeItemTreeV.class);
            if (chargeItemE.getParentId() != null) {
                chargeItemTreeV.setPid(chargeItemE.getParentId());
            }
            chargeItemTreeVoList.add(chargeItemTreeV);
        }
        return TreeUtil.treeing(chargeItemTreeVoList);
    }

    public List<ChargeItemTreeV> queryTreeNew(Long filterId, String attribute, String type,Integer incomeOrPay) {
        List<String> typeList = new ArrayList<>();
        List<String> attributeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(type)) {
            typeList = Arrays.asList(type.split(","));
        }
        if (StringUtils.isNotEmpty(attribute)) {
            attributeList = Arrays.asList(attribute.split(","));
        }
        //获取满足条件的所有费项
        List<ChargeItemE> chargeItemTreeList = chargeItemDomainService.queryChargeItemList(filterId, attributeList, typeList, curIdentityInfo().getTenantId(), null, null);
        ArrayList<ChargeItemTreeV> chargeItemTreeVoList = new ArrayList<>();
        for (ChargeItemE chargeItemE : chargeItemTreeList) {
            ChargeItemTreeV chargeItemTreeV = Global.mapperFacade.map(chargeItemE, ChargeItemTreeV.class);
            if (chargeItemE.getParentId() != null) {
                chargeItemTreeV.setPid(chargeItemE.getParentId());
            }
            chargeItemTreeVoList.add(chargeItemTreeV);
        }
        if (!CollectionUtils.isEmpty(chargeItemTreeList)) {
            chargeItemTreeList.removeIf(c->incomeOrPay.equals(c.getAttribute()));
        }
        return TreeUtil.treeing(chargeItemTreeVoList);
    }

}

