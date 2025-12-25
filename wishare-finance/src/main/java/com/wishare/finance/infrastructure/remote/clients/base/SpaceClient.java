package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.infrastructure.remote.fo.space.CommunityIdF;
import com.wishare.finance.infrastructure.remote.fo.space.SpaceLastNodesF;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.CommunityRelationRF;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.SpacePermissionF;
import com.wishare.finance.infrastructure.remote.vo.space.*;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 空间
 * @author: pgq
 * @since: 2022/10/20 9:10
 * @version: 1.0.0
 */
@OpenFeignClient(name = "${wishare.feignClients.space.name:wishare-space}", serverName = "空间中心", path = "/space")
public interface SpaceClient {

    @GetMapping(value = "/community/get", name = "获取项目简短信息")
    CommunityShortRV getCommunityInfo(@RequestParam("id") @Validated String id);

    @GetMapping(value = "/community/getDetail", name = "获取项目详细信息")
    SpaceCommunityV getCommunityDetail(@RequestParam("id") String id);

    @PostMapping("/community/getCommunityPJCodes")
    @ApiOperation(value = "获取项目PJ码信息", notes = "获取项目PJ码信息")
    List<CommunityPJCodeV> getCommunityPJCodes(@RequestBody List<String> communityIds);

    @PostMapping(value = "/space/getSpacePermission", name = "获取用户所拥有的空间权限")
    Map<String, Map<String, List<String>>> getSpacePermission(@RequestBody SpacePermissionF spacePermissionF);

    @PostMapping(value = "/community/relation/list", name = "关联关系列表查询")
    List<SpaceCommunityMtmTenantCommunityRV> communityRelationList(@RequestBody CommunityRelationRF form);

    @PostMapping(value = "/space/getDetails",name = "批量获取空间详细信息")
    List<SpaceDetails> getDetails(@RequestBody List<Long> ids);

    @GetMapping(value = "/community/v2/getDetail", name = "获取项目详细信息")
    SpaceCommunityV2V getCommunityDetailV2(@RequestParam("id") String id);


    @GetMapping("/space/getSpaceExternals")
    @ApiOperation(value = "获取空间扩展表external信息", notes = "获取空间扩展表external信息", response = SpaceSpaceExpandExternalV.class)
    List<SpaceSpaceExpandExternalV> getSpaceExternals(@RequestParam("spaceIds") List<Long> spaceIds);

    @PostMapping(value = "/community/getCommunityNameById",name = " 通过项目id批量查询项目名称 ")
    List<CommunityDetailRv> getCommunityNameById(List<String> communityIds);

    @GetMapping(value = "/asset/v2/getBySpaceId/{spaceId}",name = " 通过项目id批量查询项目名称 ")
    List<SAssetV> getBySpaceId(@PathVariable("spaceId") @Validated Long spaceId);

    @GetMapping(value = "/community/getCommunityByTenantId", name = "通过tenantId获取项目集合")
    List<CommunityShortRV> getCommunityByTenantId(@RequestParam("tenantId") String tenantId);

    @GetMapping(value = "/community/get", name = "根据项目id获取中交的项目id")
    SpaceCommunityShortV get(@RequestParam("id") String id);


    @GetMapping(value =  "/permission/community/{userId}", name = "获取用户拥有的项目权限")
    SPermissionUserV getUserCommunityPermission(@PathVariable("userId") String userId);

    @GetMapping(value = "/community/list/per", name = "获取有权限的项目集合")
    List<SpaceCommunityShortV> perCommunitys();

    @ApiOperation(value = "通过企业档案id获取企业信息（远洋使用）", notes = "通过企业档案id获取企业信息（远洋使用）",
            response = ArchivesEnterpriseDetailV.class)
    @GetMapping("/archivesEnterprise/info/{id}")
    ArchivesEnterpriseDetailV getEnterpriseDetail(@PathVariable("id") Long id);

    @GetMapping(value = "/space/getDetail", name = "获取空间详细信息")
    SpaceV getSpaceInfo(@RequestParam("id") @Validated Long id);

    /**
     * 远洋专用
     * @param spaceLastNodesF
     * @return
     */
    @ApiOperation(value = "根据父节点pid获取末级节点", notes = "根据父节点pid获取末级节点")
    @PostMapping("/space/getLastSpaceNodesByPids")
    List<SpaceV> getLastSpaceNodesByPids(@RequestBody SpaceLastNodesF spaceLastNodesF);


    @GetMapping("/permission/getPerUserByCommunityId")
    @ApiOperation(value = "通过项目id获取拥有该项目权限的用户")
    List<UserInfoRawV> getPerUserByCommunityId(@RequestParam("communityId") String communityId);

    @PostMapping("/community/getOrgIdsByCommunityIds")
    List<CommunityOrgV> getOrgIdsByCommunityIds(@RequestBody CommunityIdF communityIdF);

    @PostMapping("/space/selectRoomIdsBySpaceTypeIds")
    List<String> selectRoomIdsBySpaceTypeIds(@RequestBody SpacePermissionF spacePermissionF);

    @ApiOperation(value = "获取所拥有该项目权限的用户id集合", notes = "获取所拥有该项目权限的用户id集合")
    @GetMapping("/community/getUserPermission")
    Set<String> getPerUserIdsByCommunityId(@RequestParam("communityId") String communityId);
}
