package com.wishare.finance.apps.service.acl;

import java.util.List;

import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.space.SpaceLastNodesF;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceV;
import io.swagger.annotations.ApiOperation;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * ACL 防腐层
 */
@Service
@Slf4j
public class AclSpaceClientService {

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;


    /**
     * 远洋专用
     * @param spaceLastNodesF
     * @return
     */
    @ApiOperation(value = "根据父节点pid获取末级节点", notes = "根据父节点pid获取末级节点")
    public List<SpaceV> getLastSpaceNodesByPids(SpaceLastNodesF spaceLastNodesF){
        return spaceClient.getLastSpaceNodesByPids(spaceLastNodesF);
    }

    /**
     * 通过项目id获取拥有该项目权限的用户
     * @param communityId
     * @return
     */
    public List<UserInfoRawV> getPerUserByCommunityId(String communityId){
        return spaceClient.getPerUserByCommunityId(communityId);
    }

}
