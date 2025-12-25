package com.wishare.contract.apps.service.revision.pay.team;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.pay.team.ContractPayTeamE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamPageF;
import com.wishare.contract.domains.service.revision.pay.team.ContractPayTeamService;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamV;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamListF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamSaveF;
import com.wishare.contract.apps.fo.revision.pay.team.ContractPayTeamUpdateF;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamListV;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 支出合同-团队表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Service
@Slf4j
public class ContractPayTeamAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayTeamService contractPayTeamService;

    public ContractPayTeamV get(ContractPayTeamF contractPayTeamF){
        return contractPayTeamService.get(contractPayTeamF).orElse(null);
    }

    public ContractPayTeamV get(String id){
        return contractPayTeamService.get(new ContractPayTeamF().setId(id)).orElse(null);
    }

    public ContractPayTeamListV list(ContractPayTeamListF contractPayTeamListF){
        return contractPayTeamService.list(contractPayTeamListF);
    }

    public String save(ContractPayTeamSaveF contractPayTeamF){
        return contractPayTeamService.save(contractPayTeamF);
    }

    public void update(ContractPayTeamUpdateF contractPayTeamF){
        contractPayTeamService.update(contractPayTeamF);
    }

    public boolean removeById(String id){
        return contractPayTeamService.removeById(id);
    }

    public PageV<ContractPayTeamV> page(PageF<ContractPayTeamPageF> request) {
        return contractPayTeamService.page(request);
    }

    public PageV<ContractPayTeamV> frontPage(PageF<SearchF<ContractPayTeamE>> request) {
        return contractPayTeamService.frontPage(request);
    }

    public List<ContractPayTeamV> getTeamForContract(String id) {
        QueryWrapper<ContractPayTeamE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractPayTeamE.GMT_CREATE)
                .eq(ContractPayTeamE.TENANT_ID, tenantId())
                .eq(ContractPayTeamE.CONTRACT_ID, id);
        List<ContractPayTeamE> list = contractPayTeamService.list(queryWrapper);

        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : Global.mapperFacade.mapAsList(list, ContractPayTeamV.class);
    }

}
