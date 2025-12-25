package com.wishare.contract.domains.service.contractset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.contractset.ContractBondCollectionDetailFieldConst;
import com.wishare.contract.domains.consts.contractset.ContractCollectionDetailFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractBondCollectionDetailE;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.wishare.contract.domains.mapper.contractset.ContractBondCollectionDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.vo.contractset.ContractBondCollectionDetailV;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailF;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.wishare.starter.helpers.UidHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 保证金计划收款明细
 * </p>
 *
 * @author ljx
 * @since 2022-10-25
 */
@Service
@Slf4j
public class ContractBondCollectionDetailService extends ServiceImpl<ContractBondCollectionDetailMapper, ContractBondCollectionDetailE>  {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondCollectionDetailMapper contractBondCollectionDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;

    public ContractBondCollectionDetailE saveBondCollectionDetail(ContractBondCollectionDetailF collectionDetailF, IdentityInfo identityInfo) {
        LocalDateTime now = LocalDateTime.now();
        String collectionApplyNumber = collectionApplyNumber(identityInfo.getTenantId(), collectionDetailF.getBondPlanId());
        ContractBondCollectionDetailE map = Global.mapperFacade.map(collectionDetailF, ContractBondCollectionDetailE.class);
        map.setId(UidHelper.nextId(ContractBondCollectionDetailFieldConst.CONTRACT_BOND_COLLECTION_DETAIL));
        // 付款编号
        map.setCollectionCode(collectionApplyNumber);
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
        LambdaQueryWrapper<ContractBondCollectionDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondCollectionDetailE::getBondPlanId, bondPlanId);
        return contractBondCollectionDetailMapper.selectCount(queryWrapper);
    }

    public List<ContractBondCollectionDetailV> listByBondPlanId(Long bondPlanId) {
        LambdaQueryWrapper<ContractBondCollectionDetailE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractBondCollectionDetailE::getBondPlanId, bondPlanId);
        List<ContractBondCollectionDetailE> contractBondCollectionDetailES = contractBondCollectionDetailMapper.selectList(queryWrapper);
        List<ContractBondCollectionDetailV> contractBondCollectionDetailVS =
                Global.mapperFacade.mapAsList(contractBondCollectionDetailES, ContractBondCollectionDetailV.class);
        if (!contractBondCollectionDetailVS.isEmpty()) {
            contractBondCollectionDetailVS.forEach(item -> {
                // 文件获取
                if (StringUtils.isNotBlank(item.getReceiptVoucher()) && StringUtils.isNotBlank(item.getReceiptVoucherName())) {
                    item.setReceiptVoucherFileVos(fileStorageUtils.getFileNameList(item.getReceiptVoucher(), item.getReceiptVoucherName()));
                }
            });
            return contractBondCollectionDetailVS;
        } else {
            return new ArrayList<>();
        }
    }

    private String collectionApplyNumber(String tenantId, Long bondPlanId) {
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Long count = selectCountByBondPlanId(bondPlanId);
        //收款编号
        String code = String.format("%0" + 3 + "d", count + 1);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        if (null != tenantInfoRv && StringUtils.isNotBlank(tenantInfoRv.getEnglishName())) {
            return tenantInfoRv.getEnglishName() + "BZJSK" + year + code;
        } else {
            return "BZJSK" + year + code;
        }
    }
}
