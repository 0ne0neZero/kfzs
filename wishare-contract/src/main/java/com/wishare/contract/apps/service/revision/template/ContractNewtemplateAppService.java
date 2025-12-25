package com.wishare.contract.apps.service.revision.template;

import com.wishare.tools.starter.vo.FileVo;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.template.ContractNewtemplateE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplatePageF;
import com.wishare.contract.domains.service.revision.template.ContractNewtemplateService;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateV;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateListF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractNewtemplateUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractNewtemplateListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;

/**
* <p>
* 新合同范本表
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Service
@Slf4j
public class ContractNewtemplateAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractNewtemplateService contractNewtemplateService;

    public ContractNewtemplateV get(ContractNewtemplateF contractNewtemplateF){
        return contractNewtemplateService.get(contractNewtemplateF).orElse(null);
    }

    public List<ContractNewtemplateV> list(ContractNewtemplateListF contractNewtemplateListF){
        return contractNewtemplateService.list(contractNewtemplateListF);
    }

    public String save(ContractNewtemplateSaveF contractNewtemplateF){
        return contractNewtemplateService.save(contractNewtemplateF);
    }

    public void update(ContractNewtemplateUpdateF contractNewtemplateF){
        contractNewtemplateService.update(contractNewtemplateF);
    }

    public void disable(String id){
        contractNewtemplateService.disable(id);
    }

    public void able(String id){
        contractNewtemplateService.able(id);
    }

    public String preview(String id){
         return contractNewtemplateService.preview(id);
    }

    public boolean removeById(String id){
        return contractNewtemplateService.removeById(id);
    }

    public PageV<ContractNewtemplateV> page(PageF<ContractNewtemplatePageF> request) {
        return contractNewtemplateService.page(request);
    }

    public PageV<ContractNewtemplateV> frontPage(PageF<SearchF<ContractNewtemplateE>> request) {
        return contractNewtemplateService.frontPage(request);
    }
}
