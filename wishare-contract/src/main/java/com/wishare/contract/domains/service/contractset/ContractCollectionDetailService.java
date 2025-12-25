package com.wishare.contract.domains.service.contractset;

import com.wishare.contract.domains.entity.contractset.ContractCollectionDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractCollectionDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.vo.contractset.CollectionDetailPlanV;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * <p>
 * 合同收款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Service
@Slf4j
public class ContractCollectionDetailService extends ServiceImpl<ContractCollectionDetailMapper, ContractCollectionDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractCollectionDetailMapper contractCollectionDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;

    public Long selectCountByCollectionPlanId(Long collectionPlanId) {
        LambdaQueryWrapper<ContractCollectionDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCollectionDetailE::getCollectionPlanId, collectionPlanId);
        return contractCollectionDetailMapper.selectCount(queryWrapper);
    }



    public List<CollectionDetailPlanV> contractCollectionDetailList(Long contractId, Long collectionPlanId, String tenantId) {
        List<CollectionDetailPlanV> collectionDetailES = contractCollectionDetailMapper.selectByCollectionPlanId(contractId, collectionPlanId, tenantId);
        if (!collectionDetailES.isEmpty()) {
            collectionDetailES.forEach(item -> {
                // 文件获取
                if (StringUtils.isNotBlank(item.getReceiptVoucher()) && StringUtils.isNotBlank(item.getReceiptVoucherName())) {
                    item.setReceiptVoucherFileList(fileStorageUtils.getFileNameList(item.getReceiptVoucher(), item.getReceiptVoucherName()));
                }
            });
            return collectionDetailES;
        } else {
            return new ArrayList<>();
        }
    }

    public void deleteByCollectionPlanId(Long collectionPlanId) {
        contractCollectionDetailMapper.deleteByCollectionPlanId(collectionPlanId);
    }

    public void deleteByContractId(Long contractId) {
        contractCollectionDetailMapper.deleteByCollectionPlanId(contractId);
    }

    public Long selectCountCurrentDate() {
        return contractCollectionDetailMapper.selectCountCurrentDate();
    }
}
