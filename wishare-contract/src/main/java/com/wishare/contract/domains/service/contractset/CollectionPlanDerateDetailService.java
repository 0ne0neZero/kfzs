package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractBondCollectionDetailFieldConst;
import com.wishare.contract.domains.entity.contractset.CollectionPlanDerateDetailE;
import com.wishare.contract.domains.entity.contractset.ContractBondCollectionDetailE;
import com.wishare.contract.domains.mapper.contractset.CollectionPlanDerateDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.contractset.CollectionPlanDerateDetailV;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.wishare.contract.domains.consts.contractset.CollectionPlanDerateDetailFieldConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
/**
 * <p>
 * 收款计划减免明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-07
 */
@Service
@Slf4j
public class CollectionPlanDerateDetailService extends ServiceImpl<CollectionPlanDerateDetailMapper, CollectionPlanDerateDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private CollectionPlanDerateDetailMapper collectionPlanDerateDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    public CollectionPlanDerateDetailE saveBondCollectionDetail(CollectionPlanDerateDetailF collectionDetailF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
//        String auditCode = collectionApplyNumber(identityInfo.getTenantId(), collectionDetailF.getCollectionPlanId());
        CollectionPlanDerateDetailE map = Global.mapperFacade.map(collectionDetailF, CollectionPlanDerateDetailE.class);
        map.setId(UidHelper.nextId("collection_plan_derate_detail"));
        // 审批编号
//        map.setAuditCode(auditCode);
        // 创建人
        map.setCreator(identityInfo.getUserId());
        map.setCreatorName(identityInfo.getUserName());
        map.setGmtCreate(now);
        save(map);
        return map;
    }

    public Long selectCountByCollectionPlanId(Long collectionPlanId) {
        LambdaQueryWrapper<CollectionPlanDerateDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectionPlanDerateDetailE::getCollectionPlanId, collectionPlanId);
        return collectionPlanDerateDetailMapper.selectCount(queryWrapper);
    }

    public List<CollectionPlanDerateDetailV> listByCollectionPlanId(Long collectionPlanId) {
        LambdaQueryWrapper<CollectionPlanDerateDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectionPlanDerateDetailE::getCollectionPlanId, collectionPlanId);
        List<CollectionPlanDerateDetailE> collectionPlanDerateDetailES = collectionPlanDerateDetailMapper.selectList(queryWrapper);
        if (!collectionPlanDerateDetailES.isEmpty()) {
            return Global.mapperFacade.mapAsList(collectionPlanDerateDetailES, CollectionPlanDerateDetailV.class);
        } else {
            return new ArrayList<>();
        }
    }

    private String collectionApplyNumber(String tenantId, Long bondPlanId) {
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = selectCountByCollectionPlanId(bondPlanId);
        //审批编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + "SKJM" + year + code;
        } else {
            return "SKJM" + year + code;
        }
    }
}
