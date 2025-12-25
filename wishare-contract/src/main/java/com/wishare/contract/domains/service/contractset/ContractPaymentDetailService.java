package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractPaymentDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wishare.contract.domains.vo.contractset.ContractPaymentDetailV;
import com.wishare.starter.Global;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 合同付款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-29
 */
@Service
@Slf4j
public class ContractPaymentDetailService extends ServiceImpl<ContractPaymentDetailMapper, ContractPaymentDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractPaymentDetailMapper contractPaymentDetailMapper;

    public Long selectCountByCollectionPlanId(Long collectionPlanId) {
        LambdaQueryWrapper<ContractPaymentDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractPaymentDetailE::getCollectionPlanId, collectionPlanId);
        return contractPaymentDetailMapper.selectCount(queryWrapper);
    }

    public List<ContractPaymentDetailV> contractPaymentDetailList(Long contractId, Long collectionPlanId, String tenantId) {
        LambdaQueryWrapper<ContractPaymentDetailE> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(contractId) && contractId != 0) {
            queryWrapper.eq(ContractPaymentDetailE::getContractId, contractId);
        }
        if (Objects.nonNull(collectionPlanId) && collectionPlanId != 0) {
            queryWrapper.eq(ContractPaymentDetailE::getCollectionPlanId, collectionPlanId);
        }
        queryWrapper.eq(ContractPaymentDetailE::getTenantId, tenantId);
        queryWrapper.orderByAsc(ContractPaymentDetailE::getApplyPaymentTime);
        queryWrapper.orderByAsc(ContractPaymentDetailE::getActualPaymentTime);
        return Global.mapperFacade.mapAsList(contractPaymentDetailMapper.selectList(queryWrapper), ContractPaymentDetailV.class);
    }

    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractPaymentDetailMapper.deleteByCollectionPlanId(collectionPlanId);
    }

    public void deleteByContractId(Long contractId) {
        contractPaymentDetailMapper.deleteByCollectionPlanId(contractId);
    }

    public Long selectCountCurrentDate() {
        return contractPaymentDetailMapper.selectCountCurrentDate();
    }
}
