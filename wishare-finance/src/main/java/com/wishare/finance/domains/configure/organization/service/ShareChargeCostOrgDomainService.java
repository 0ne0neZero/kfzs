package com.wishare.finance.domains.configure.organization.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wishare.finance.apps.model.configure.organization.fo.ShareChargeCostOrgF;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.ShareChargeCostOrgE;
import com.wishare.finance.domains.configure.organization.repository.ShareChargeCostOrgRepository;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.starter.interfaces.ApiBase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShareChargeCostOrgDomainService implements ApiBase {


    private final ShareChargeCostOrgRepository shareChargeCostOrgRepository;



    /**
     * 查询分成费项与成本中心关联关系
     * @param costOrdId 成本中心id
     * @return
     */
    public List<ShareChargeCostOrgDto> queryShareChargeCostCenterRelation(Long costOrdId) {
        return shareChargeCostOrgRepository.getRelation(costOrdId);
    }

    /**
     * 根据成本中心和费项查询分成费项信息
     * @param costOrdId 成本中心id
     * @param shareChargeId 费项id
     * @return
     */
    public ShareChargeCostOrgDto getShareChargeInfo(Long costOrdId, Long shareChargeId) {
        return shareChargeCostOrgRepository.getShareChargeInfo(costOrdId, shareChargeId);
    }

    /**
     * 编辑成本中心关联关系
     * @param shareChargeCostOrgF
     * @return
     */
    @Transactional
    public Boolean editShareChargeCostCenterRelation(ShareChargeCostOrgF shareChargeCostOrgF) {
        List<Long> addShareChargeList = shareChargeCostOrgF.getAddShareChargeList();
        List<Long> deleteShareChargeList = shareChargeCostOrgF.getDeleteShareChargeList();
        if (addShareChargeList != null && addShareChargeList.size() > 0) {
            List<ShareChargeCostOrgE> shareChargeCostOrgES = addShareChargeList.stream().map(shareChargeId -> {
                ShareChargeCostOrgE shareChargeCostOrgE = new ShareChargeCostOrgE();
                shareChargeCostOrgE.setShareChargeId(shareChargeId);
                shareChargeCostOrgE.setShareProportion(shareChargeCostOrgF.getShareProportion());
                shareChargeCostOrgE.setCostOrgId(shareChargeCostOrgF.getCostOrgId());
                return shareChargeCostOrgE;
            }).collect(Collectors.toList());
            shareChargeCostOrgRepository.saveBatch(shareChargeCostOrgES);
        }else {
            LambdaUpdateWrapper<ShareChargeCostOrgE> shareChargeCostOrgE = new LambdaUpdateWrapper<>();
            shareChargeCostOrgE.set(ShareChargeCostOrgE::getShareProportion,shareChargeCostOrgF.getShareProportion());
            shareChargeCostOrgE.eq(ShareChargeCostOrgE::getCostOrgId,shareChargeCostOrgF.getCostOrgId());
            shareChargeCostOrgRepository.update(shareChargeCostOrgE);
        }
        if (deleteShareChargeList != null && deleteShareChargeList.size() > 0) {
            LambdaQueryWrapper<ShareChargeCostOrgE> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ShareChargeCostOrgE::getCostOrgId, shareChargeCostOrgF.getCostOrgId());
            queryWrapper.in(ShareChargeCostOrgE::getShareChargeId, shareChargeCostOrgF.getDeleteShareChargeList());
            shareChargeCostOrgRepository.remove(queryWrapper);
        }
        return true;
    }

}
