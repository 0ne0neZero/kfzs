package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.apps.remote.vo.InvoiceInfoRv;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractInvoiceDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.vo.contractset.ContractInvoiceDetailV;
import com.wishare.contract.domains.vo.contractset.InvoiceDetailPlanV;
import java.util.ArrayList;

import com.wishare.starter.Global;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
/**
 * <p>
 * 合同开票明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Service
@Slf4j
public class ContractInvoiceDetailService extends ServiceImpl<ContractInvoiceDetailMapper, ContractInvoiceDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceDetailMapper contractInvoiceDetailMapper;

    public Long selectCountByCollectionPlanId(Long collectionPlanId) {
        LambdaQueryWrapper<ContractInvoiceDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractInvoiceDetailE::getCollectionPlanId, collectionPlanId);
        return contractInvoiceDetailMapper.selectCount(queryWrapper);
    }

    public List<InvoiceDetailPlanV> contractInvoiceDetailList(Long contractId, Long collectionPlanId, String tenantId) {
        List<InvoiceDetailPlanV> invoiceDetailPlanVS = contractInvoiceDetailMapper.selectByCollectionPlanId(contractId, collectionPlanId, tenantId);
        if (!invoiceDetailPlanVS.isEmpty()) {
            return invoiceDetailPlanVS;
        } else {
            return new ArrayList<>();
        }
    }


    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractInvoiceDetailMapper.deleteByCollectionPlanId(collectionPlanId);
    }

    public void deleteByContractId(Long contractId) {
        contractInvoiceDetailMapper.deleteByCollectionPlanId(contractId);
    }

    /**
     * 更新开票相关信息
     * @param invoiceInfoRv
     */
    public void updateInvoiceInfo(InvoiceInfoRv invoiceInfoRv) {
        contractInvoiceDetailMapper.updateInvoiceInfo(invoiceInfoRv);
    }

    public List<ContractInvoiceDetailV> invoiceDetailListByInvoiceId(Long invoiceId) {
        LambdaQueryWrapper<ContractInvoiceDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractInvoiceDetailE::getInvoiceId,invoiceId);
        return Global.mapperFacade.mapAsList(list(queryWrapper), ContractInvoiceDetailV.class);
    }


    public Long selectCountCurrentDate() {
        return contractInvoiceDetailMapper.selectCountCurrentDate();
    }
}
