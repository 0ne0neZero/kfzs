package com.wishare.contract.apps.service.revision.template;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.template.ContractTemplateConfigE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigPageF;
import com.wishare.contract.domains.service.revision.template.ContractTemplateConfigService;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigV;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigListF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractTemplateConfigUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractTemplateConfigListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同范本字段配置表
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Service
@Slf4j
public class ContractTemplateConfigAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractTemplateConfigService contractTemplateConfigService;

    public ContractTemplateConfigV get(ContractTemplateConfigF contractTemplateConfigF){
        return contractTemplateConfigService.get(contractTemplateConfigF).orElse(null);
    }

    public ContractTemplateConfigListV list(ContractTemplateConfigListF contractTemplateConfigListF){
        return contractTemplateConfigService.list(contractTemplateConfigListF);
    }

    public String save(ContractTemplateConfigSaveF contractTemplateConfigF){
        return contractTemplateConfigService.save(contractTemplateConfigF);
    }

    public void update(ContractTemplateConfigUpdateF contractTemplateConfigF){
        contractTemplateConfigService.update(contractTemplateConfigF);
    }

    public boolean removeById(String id){
        return contractTemplateConfigService.removeById(id);
    }

    public PageV<ContractTemplateConfigV> page(PageF<ContractTemplateConfigPageF> request) {
        return contractTemplateConfigService.page(request);
    }

    public PageV<ContractTemplateConfigV> frontPage(PageF<SearchF<ContractTemplateConfigE>> request) {
        return contractTemplateConfigService.frontPage(request);
    }
}
