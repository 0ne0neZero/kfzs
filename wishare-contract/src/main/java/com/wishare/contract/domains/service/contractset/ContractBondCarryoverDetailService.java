package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractBondPaymentDetailFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractBondCarryoverDetailE;
import com.wishare.contract.domains.entity.contractset.ContractBondPaymentDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractBondCarryoverDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.vo.contractset.ContractBondPaymentDetailV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.ContractBondCarryoverDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondCarryoverDetailF;
import com.wishare.contract.apps.fo.contractset.ContractBondCarryoverDetailPageF;
import com.wishare.contract.apps.fo.contractset.ContractBondCarryoverDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondCarryoverDetailUpdateF;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.ContractBondCarryoverDetailFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
/**
 * <p>
 * 保证金结转明细表
 * </p>
 *
 * @author ljx
 * @since 2022-11-21
 */
@Service
@Slf4j
public class ContractBondCarryoverDetailService extends ServiceImpl<ContractBondCarryoverDetailMapper, ContractBondCarryoverDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondCarryoverDetailMapper contractBondCarryoverDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    public Long saveBondPlanCarryoverDetail(ContractBondCarryoverDetailF carryoverDetailF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        String auditCode = carryoverAuditCode(identityInfo.getTenantId(), carryoverDetailF.getBondPlanId());
        ContractBondCarryoverDetailE map = Global.mapperFacade.map(carryoverDetailF, ContractBondCarryoverDetailE.class);
        map.setId(UidHelper.nextId("contract_bond_carryover_detail"));
        // 审批编号
        map.setAuditCode(auditCode);
        // 创建人操作人
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        save(map);
        return map.getId();
    }

    public List<ContractBondCarryoverDetailV> listByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondCarryoverDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondCarryoverDetailE::getBondPlanId, bondPlanId);
        return Global.mapperFacade.mapAsList(contractBondCarryoverDetailMapper.selectList(queryWrapper), ContractBondCarryoverDetailV.class);
    }

    public Long selectCountByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondCarryoverDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondCarryoverDetailE::getBondPlanId, bondPlanId);
        return contractBondCarryoverDetailMapper.selectCount(queryWrapper);
    }

    private String carryoverAuditCode(String tenantId, Long bondPlanId) {
        String str = "BZJJZ";
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = selectCountByBondPlanId(bondPlanId);
        //审批编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + str + year + code;
        } else {
            return str + year + code;
        }
    }
}
