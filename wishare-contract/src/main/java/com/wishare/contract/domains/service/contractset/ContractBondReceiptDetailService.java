package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.domains.entity.contractset.ContractBondReceiptDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractBondReceiptDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.contract.domains.vo.contractset.ContractBondReceiptDetailV;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
/**
 * <p>
 * 保证金收据明细表
 * </p>
 *
 * @author ljx
 * @since 2022-12-12
 */
@Service
@Slf4j
public class ContractBondReceiptDetailService extends ServiceImpl<ContractBondReceiptDetailMapper, ContractBondReceiptDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondReceiptDetailMapper contractBondReceiptDetailMapper;

    public ContractBondReceiptDetailE saveBondReceiptDetail(ContractBondReceiptDetailF receiptDetailF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        ContractBondReceiptDetailE map = Global.mapperFacade.map(receiptDetailF, ContractBondReceiptDetailE.class);
        map.setId(UidHelper.nextId("contract_bond_receipt_detail"));
        // 创建人操作人
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        save(map);
        return map;
    }

    public List<ContractBondReceiptDetailV> listByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondReceiptDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondReceiptDetailE::getBondPlanId, bondPlanId);
        List<ContractBondReceiptDetailE> contractBondCollectionDetailES = contractBondReceiptDetailMapper.selectList(queryWrapper);
        List<ContractBondReceiptDetailV> contractBondCollectionDetailVS =
                Global.mapperFacade.mapAsList(contractBondCollectionDetailES, ContractBondReceiptDetailV.class);
        if (!contractBondCollectionDetailVS.isEmpty()) {
            return contractBondCollectionDetailVS;
        } else {
            return new ArrayList<>();
        }
    }

    public ContractBondReceiptDetailE getByInvoiceNumber (String invoiceNumber) {
        LambdaQueryWrapper<ContractBondReceiptDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondReceiptDetailE::getInvoiceNumber, invoiceNumber);
        return contractBondReceiptDetailMapper.selectOne(queryWrapper);
    }
}
