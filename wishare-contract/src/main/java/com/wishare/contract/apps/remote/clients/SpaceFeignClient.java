package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.fo.remind.CommunityIdF;
import com.wishare.contract.apps.remote.vo.SpaceCommunityRv;
import com.wishare.contract.apps.remote.vo.SpaceCommunityUserV;
import com.wishare.contract.domains.vo.revision.remind.CommunityOrgV;
import com.wishare.starter.annotations.OpenFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @Author wangrui
 * 空间中心调用
 * @Date 2022/05/24
 */
@OpenFeignClient(name = "wishare-space", serverName = "空间中心", path = "/space")
public interface SpaceFeignClient {

    /**
     * 获取项目详细信息
     *
     * @return
     */
    @GetMapping(value = "/community/getDetail", name = "获取项目详细信息")
    SpaceCommunityRv getById(@RequestParam("id")String id);

    /**
     * 通过项目id批量查询组织id
     *
     * @param communityIdF
     * @return
     */
    @PostMapping("/community/getOrgIdsByCommunityIds")
    List<CommunityOrgV> getOrgIdsByCommunityIds(@RequestBody CommunityIdF communityIdF);

    //根据项目ID获取该项目中“项目经理”人员信息
    @PostMapping("/community/getUserIdByCommunityIdList")
    List<SpaceCommunityUserV> getUserIdByCommunityIdList(List<String> communityIdList);
}
