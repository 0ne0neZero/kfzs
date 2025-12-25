package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractBondCollectionDetailFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractBondCollectionDetailE;
import com.wishare.contract.domains.entity.contractset.ContractBondPaymentDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractBondPaymentDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractBondPaymentDetailV;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractBondPaymentDetailFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
/**
 * <p>
 * 保证金计划付/退款明细
 * </p>
 *
 * @author ljx
 * @since 2022-10-25
 */
@Service
@Slf4j
public class ContractBondPaymentDetailService extends ServiceImpl<ContractBondPaymentDetailMapper, ContractBondPaymentDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondPaymentDetailMapper contractBondPaymentDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    public ContractBondPaymentDetailE saveBondPaymentDetail(ContractBondPaymentDetailF collectionDetailF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        String paymentApplyNumber = paymentApplyNumber(identityInfo.getTenantId(), collectionDetailF.getBondPlanId(), collectionDetailF.getType());
        ContractBondPaymentDetailE map = Global.mapperFacade.map(collectionDetailF, ContractBondPaymentDetailE.class);
        map.setId(UidHelper.nextId(ContractBondPaymentDetailFieldConst.CONTRACT_BOND_PAYMENT_DETAIL));
        // 付款编号
        map.setPaymentNumber(paymentApplyNumber);
        // 创建人操作人
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        map.setOperator(identityInfo.getUserId());
        map.setOperatorName(identityInfo.getUserName());
        map.setGmtModify(now);
        save(map);
        return map;
    }

    public Long selectCountByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondPaymentDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondPaymentDetailE::getBondPlanId, bondPlanId);
        return contractBondPaymentDetailMapper.selectCount(queryWrapper);
    }

    public List<ContractBondPaymentDetailV> listByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondPaymentDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondPaymentDetailE::getBondPlanId, bondPlanId);
        return Global.mapperFacade.mapAsList(contractBondPaymentDetailMapper.selectList(queryWrapper), ContractBondPaymentDetailV.class);
    }

    private String paymentApplyNumber(String tenantId, Long bondPlanId, Integer type) {
        String str = "";
        if (type == 1) {
            str = "BZJFK";
        } else if(type == 2){
            str = "BZJTK";
        }else{
            //扣款
            str = "BZJKK";
        }
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = selectCountByBondPlanId(bondPlanId);
        //付/退款编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + str + year + code;
        } else {
            return str + year + code;
        }
    }
}
