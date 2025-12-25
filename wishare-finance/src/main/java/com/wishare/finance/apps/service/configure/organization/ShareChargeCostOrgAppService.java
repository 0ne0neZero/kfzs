package com.wishare.finance.apps.service.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.ShareChargeCostOrgF;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.service.ShareChargeCostOrgDomainService;
import com.wishare.starter.interfaces.ApiBase;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Service
public class ShareChargeCostOrgAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ShareChargeCostOrgDomainService statutoryBodyAccountDomainService;

    /**
     * 查询分成费项与成本中心关联关系
     * @param costOrdId 成本中心id
     * @return
     */
    public List<ShareChargeCostOrgDto> queryShareChargeCostCenterRelation(Long costOrdId) {
        return statutoryBodyAccountDomainService.queryShareChargeCostCenterRelation(costOrdId);
    }

    /**
     * 根据成本中心和费项查询分成费项信息
     * @param costOrdId 成本中心id
     * @param shareChargeId 费项id
     * @return
     */
    public ShareChargeCostOrgDto getShareChargeInfo(Long costOrdId, Long shareChargeId) {
        return statutoryBodyAccountDomainService.getShareChargeInfo(costOrdId, shareChargeId);
    }

    /**
     * 编辑成本中心关联关系
     * @param shareChargeCostOrgF
     * @return
     */
    public Boolean editShareChargeCostCenterRelation(ShareChargeCostOrgF shareChargeCostOrgF) {
        return statutoryBodyAccountDomainService.editShareChargeCostCenterRelation(shareChargeCostOrgF);
    }
}
