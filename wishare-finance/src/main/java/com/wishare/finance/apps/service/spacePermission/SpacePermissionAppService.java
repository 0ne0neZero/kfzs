package com.wishare.finance.apps.service.spacePermission;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.spacePermission.SpacePermissionF;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceV;
import com.wishare.finance.infrastructure.remote.vo.spacePermission.SpacePermissionV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SpacePermissionAppService implements ApiBase {


    @Setter(onMethod_ = {@Autowired})
    private SpaceClient spaceClient;

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    //type:1应收  2:预收   3：临时
    public PageF<SearchF<?>> getSpacePermissionV(PageF<SearchF<?>> queryF, String alias){

        Field field = getField(queryF, "b.sup_cp_unit_id");
        if (Objects.isNull(field) || Objects.isNull(field.getValue()) || "default".equals(field.getValue())){
            return queryF;
        }
        // 没有树的时候不走该逻辑
        Field spaceTreeId = getField(queryF, "spaceTreeId");
        if (Objects.isNull(spaceTreeId)) {
            return queryF;
        }
        return queryWrapper(queryF,"spaceTreeId","flag", alias);
    }

    public PageF<SearchF<?>> getComminutyAndRoomV(PageF<SearchF<?>> queryF, String alias){
        // 没有树的时候不走该逻辑
        Field spaceTreeId = getField(queryF, "spaceTreeId");
        if (Objects.isNull(spaceTreeId)) {
            Field field = getField(queryF, "flag");
            queryF.getConditions().getFields().remove(field);
            return queryF;
        }
        return comminutyAndRoomWrapper(queryF,"spaceTreeId","flag", alias);
    }


    private PageF<SearchF<?>> queryWrapper(PageF<SearchF<?>> f, String queryName, String treeQueryFlag, String alias) {
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
        Map<String, Map<String, List<String>>> spacePermission = spaceClient.getSpacePermission(spacePermissionF);
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
    }

    public Field getField(Object param, String queryName) {
        PageF<SearchF<?>> f = (PageF<SearchF<?>>) param;
        List<Field> fields = f.getConditions().getFields();
        Optional<Field> optionalField = fields.stream().filter(e -> queryName.equals(e.getName())).findAny();
        boolean empty = optionalField.isEmpty();
        if (empty) {
            return null;
        }
        return optionalField.get();
    }

    public static void removeField(PageF<SearchF<?>> param, Field field) {
        if (
                Objects.isNull(param) ||
                        Objects.isNull(param.getConditions()) ||
                        Objects.isNull(field)
        ) {
            return;
        }
        param.getConditions().getFields().remove(field);
    }

    /**
     * 获取房号对应的path字符串
     * @param roomId 房号ID
     * @return
     */
    public String getSpacePath(String roomId){
        if (StringUtils.isNotBlank(roomId)) {
            // 获取空间档案信息
            SpaceV spaceInfo = spaceClient.getSpaceInfo(Long.valueOf(roomId));
            if (Objects.isNull(spaceInfo)){return null;}
            // 获取path列表
            List<String> pathList = spaceInfo.getPath();
            return String.join(",", pathList);
        }
        return null;
    }

    private PageF<SearchF<?>> comminutyAndRoomWrapper(PageF<SearchF<?>> f, String queryName, String treeQueryFlag, String alias) {
        Field field = getField(f, treeQueryFlag);
        Field fieldQuery = getField(f, queryName);
        f.getConditions().getFields().remove(field);
        f.getConditions().getFields().remove(fieldQuery);
        Object fieldQueryValue = null;
        SpacePermissionF spacePermissionF = new SpacePermissionF();
        spacePermissionF.setUserId(getUserId().get());
        spacePermissionF.setAllReturn(false);
        spacePermissionF.setSpaceTypeClassify(List.of("1", "3"));
        spacePermissionF.setSpaceTypeNameFlag(List.of("room"));
        if (fieldQuery != null) {
            fieldQueryValue = fieldQuery.getValue();
        }
        if (field == null) {
            Long orgId = orgClient.getTenantIdByOrgId();
            if(Objects.nonNull(orgId)){
                List<String> orgIdList = Lists.newArrayList();
                orgIdList.add(orgId.toString());
                spacePermissionF.setOrgIds(orgIdList);
            } else {
                return f;
            }
        } else {
            String flag = field.getValue().toString();
            if ("0".equals(flag)) {
                spacePermissionF.setOrgIds(List.of(fieldQueryValue.toString()));
            } else {
                spacePermissionF.setSpaceIds(List.of(fieldQueryValue.toString()));
            }
        }

        List<String> communityIds = new ArrayList<>();
        List<String> spaceIds = new ArrayList<>();
        log.info("空间权限参数-------》spacePermissionF：{}", JSON.toJSONString(spacePermissionF));
        long time = System.currentTimeMillis();
        Map<String, Map<String, List<String>>> spacePermission = spaceClient.getSpacePermission(spacePermissionF);
        log.info("获取空间权限接口时间：{}", System.currentTimeMillis() - time);
        for (String communityId : spacePermission.keySet()) {
            Map<String, List<String>> stringListMap = spacePermission.get(communityId);
            if (Objects.nonNull(stringListMap) && !stringListMap.isEmpty()) {
                for (String spaceTypeClassify : stringListMap.keySet()) {
                    spaceIds.addAll(stringListMap.get(spaceTypeClassify));
                }

                if (CollectionUtils.isEmpty(spaceIds)) {
                    communityIds.add(communityId);
                }
            }
        }

        if (CollectionUtils.isEmpty(communityIds) && CollectionUtils.isEmpty(spaceIds)) {
            return null;
        }
        if (!CollectionUtils.isEmpty(communityIds)) {
            if (StringUtils.isBlank(alias)) {
                f.getConditions().getFields().add(new Field("community_id", communityIds, 15));
            } else {
                f.getConditions().getFields().add(new Field(alias + ".community_id", communityIds, 15));
            }

        }
        if (!CollectionUtils.isEmpty(spaceIds)) {
            if (StringUtils.isBlank(alias)) {
                f.getConditions().getFields().add(new Field("room_id", spaceIds, 15));
            } else {
                f.getConditions().getFields().add(new Field(alias + ".room_id", spaceIds, 15));
            }

        }
        return f;
    }
}

