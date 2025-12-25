package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.wishare.contract.domains.entity.contractset.ContractReceiveInvoiceDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractReceiveInvoiceDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.vo.contractset.ContractPaymentDetailV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractReceiveInvoiceDetailV;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailPageF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailUpdateF;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractReceiveInvoiceDetailFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 付款计划收票明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-29
 */
@Service
@Slf4j
public class ContractReceiveInvoiceDetailService extends ServiceImpl<ContractReceiveInvoiceDetailMapper, ContractReceiveInvoiceDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractReceiveInvoiceDetailMapper contractReceiveInvoiceDetailMapper;

    public ContractReceiveInvoiceDetailE saveReceiveInvoiceDetail(ContractReceiveInvoiceDetailF from, IdentityInfo identityInfo) {
        ContractReceiveInvoiceDetailE map = Global.mapperFacade.map(from, ContractReceiveInvoiceDetailE.class);
        map.setId(UidHelper.nextId("contract_receive_invoice_detail"));
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(LocalDateTime.now());
        contractReceiveInvoiceDetailMapper.insert(map);
        return map;
    }


    public List<ContractReceiveInvoiceDetailV> contractReceiveInvoiceDetailList(Long contractId, Long collectionPlanId) {
        LambdaQueryWrapper<ContractReceiveInvoiceDetailE> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(contractId) && contractId != 0) {
            queryWrapper.eq(ContractReceiveInvoiceDetailE::getContractId, contractId);
        }
        if (Objects.nonNull(collectionPlanId) && collectionPlanId != 0) {
            queryWrapper.eq(ContractReceiveInvoiceDetailE::getCollectionPlanId, collectionPlanId);
        }
        queryWrapper.orderByDesc(ContractReceiveInvoiceDetailE::getInvoiceTime);
        return Global.mapperFacade.mapAsList(contractReceiveInvoiceDetailMapper.selectList(queryWrapper), ContractReceiveInvoiceDetailV.class);
    }
}
