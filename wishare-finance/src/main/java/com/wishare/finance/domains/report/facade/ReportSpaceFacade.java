package com.wishare.finance.domains.report.facade;

import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.SpacePermissionF;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityDetailRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xujian
 * @date 2023/1/12
 * @Description:
 */
@Service
@Slf4j
public class ReportSpaceFacade {

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;

    /**
     * 批量获取空间详细信息
     *
     * @param roomIdList 房号id集合
     * @return List
     */
    public List<SpaceDetails> getDetails(List<Long> roomIdList){
        return spaceClient.getDetails(roomIdList);
    }

    /**
     * 获取用户拥有的房号权限
     *
     * @param spacePermissionF 入参
     * @return List
     */
    public List<String> getRoomIdList(SpacePermissionF spacePermissionF){
        List<String> roomIds = new ArrayList<>();
        Map<String, Map<String, List<String>>> spacePermission = spaceClient.getSpacePermission(spacePermissionF);
        for (String communityId : spacePermission.keySet()) {
            Map<String, List<String>> stringListMap = spacePermission.get(communityId);
            if(Objects.nonNull(stringListMap) && !stringListMap.isEmpty()){
                // 暂时只需要房号，后续可调整
                List<String> list = stringListMap.get("1");
                roomIds.addAll(list);
            }
        }
        return roomIds;
    }

    /**
     * 通过项目id批量查询项目名称
     * @return
     */
    public List<CommunityDetailRv> getCommunityNameByIds(List<String> communityIds){
        return spaceClient.getCommunityNameById(communityIds);
    }

    //type:1应收  2:预收   3：临时
    /*public PageF<SearchF<?>> getSpacePermissionV(PageF<SearchF<?>> queryF, String alias){
        return queryWrapper(queryF,"spaceTreeId","flag",alias);
    }*/

    /*private PageF<SearchF<?>> queryWrapper(PageF<SearchF<?>> f, String queryName, String treeQueryFlag,String alias) {
        Field field = getField(f, treeQueryFlag);
        Field fieldQuery = getField(f, queryName);
        f.getConditions().getFields().remove(field);
        f.getConditions().getFields().remove(fieldQuery);
        Object fieldQueryValue = null;
        SpacePermissionF spacePermissionF = new SpacePermissionF();
        spacePermissionF.setUserId(getUserId().get());
        spacePermissionF.setAllReturn(false);
        spacePermissionF.setSpaceTypeClassify(List.of("1"));
        if (fieldQuery != null) {
            fieldQueryValue = fieldQuery.getValue();
        }
        if (field == null) {
            List<Long> orgIds = getOrgIds().get();
            if(!CollectionUtils.isEmpty(orgIds)){
                List<String> orgIdList = orgIds.stream().map(Object::toString).collect(Collectors.toList());
                spacePermissionF.setOrgIds(orgIdList);
            }else{
                return f;
            }
        }else{
            String flag = field.getValue().toString();
            if("0".equals(flag)){
                spacePermissionF.setOrgIds(List.of(fieldQueryValue.toString()));
            }else{
                spacePermissionF.setSpaceIds(List.of(fieldQueryValue.toString()));
            }
        }




        SpacePermissionV spacePermissionV = new SpacePermissionV();
        List<String> communityIds = new ArrayList<>();
        List<String> spaceIds = new ArrayList<>();
        Map<String, Map<String, List<String>>> spacePermission = spaceFeignClient.getSpacePermission(spacePermissionF);
        log.info("获取原始空间权限出参-------》：{}",spacePermission);
        for (String communityId : spacePermission.keySet()) {
            Map<String, List<String>> stringListMap = spacePermission.get(communityId);
            if(Objects.nonNull(stringListMap) && !stringListMap.isEmpty()){
                // 暂时只需要房号，后续可调整
                List<String> list = stringListMap.get("1");
                if(CollectionUtils.isEmpty(list)){
                    communityIds.add(communityId);
                }else {
                    spaceIds.addAll(list);
                }
            }
        }
        log.info("参数空间权限-------》：{}",spacePermissionV);
        if(CollectionUtils.isEmpty(communityIds) && CollectionUtils.isEmpty(spaceIds)){
            return null;
        }
        if(!CollectionUtils.isEmpty(communityIds)){
            f.getConditions().getFields().add(new Field(alias + ".community_id",communityIds,15));
        }
        if(!CollectionUtils.isEmpty(spaceIds)){
            f.getConditions().getFields().add(new Field(alias + ".room_id",spaceIds,15));
        }

        return f;
    }*/
}
