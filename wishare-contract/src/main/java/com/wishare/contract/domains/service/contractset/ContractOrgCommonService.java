package com.wishare.contract.domains.service.contractset;

import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.starter.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class ContractOrgCommonService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrgEnhanceComponent orgEnhanceComponent;

    @Value("${root.org.id:13554968509111}")
    private Long rootOrgId;

    /**
     * 查询指定用户权限范围内的组织id集合
     * 和合同列表中查询权限范围内的组织完全一致
     *
     * @param userId
     * @return
     */
    public ContractOrgPermissionV queryAuthOrgIds(String userId, List<Long> orgIds) {
        log.info("查询用户权限范围内的组织,userId:{},orgIds:{}", userId, orgIds);
        ContractOrgPermissionV orgPermissionV = new ContractOrgPermissionV();
        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId);
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId);
        log.info("用户所属组织:{}", userInfoRv.getOrgIds());
        if (userInfoRv.getOrgIds().contains(rootOrgId)) {
            superAccount = true;
        }
        if (superAccount) {
            log.info("当前用户是superAccount");
            orgPermissionV.setRadio(RadioEnum.ALL);
            return orgPermissionV;
        }
        Set<String> orgIdList = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds, String.class));
        log.info("当前用户权限范围内的组织集合:{}", orgIdList);
        if (CollectionUtils.isEmpty(orgIdList)) {
            orgPermissionV.setRadio(RadioEnum.NONE);
            return orgPermissionV;
        }
        orgPermissionV.setRadio(RadioEnum.APPOINT);
        orgPermissionV.setOrgIds(orgIdList);
        return orgPermissionV;
    }
}
