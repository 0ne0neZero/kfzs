package com.wishare.contract.apps.service.revision.template;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.template.ContractRecordInfoE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoPageF;
import com.wishare.contract.domains.service.revision.template.ContractRecordInfoService;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoV;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoListF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoSaveF;
import com.wishare.contract.apps.fo.revision.template.ContractRecordInfoUpdateF;
import com.wishare.contract.domains.vo.revision.template.ContractRecordInfoListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同修改记录表
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Service
@Slf4j
public class ContractRecordInfoAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractRecordInfoService contractRecordInfoService;

    public ContractRecordInfoV get(ContractRecordInfoF contractRecordInfoF){
        return contractRecordInfoService.get(contractRecordInfoF).orElse(null);
    }

    public ContractRecordInfoListV list(ContractRecordInfoListF contractRecordInfoListF){
        return contractRecordInfoService.list(contractRecordInfoListF);
    }

    public String save(ContractRecordInfoSaveF contractRecordInfoF){
        return contractRecordInfoService.save(contractRecordInfoF);
    }

    public void update(ContractRecordInfoUpdateF contractRecordInfoF){
        contractRecordInfoService.update(contractRecordInfoF);
    }

    public boolean removeById(String id){
        return contractRecordInfoService.removeById(id);
    }

    public PageV<ContractRecordInfoV> page(PageF<ContractRecordInfoPageF> request) {
        return contractRecordInfoService.page(request);
    }

    public PageV<ContractRecordInfoV> frontPage(PageF<SearchF<ContractRecordInfoE>> request) {
        return contractRecordInfoService.frontPage(request);
    }
}
