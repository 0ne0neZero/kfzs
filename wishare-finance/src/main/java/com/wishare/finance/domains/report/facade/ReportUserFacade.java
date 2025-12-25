package com.wishare.finance.domains.report.facade;

import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.vo.user.UserTenantPermissionRv;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 空间中心防腐层
 *
 * @author yancao
 */
@Service
@Slf4j
public class ReportUserFacade {

    @Setter(onMethod_ = {@Autowired})
    private UserClient userClient;


    /**
     * 获取用户拥有的权限id
     *
     * @param userId   用户id
     * @param dataType dataType=1表示小区 dataType=2表示空间节点
     * @return List
     */
    public List<String> listDataPermissions(String userId, String dataType) {
        List<UserTenantPermissionRv> userPermissionsList = userClient.listDataPermissions(userId, dataType);
        return userPermissionsList.stream().
                filter(s -> !s.getDisabled())
                .map(UserTenantPermissionRv::getDataId)
                .collect(Collectors.toList());
    }
}
