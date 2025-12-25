package com.wishare.finance.domains.configure.organization.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.ShareChargeCostOrgE;
import com.wishare.finance.domains.configure.organization.repository.mapper.ShareChargeCostOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Service
public class ShareChargeCostOrgRepository extends ServiceImpl<ShareChargeCostOrgMapper, ShareChargeCostOrgE> {

    @Autowired
    private ShareChargeCostOrgMapper shareChargeCostOrgMapper;

    /**
     * 查询分成费项与成本中心关联关系
     * @param costOrdId 成本中心id
     * @return
     */
    public List<ShareChargeCostOrgDto> getRelation(Long costOrdId) {
        return shareChargeCostOrgMapper.getRelation(costOrdId);
    }


    /**
     * 根据成本中心和费项查询分成费项信息
     *@param costOrdId 成本中心id
     *@param shareChargeId 费项id
     * @return
     */
    public ShareChargeCostOrgDto getShareChargeInfo(Long costOrdId, Long shareChargeId) {
        return shareChargeCostOrgMapper.getShareChargeInfo(costOrdId, shareChargeId);
    }

}
