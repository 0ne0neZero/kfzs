package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.fo.contractset.CreateContractCategoryF;
import com.wishare.contract.apps.fo.contractset.UpdateContractCategoryF;
import com.wishare.contract.apps.vo.contractset.ContractCategoryDetailV;
import com.wishare.contract.apps.vo.contractset.ContractCategoryTreeV;
import com.wishare.contract.apps.vo.contractset.ContractCategoryV;
import com.wishare.contract.domains.entity.contractset.ContractCategoryE;
import com.wishare.contract.domains.service.contractset.ContractCategoryDomainsService;
import com.wishare.starter.Global;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.starter.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 合同分类应用服务
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractCategoryAppService implements ApiBase {

    private final ContractCategoryDomainsService contractCategoryDomainsService;

    /**
     * 新增合同分类
     *
     * @param createContractCategoryF createContractCategoryF
     * @return Long
     */
    public Long create(CreateContractCategoryF createContractCategoryF) {
        return contractCategoryDomainsService.create(createContractCategoryF, curIdentityInfo());
    }

    /**
     * 更新合同分类
     *
     * @param updateContractCategoryF updateContractCategoryF
     * @return Boolean
     */
    public Boolean update(UpdateContractCategoryF updateContractCategoryF) {
        return contractCategoryDomainsService.update(updateContractCategoryF, curIdentityInfo());
    }

    /**
     * 删除合同分类
     *
     * @param id id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        return contractCategoryDomainsService.delete(id);
    }

    /**
     * 获取合同分类树
     *
     * @return List
     */
    public List<ContractCategoryTreeV> queryTree(String keyword) {
        List<ContractCategoryE> contractCategoryList = contractCategoryDomainsService.queryAll(curIdentityInfo().getTenantId(), keyword);
        ArrayList<ContractCategoryTreeV> resultList = new ArrayList<>();
        for (ContractCategoryE contractCategoryE : contractCategoryList) {
            String path = contractCategoryE.getPath();
            String[] split = path.substring(1, path.length() - 1).split(",");
            List<Long> parentIdList = new ArrayList<>();
            for (String s : split) {
                parentIdList.add(Long.valueOf(s.replaceAll(" ","")));
            }
            List<ContractCategoryE> contractCategoryEList = contractCategoryDomainsService.queryByIdList(parentIdList);
            for (ContractCategoryE contractCategoryEP : contractCategoryEList) {
                ContractCategoryTreeV contractCategoryTreeVP = new ContractCategoryTreeV();
                contractCategoryTreeVP.setId(contractCategoryEP.getId());
                contractCategoryTreeVP.setName(contractCategoryEP.getName());
                contractCategoryTreeVP.setPid(contractCategoryEP.getParentId());
                contractCategoryTreeVP.setPath(contractCategoryEP.getPath());
                contractCategoryTreeVP.setDisabled(contractCategoryEP.getDisabled());
                contractCategoryTreeVP.setNatureTypeId(contractCategoryEP.getNatureTypeId());
                contractCategoryTreeVP.setNatureTypeCode(contractCategoryEP.getNatureTypeCode());
                contractCategoryTreeVP.setNatureTypeName(contractCategoryEP.getNatureTypeName());
                contractCategoryTreeVP.setBizCode(contractCategoryEP.getBizCode());
                contractCategoryTreeVP.setIsBuy(contractCategoryEP.getIsBuy());
                resultList.add(contractCategoryTreeVP);
            }
            ContractCategoryTreeV contractCategoryTreeV = new ContractCategoryTreeV();
            contractCategoryTreeV.setId(contractCategoryE.getId());
            contractCategoryTreeV.setName(contractCategoryE.getName());
            contractCategoryTreeV.setPid(contractCategoryE.getParentId());
            contractCategoryTreeV.setPath(contractCategoryE.getPath());
            contractCategoryTreeV.setDisabled(contractCategoryE.getDisabled());
            contractCategoryTreeV.setNatureTypeId(contractCategoryE.getNatureTypeId());
            contractCategoryTreeV.setNatureTypeCode(contractCategoryE.getNatureTypeCode());
            contractCategoryTreeV.setNatureTypeName(contractCategoryE.getNatureTypeName());
            contractCategoryTreeV.setIsBuy(contractCategoryE.getIsBuy());
            contractCategoryTreeV.setBizCode(contractCategoryE.getBizCode());
            resultList.add(contractCategoryTreeV);
        }
        ArrayList<ContractCategoryTreeV> collect = resultList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(ContractCategoryTreeV::getId))), ArrayList::new));
        return TreeUtil.treeing(collect);
    }

    /**
     * 根据parentId获取下级分类
     *
     * @param parentId parentId
     * @return List
     */
    public List<ContractCategoryV> queryByParentId(Long parentId) {
        parentId = parentId == null ? 0 : parentId;
        List<ContractCategoryE> contractCategoryList = contractCategoryDomainsService.querySubById(parentId,curIdentityInfo().getTenantId());
        return Global.mapperFacade.mapAsList(contractCategoryList, ContractCategoryV.class);
    }

    /**
     * 根据id获取合同分类详细信息
     *
     * @param id 合同分类id
     * @return ContractCategoryDetailV
     */
    public ContractCategoryDetailV queryById(Long id) {
        ContractCategoryE contractCategoryE = contractCategoryDomainsService.queryById(id);
        if(Objects.nonNull(contractCategoryE)){
            ContractCategoryDetailV contractCategoryDetailV = Global.mapperFacade.map(contractCategoryE, ContractCategoryDetailV.class);
            contractCategoryDetailV.setPathList(JSON.parseArray(contractCategoryE.getPath(), Long.class));
            return contractCategoryDetailV;
        }else{
            return null;
        }
    }
}
