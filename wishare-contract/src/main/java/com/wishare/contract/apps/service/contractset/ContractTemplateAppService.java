package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.vo.contractset.ContractTemplateTreeV;
import com.wishare.contract.apps.vo.contractset.ContractTemplateV;
import com.wishare.contract.domains.entity.contractset.ContractTemplateE;
import com.wishare.contract.domains.service.contractset.ContractTemplateAggregate;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 合同范本应用服务
 *
 * @author ljx
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractTemplateAppService implements ApiBase {

    private final ContractTemplateAggregate contractTemplateAggregate;

    /**
     * 创建范本
     */
    public Map<String, List<String>> createContractTemplate(List<CreateContractTemplateF> createContractTemplateFList) {
        return contractTemplateAggregate.createContractTemplate(createContractTemplateFList, curIdentityInfo());
    }

    /**
     * 分页查询合同范本
     */
    public PageV<ContractTemplateTreeV> queryByPage(PageF<SearchF<PageContractTemplateF>> form) {
        return contractTemplateAggregate.queryByPage(form, curIdentityInfo().getTenantId());
    }

    /**
     * id查询合同范本
     */
    public ContractTemplateV queryById(Long id) {
        return contractTemplateAggregate.queryById(id);
    }

    /**
     * 变更范本
     */
    public Long changeContractTemplate(ChangeContractTemplateF changeContractTemplateF) {
        return contractTemplateAggregate.changeContractTemplate(changeContractTemplateF, curIdentityInfo());
    }

    /**
     * 删除范本
     */
    public Boolean deleteById(Long id) {
        return contractTemplateAggregate.deleteById(id, curIdentityInfo());
    }

    /**
     * 编辑范本
     */
    public Boolean editorContractTemplate(EditorContractTemplateF contractTemplateF) {
        return contractTemplateAggregate.editorContractTemplate(contractTemplateF, curIdentityInfo());
    }

    /**
     * 启用/禁用范本
     */
    public Boolean updateContractTemplateStatus(UpdateContractTemplateF updateContractTemplateF) {
        return contractTemplateAggregate.updateContractTemplateStatus(updateContractTemplateF, curIdentityInfo());
    }

    /**
     * 查询范本集
     */
    public List<ContractTemplateV> query(ContractTemplateF contractTemplateF) {
        return contractTemplateAggregate.query(contractTemplateF, curIdentityInfo());
    }
}
