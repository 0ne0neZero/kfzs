package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.apps.vo.contractset.ContractTemplateV;
import com.wishare.contract.domains.consts.ErrMsgEnum;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 合同范本聚合
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractTemplateAggregate {

    private final ContractTemplateDomainsService contractTemplateDomainsService;

    private final ContractTemplateLogDomainsService contractTemplateLogDomainsService;

    /**
     * 新增合同范本
     */
    public Map<String, List<String>> createContractTemplate(List<CreateContractTemplateF> createContractTemplateFList, IdentityInfo identityInfo) {
        Map<String, List<String>> result = new HashMap<>();
        List<String> success = new ArrayList<>();
        List<String> failure = new ArrayList<>();
        if (Objects.nonNull(createContractTemplateFList) && !createContractTemplateFList.isEmpty()) {
            createContractTemplateFList.forEach(item -> {
                Long templateId = contractTemplateDomainsService.create(item, identityInfo);
                if (templateId != 0) {
                    success.add(item.getName() + "创建成功");
                } else {
                    failure.add(item.getName() + ErrMsgEnum.CONTRACT_TEMPLATE_NAME_EXIST.getErrMsg());
                }
            });
        }
        result.put("success", success);
        result.put("failure", failure);
        return result;
    }

    /**
     * 分页查询合同范本
     */
    public PageV<ContractTemplateTreeV> queryByPage(PageF<SearchF<PageContractTemplateF>> form, String tenantId) {
        return contractTemplateDomainsService.queryByPage(form, tenantId);
    }

    /**
     * id查询范本
     */
    public ContractTemplateV queryById(Long id) {
        ContractTemplateV result = new ContractTemplateV();
        ContractTemplateE contractTemplateE = contractTemplateDomainsService.queryById(id);
        if (Objects.nonNull(contractTemplateE)) {
            result = Global.mapperFacade.map(contractTemplateE, ContractTemplateV.class);
        }
        return result;
    }

    /**
     * 变更范本
     */
    public Long changeContractTemplate(ChangeContractTemplateF changeContractTemplateF, IdentityInfo identityInfo) {
        return contractTemplateDomainsService.changeContractTemplate(changeContractTemplateF, identityInfo);
    }

    /**
     * 删除范本
     */
    public Boolean deleteById(Long id, IdentityInfo identityInfo) {
        return contractTemplateDomainsService.deleteById(id, identityInfo);
    }

    /**
     * 编辑范本
     */
    public Boolean editorContractTemplate(EditorContractTemplateF contractTemplateF, IdentityInfo identityInfo) {
        return contractTemplateDomainsService.editorContractTemplate(contractTemplateF, identityInfo);
    }

    /**
     * 启用/禁用范本
     */
    public Boolean updateContractTemplateStatus(UpdateContractTemplateF updateContractTemplateF, IdentityInfo identityInfo) {
        return contractTemplateDomainsService.updateContractTemplateStatus(updateContractTemplateF, identityInfo);
    }

    /**
     * 查询范本集
     */
    public List<ContractTemplateV> query(ContractTemplateF contractTemplateF, IdentityInfo identityInfo) {
        List<ContractTemplateE> contractTemplateEList = contractTemplateDomainsService.query(contractTemplateF, identityInfo);
        if (!contractTemplateEList.isEmpty()) {
            return Global.mapperFacade.mapAsList(contractTemplateEList, ContractTemplateV.class);
        } else {
            return new ArrayList<>();
        }
    }
}
