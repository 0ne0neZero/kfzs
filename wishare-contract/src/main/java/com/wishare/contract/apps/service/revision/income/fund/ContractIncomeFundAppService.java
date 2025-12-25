package com.wishare.contract.apps.service.revision.income.fund;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundPageF;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundListF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundSaveF;
import com.wishare.contract.apps.fo.revision.income.fund.ContractIncomeFundUpdateF;
import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundListV;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收入合同-款项表
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Service
@Slf4j
public class ContractIncomeFundAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeFundService contractIncomeFundService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    public ContractIncomeFundV get(ContractIncomeFundF contractIncomeFundF){
        return contractIncomeFundService.get(contractIncomeFundF).orElse(null);
    }

    public ContractIncomeFundV get(String id){
        return contractIncomeFundService.get(new ContractIncomeFundF().setId(id)).orElse(null);
    }

    public ContractIncomeFundListV list(ContractIncomeFundListF contractIncomeFundListF){
        return contractIncomeFundService.list(contractIncomeFundListF);
    }

    public ContractIncomeFundE save(ContractIncomeFundSaveF contractIncomeFundF){
        return contractIncomeFundService.save(contractIncomeFundF);
    }

    public void update(ContractIncomeFundUpdateF contractIncomeFundF){
        contractIncomeFundService.update(contractIncomeFundF);
    }

    public boolean removeById(String id){
        return contractIncomeFundService.removeById(id);
    }

    public PageV<ContractIncomeFundV> page(PageF<ContractIncomeFundPageF> request) {
        return contractIncomeFundService.page(request);
    }

    public PageV<ContractIncomeFundV> frontPage(PageF<SearchF<ContractIncomeFundE>> request) {
        PageV<ContractIncomeFundV> pageV = contractIncomeFundService.frontPage(request);
        for (ContractIncomeFundV record : pageV.getRecords()) {
            QueryWrapper<ContractIncomeConcludeE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(ContractIncomeConcludeE.ID, record.getContractId());
            ContractIncomeConcludeE concludeE =  contractIncomeConcludeMapper.selectOne(queryWrapper);
            if(ObjectUtils.isNotEmpty(concludeE) && concludeE.getReviewStatus().equals(ReviewStatusEnum.审批中.getCode())){
                record.setListDeleted(false);
            }else{
                record.setListDeleted(true);
            }
            if(record.getStandAmount().compareTo(BigDecimal.ZERO) != 0){
                record.setStandardShow(record.getStandAmount().toString() + record.getStandard());
            }
            //1230：处理单价和数量的小数位，其余金额字段，前端已处理为2位
            if (ObjectUtils.isNotEmpty(record.getStandAmount())) {
                record.setStandAmount(record.getStandAmount().setScale(6, RoundingMode.HALF_UP));
            }
            if (ObjectUtils.isNotEmpty(record.getAmountNum())) {
                record.setAmountNum(record.getAmountNum().setScale(2, RoundingMode.HALF_UP));
            }
            record.setChargeItemAllPath(financeFeignClient.chargeName(Long.valueOf(record.getChargeItemId())));
        }
        return pageV;
    }

    public List<ContractIncomeFundV> getFundForContract(String id) {
        QueryWrapper<ContractIncomeFundE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, id)
                .eq(ContractPayFundE.DELETED, 0);
        List<ContractIncomeFundE> list = contractIncomeFundService.list(queryWrapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : Global.mapperFacade.mapAsList(list, ContractIncomeFundV.class);
    }
}
