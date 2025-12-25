package com.wishare.contract.apps.service.revision.income.team;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.domains.entity.revision.pay.team.ContractPayTeamE;
import com.wishare.contract.domains.vo.revision.pay.team.ContractPayTeamV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.income.team.ContractIncomeTeamE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamPageF;
import com.wishare.contract.domains.service.revision.income.team.ContractIncomeTeamService;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamV;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamListF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamSaveF;
import com.wishare.contract.apps.fo.revision.income.team.ContractIncomeTeamUpdateF;
import com.wishare.contract.domains.vo.revision.income.team.ContractIncomeTeamListV;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收入合同-团队表
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Service
@Slf4j
public class ContractIncomeTeamAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeTeamService contractIncomeTeamService;

    public ContractIncomeTeamV get(ContractIncomeTeamF contractIncomeTeamF){
        return contractIncomeTeamService.get(contractIncomeTeamF).orElse(null);
    }

    public ContractIncomeTeamV get(String id){
        return contractIncomeTeamService.get(new ContractIncomeTeamF().setId(id)).orElse(null);
    }

    public ContractIncomeTeamListV list(ContractIncomeTeamListF contractIncomeTeamListF){
        return contractIncomeTeamService.list(contractIncomeTeamListF);
    }

    public String save(ContractIncomeTeamSaveF contractIncomeTeamF){
        return contractIncomeTeamService.save(contractIncomeTeamF);
    }

    public void update(ContractIncomeTeamUpdateF contractIncomeTeamF){
        contractIncomeTeamService.update(contractIncomeTeamF);
    }

    public boolean removeById(String id){
        return contractIncomeTeamService.removeById(id);
    }

    public PageV<ContractIncomeTeamV> page(PageF<ContractIncomeTeamPageF> request) {
        return contractIncomeTeamService.page(request);
    }

    public PageV<ContractIncomeTeamV> frontPage(PageF<SearchF<ContractIncomeTeamE>> request) {
        return contractIncomeTeamService.frontPage(request);
    }

    public List<ContractIncomeTeamV> getTeamForContract(String id) {
        QueryWrapper<ContractIncomeTeamE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractPayTeamE.GMT_CREATE)
                .eq(ContractPayTeamE.TENANT_ID, tenantId())
                .eq(ContractPayTeamE.CONTRACT_ID, id);
        List<ContractIncomeTeamE> list = contractIncomeTeamService.list(queryWrapper);

        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : Global.mapperFacade.mapAsList(list, ContractIncomeTeamV.class);
    }
}
