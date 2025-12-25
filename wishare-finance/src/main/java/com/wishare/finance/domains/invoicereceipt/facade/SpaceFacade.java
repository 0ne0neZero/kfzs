package com.wishare.finance.domains.invoicereceipt.facade;

import com.google.common.collect.Lists;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.CommunityRelationRF;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityMtmTenantCommunityRV;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/11/3
 * @Description:
 */
@Service
public class SpaceFacade {

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;

    /**
     * 空间中心获取诺诺对应的小区id映射标识
     */
    private final String code = "nuonuo";

    /**
     * 根据项目id获取诺诺映射的小区id
     * @param communityId
     */
    public String getNuonuoCommunityId(String communityId) {
        if (StringUtils.isNotBlank(communityId)) {
            CommunityRelationRF communityRelationRF = new CommunityRelationRF();
            communityRelationRF.setCode(code);
            communityRelationRF.setCommunityIds(Lists.newArrayList(communityId));
            List<SpaceCommunityMtmTenantCommunityRV> spaceCommunityMtmTenantCommunityRVS = spaceClient.communityRelationList(communityRelationRF);
            if (CollectionUtils.isNotEmpty(spaceCommunityMtmTenantCommunityRVS)) {
                SpaceCommunityMtmTenantCommunityRV spaceCommunityMtmTenantCommunityRV = spaceCommunityMtmTenantCommunityRVS.get(0);
                return spaceCommunityMtmTenantCommunityRV.getTenantCommunityId();
            }
        }
        return null;
    }
}
