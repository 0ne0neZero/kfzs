package com.wishare.contract.domains.service.revision.income.settdetails;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsUpdateF;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeSettDetailsE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.mapper.revision.income.settdetails.ContractIncomeSettDetailsMapper;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/11:17
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeSettDetailsService extends ServiceImpl<ContractIncomeSettDetailsMapper, ContractIncomeSettDetailsE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeSettDetailsMapper contractPaySettDetailsMapper;

    @Nullable
    public ContractIncomeSettDetailsV getDetailsById(String id){
        ContractIncomeSettDetailsE map = contractPaySettDetailsMapper.selectById(id);
        ContractIncomeSettDetailsV map1 = Global.mapperFacade.map(map, ContractIncomeSettDetailsV.class);
        return map1;
    }

    public String save(ContractIncomeSettDetailsSaveF contractPayFundF) {
        ContractIncomeSettDetailsE map = Global.mapperFacade.map(contractPayFundF, ContractIncomeSettDetailsE.class);
        map.setTenantId(tenantId());
        contractPaySettDetailsMapper.insert(map);
        return map.getId();
    }


    /**
     * 根据Id更新
     *
     * @param contractPaySettDetailsUpdateF 根据Id更新
     */
    public void update(ContractIncomeSettDetailsUpdateF contractPaySettDetailsUpdateF) {
        if (contractPaySettDetailsUpdateF.getId() == null) {
            throw new IllegalArgumentException();
        }
        ContractIncomeSettDetailsE map = Global.mapperFacade.map(contractPaySettDetailsUpdateF, ContractIncomeSettDetailsE.class);
        contractPaySettDetailsMapper.updateById(map);
    }


    /**
     *
     * @param id 根据Id删除
     * @return 删除结果
     */
    public boolean removeById(String id){
        contractPaySettDetailsMapper.deleteById(id);
        return true;
    }


    @Nullable
    public List<ContractIncomeSettDetailsV> getDetailsBySettlementId(String id){
        LambdaQueryWrapper<ContractIncomeSettDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomeSettDetailsE::getSettlementId, id);
        List<ContractIncomeSettDetailsE> contractPaySettlementConcludeEList = contractPaySettDetailsMapper.selectList(queryWrapper);
        List<ContractIncomeSettDetailsV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractPaySettlementConcludeEList, ContractIncomeSettDetailsV.class);
        return contractPaySettDetailsVList;
    }

}
