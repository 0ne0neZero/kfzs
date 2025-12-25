package com.wishare.contract.domains.service.contractset;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.fo.contractset.CreateContractCategoryF;
import com.wishare.contract.apps.fo.contractset.UpdateContractCategoryF;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.consts.ErrMsgEnum;
import com.wishare.contract.domains.entity.contractset.ContractCategoryE;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import com.wishare.contract.domains.mapper.contractset.ContractCategoryRepository;
import com.wishare.contract.domains.mapper.contractset.ContractTemplateRepository;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 合同分类领域业务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractCategoryDomainsService {

    private final ContractCategoryRepository contractCategoryRepository;

    private final ContractTemplateRepository contractTemplateRepository;

    /**
     * 创建合同分类
     *
     * @param createContractCategoryF 入参
     * @param identityInfo            identityInfo
     * @return Long
     */
    public Long create(CreateContractCategoryF createContractCategoryF, IdentityInfo identityInfo) {
        //检验同级下是否存在相同名称
        String name = createContractCategoryF.getName();
        Long parentId = createContractCategoryF.getParentId() == null ? 0 : createContractCategoryF.getParentId();
        checkNameIsExist(identityInfo.getTenantId(), name, parentId, null);

        ContractCategoryE contractCategoryE = Global.mapperFacade.map(createContractCategoryF, ContractCategoryE.class);
        long id = UidHelper.nextId("contractCategoryId");
        //设置path
        if (parentId == 0) {
            ArrayList<Long> pathList = new ArrayList<>();
            pathList.add(id);
            contractCategoryE.setPath(JSON.toJSONString(pathList));
        } else {
            //获取父分类的path
            ContractCategoryE parentContractCategory = contractCategoryRepository.getById(parentId);
            List<Long> pathList = JSON.parseArray(parentContractCategory.getPath(), Long.class);

            //检验层级（最大为5级）
            if (pathList.size() >= ContractSetConst.CONTRACT_CATEGORY_MAX_LEVEL) {
                throw BizException.throw400(ErrMsgEnum.CONTRACT_CATEGORY_EXCEED_MAX_LEVEL.getErrMsg());
            }

            pathList.add(id);
            contractCategoryE.setPath(JSON.toJSONString(pathList));
        }

        LocalDateTime now = LocalDateTime.now();
        contractCategoryE.setId(id);
        contractCategoryE.setCreator(identityInfo.getUserId());
        contractCategoryE.setTenantId(identityInfo.getTenantId());
        contractCategoryE.setCreatorName(identityInfo.getUserName());
        contractCategoryE.setGmtCreate(now);
        contractCategoryE.setOperator(identityInfo.getUserId());
        contractCategoryE.setOperatorName(identityInfo.getUserName());
        contractCategoryE.setGmtModify(now);
        contractCategoryRepository.save(contractCategoryE);
        return id;
    }

    /**
     * 校验同级下是否存在相同名称
     *
     * @param tenantId tenantId
     * @param name     name
     * @param parentId parentId
     */
    private void checkNameIsExist(String tenantId, String name, Long parentId, Long currentId) {
        List<ContractCategoryE> contractCategoryList = contractCategoryRepository.queryByParentId(parentId, tenantId, currentId);
        List<String> collect = contractCategoryList.stream().map(ContractCategoryE::getName).filter(s -> s.equals(name)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            throw BizException.throw400(ErrMsgEnum.CONTRACT_CATEGORY_NAME_EXIST.getErrMsg());
        }
    }

    /**
     * 更新合同分类
     *
     * @param updateContractCategoryF updateContractCategoryF
     * @param identityInfo            identityInfo
     * @return Boolean
     */
    public Boolean update(UpdateContractCategoryF updateContractCategoryF, IdentityInfo identityInfo) {
        //校验分类是否存在
        Long currentId = updateContractCategoryF.getId();
        ContractCategoryE contractCategory = contractCategoryRepository.getById(currentId);
        if (contractCategory == null) {
            throw BizException.throw400(ErrMsgEnum.CONTRACT_CATEGORY_NOT_EXIST.getErrMsg());
        }

        //检验同级下是否存在相同名称
        String name = updateContractCategoryF.getName();
        Long parentId = contractCategory.getParentId();
        checkNameIsExist(identityInfo.getTenantId(), name, parentId, currentId);

        //更新禁用状态，同时更新下级分类
        if (!updateContractCategoryF.getDisabled().equals(contractCategory.getDisabled())) {
            List<ContractCategoryE> contractCategoryList = contractCategoryRepository.querySubLevel(currentId);
            List<Long> idList = contractCategoryList.stream().map(ContractCategoryE::getId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(idList)) {
                contractCategoryRepository.updateDisabledByIdList(idList, updateContractCategoryF.getDisabled());
            }
            // 若启用需要启用所有父级
            if (updateContractCategoryF.getDisabled() == 0) {
                String path = contractCategory.getPath();
                String[] split = path.substring(1, path.length() - 1).split(",");
                List<Long> parentIdList = new ArrayList<>();
                for (String s : split) {
                    parentIdList.add(Long.valueOf(s.replaceAll(" ","")));
                }
                contractCategoryRepository.updateDisabledByIdList(parentIdList, updateContractCategoryF.getDisabled());
            }
        }

        ContractCategoryE newContractCategory = Global.mapperFacade.map(updateContractCategoryF, ContractCategoryE.class);
        LocalDateTime now = LocalDateTime.now();
        newContractCategory.setOperator(identityInfo.getUserId());
        newContractCategory.setOperatorName(identityInfo.getUserName());
        newContractCategory.setGmtModify(now);
        // 更新范本中的分类路径名
        if (Strings.isNotBlank(updateContractCategoryF.getCategoryPathName())) {
            contractTemplateRepository.updateCategoryPathNameByCategoryId(updateContractCategoryF.getCategoryPathName(), currentId);
        }
        return contractCategoryRepository.updateById(newContractCategory);
    }

    /**
     * 删除合同分类
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        ContractCategoryE contractCategory = contractCategoryRepository.getById(id);
        if (contractCategory == null) {
            throw BizException.throw400(ErrMsgEnum.CONTRACT_CATEGORY_NOT_EXIST.getErrMsg());
        }
        //获取当前分类和所有下级分类
        List<ContractCategoryE> contractCategoryList = contractCategoryRepository.querySubLevel(id);

        // 校验当前分类和所有下级分类是否被关联合同范本
        contractCategoryList.forEach(item -> {
            List<ContractTemplateE> contractTemplateES = contractTemplateRepository.queryByCategoryId(item.getId());
            if (Objects.nonNull(contractTemplateES) && !contractTemplateES.isEmpty()) {
                throw BizException.throw400(ErrMsgEnum.CONTRACT_CATEGORY_IN_USE.getErrMsg());
            }
        });

        return contractCategoryRepository.removeByIds(contractCategoryList);
    }

    /**
     * 获取当前租户下的合同分类
     *
     * @param tenantId tenantId
     * @return List
     */
    public List<ContractCategoryE> queryAll(String tenantId, String keyword) {
        return contractCategoryRepository.queryByTenantId(tenantId, keyword);
    }

    /**
     * 根据parentId获取下级合同分类
     *
     * @param parentId parentId
     * @param tenantId tenantId
     * @return List
     */
    public List<ContractCategoryE> querySubById(Long parentId, String tenantId) {
        return contractCategoryRepository.queryByParentId(parentId, tenantId, null);
    }

    /**
     * 根据id获取合同分类详细信息
     *
     * @param id 合同分类id
     * @return ContractCategoryDetailV
     */
    public ContractCategoryE queryById(Long id) {
        return contractCategoryRepository.getById(id);
    }

    public List<ContractCategoryE> queryByIdList(List<Long> idList) {
        return contractCategoryRepository.getByIdList(idList);
    }
}
