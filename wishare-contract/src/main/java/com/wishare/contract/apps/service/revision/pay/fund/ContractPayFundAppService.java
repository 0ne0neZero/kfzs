package com.wishare.contract.apps.service.revision.pay.fund;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.service.revision.pay.ContractPayBusinessService;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.vo.contractset.ContractPayCostPlanReqV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayCostPlanV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundPageF;
import com.wishare.contract.domains.service.revision.pay.fund.ContractPayFundService;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundListF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundSaveF;
import com.wishare.contract.apps.fo.revision.pay.fund.ContractPayFundUpdateF;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundListV;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 支出合同-款项表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Service
@Slf4j
public class ContractPayFundAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayFundService contractPayFundService;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeMapper contractPayConcludeMapper;
    @Autowired
    private ContractPayBusinessService contractPayBusinessService;


    public ContractPayFundV get(ContractPayFundF contractPayFundF){
        return contractPayFundService.get(contractPayFundF).orElse(null);
    }

    public ContractPayFundV getDetail(String id){
        return contractPayFundService.get(new ContractPayFundF().setId(id)).orElse(null);
    }

    public ContractPayFundListV list(ContractPayFundListF contractPayFundListF){
        return contractPayFundService.list(contractPayFundListF);
    }

    public String save(ContractPayFundSaveF saveF){
        return contractPayFundService.save(saveF);
    }

    public void update(ContractPayFundUpdateF contractPayFundF){
        contractPayFundService.update(contractPayFundF);
    }

    public boolean removeById(String id){
        return contractPayFundService.removeById(id);
    }

    public PageV<ContractPayFundV> page(PageF<ContractPayFundPageF> request) {
        return contractPayFundService.page(request);
    }

    public PageV<ContractPayFundV> frontPage(PageF<SearchF<ContractPayFundE>> request) {
        PageV<ContractPayFundV> pageV = contractPayFundService.frontPage(request);
        for (ContractPayFundV record : pageV.getRecords()) {
            QueryWrapper<ContractPayConcludeE> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(ContractPayConcludeE.ID, record.getContractId());
            ContractPayConcludeE contractPayConcludeE = contractPayConcludeMapper.selectOne(queryWrapper);
            if(ObjectUtils.isNotEmpty(contractPayConcludeE) && contractPayConcludeE.getReviewStatus().equals(ReviewStatusEnum.审批中.getCode())){
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
            record.setChargeItemAllPath(financeFeignClient.chargeName(record.getChargeItemId()));
            record.setIsCostData(StringUtils.isEmpty(record.getCbApportionId()) ? 0 : 1) ;
        }
        if(CollectionUtils.isNotEmpty(pageV.getRecords())){
            pageV.getRecords().sort(Comparator.comparing(ContractPayFundV::getIsCostData)
                    .thenComparing(ContractPayFundV::getChargeItemId));
        }
        return pageV;
    }

    public List<ContractPayFundV> getFundForContract(String id) {
        QueryWrapper<ContractPayFundE> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(ContractPayFundE.GMT_CREATE)
                .eq(ContractPayFundE.TENANT_ID, tenantId())
                .eq(ContractPayFundE.CONTRACT_ID, id)
                .eq(ContractPayFundE.DELETED,0);
        List<ContractPayFundE> list = contractPayFundService.list(queryWrapper);
        return CollectionUtils.isEmpty(list) ? new ArrayList<>() : Global.mapperFacade.mapAsList(list, ContractPayFundV.class);
    }

}
