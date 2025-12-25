package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanUpdateF;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossPlanE;
import com.wishare.contract.domains.mapper.contractset.ContractProfitLossPlanMapper;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossPlanV;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.helpers.UidHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author wangrui
 * @since 2022-09-13
 */
@Service
@Slf4j
public class ContractProfitLossPlanService extends ServiceImpl<ContractProfitLossPlanMapper, ContractProfitLossPlanE> {

    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanMapper contractProfitLossPlanMapper;


    @Nullable
    public List<ContractProfitLossPlanV> getProfitLossPlanList(ContractProfitLossPlanF profitLossPlanF) {
        return contractProfitLossPlanMapper.getProfitLossPlanList(profitLossPlanF);
    }

    public Long saveContractProfitLossPlan(ContractProfitLossPlanSaveF contractProfitLossPlanF) {
        ContractProfitLossPlanE map = Global.mapperFacade.map(contractProfitLossPlanF, ContractProfitLossPlanE.class);
        map.setId(UidHelper.nextId("contract_profit_loss_plan"));
        contractProfitLossPlanMapper.insert(map);
        return map.getId();
    }

    public void deleteProfitLossPlan(Long collectionPlanId, Long contractId) {
        contractProfitLossPlanMapper.deleteProfitLossPlan(collectionPlanId, contractId);
    }

    public void delete( Long contractId) {
        contractProfitLossPlanMapper.deletePlan(contractId);
    }

    public List<ContractProfitLossPlanV> listProfitLossPlan(ContractProfitLossPlanF profitLossPlanF) {
        LambdaQueryWrapper<ContractProfitLossPlanE> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper
                .eq(ContractProfitLossPlanE::getContractId, profitLossPlanF.getContractId())
                .eq(ContractProfitLossPlanE::getDeleted, Const.State._0);
        return Global.mapperFacade.mapAsList(contractProfitLossPlanMapper.selectList(objectLambdaQueryWrapper), ContractProfitLossPlanV.class);
    }

    public void updateContractProfitLossPlan(ContractProfitLossPlanUpdateF contractProfitLossPlanF) {
        ContractProfitLossPlanE map = Global.mapperFacade.map(contractProfitLossPlanF, ContractProfitLossPlanE.class);
        contractProfitLossPlanMapper.updateById(map);
    }

    public BigDecimal selectAmountSum(Long id) {
        return contractProfitLossPlanMapper.selectAmountSum(id);
    }

    public BigDecimal localCurrencyAmount(Long id) {
        return contractProfitLossPlanMapper.localCurrencyAmount(id);
    }

    public void updateBillId(Long id, Long billId) {
        contractProfitLossPlanMapper.updateBillId(id,billId);
    }

    public List<ContractProfitLossPlanV> selectProfitLossPlanByIds(List<Long> ids) {
        return contractProfitLossPlanMapper.selectProfitLossPlanByIds(ids);
    }

    public void removePlan(Long id) {
        ContractProfitLossPlanE map = new ContractProfitLossPlanE();
        map.setId(id);
        map.setDeleted(Const.State._1);
        contractProfitLossPlanMapper.updateById(map);
    }

    public List<ContractProfitLossPlanV> selectByContract(Long contractId) {
        return contractProfitLossPlanMapper.selectByContract(contractId);
    }
}
