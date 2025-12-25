package com.wishare.finance.domains.configure.organization.facade;

import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.PermissionRV;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/30
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CostCenterFacade {

    private final OrgClient orgClient;


    /**
     * 对前端传的成本中心id查询进行转换
     * 如果有权限看到这个成本中心的子节点，那么都加进来转换成in条件sql查询
     * @param conditions 前端查询条件
     * @param costCenterIdName 前端传的成本中心id 字段名
     * @return 如果没有查询到有权限的成本中心节点，返回false。
     */
    public Boolean changeNodeIdSearch(SearchF<?> conditions, String costCenterIdName) {
        List<Field> fields = conditions.getFields();
        for (Field field : fields) {
            if (field.getName().equals(costCenterIdName)) {
                String userId = ApiData.API.getUserId().orElse("administrator");
                Long nodeId =null;
                if (field.getValue() instanceof Integer){
                    nodeId = ((Integer) field.getValue()).longValue();
                }else {
                    nodeId = (Long) field.getValue();
                }
                PermissionRV permission = orgClient.permission(2, userId, nodeId);
                if (permission == null) {
                    log.info("流水认领查询：该用户查询权限返回为空，userId:{}, nodeId:{}", userId, nodeId);
                    return false;
                }
                if (permission.getType() == 0) {
                    log.info("permission查询：该账户没有任何权限，userId:{}", userId);
                    return false;
                }
                List<Long> allNodes = permission.getAllNodes();
                if (allNodes != null && allNodes.size() > 1) {
                    field.setMethod(15);
                    field.setValue(allNodes);
                } else if (allNodes != null && allNodes.size() == 1) {
                    field.setValue(allNodes.get(0));
                } else {
                    log.info("流水认领查询：该用户查询权限返回列表无数据，userId:{}, nodeId:{}", userId, nodeId);
                    return false;
                }
                log.info("流水认领查询：该用户查询权限返回数据:{}", allNodes.stream().map(Objects::toString).collect(Collectors.joining(",")));
            }
        }
        return true;
    }

    public Boolean changeNodeIdSearchByStatutoryBodyAccountId(SearchF<?> conditions, String costCenterIdName) {
        List<Field> fields = conditions.getFields();
        for (Field field : fields) {
            if (field.getName().equals(costCenterIdName)) {
                String userId = ApiData.API.getUserId().orElse("administrator");
                // Long nodeId = (Long) field.getValue();
                Long nodeId =  Long.valueOf(field.getValue().toString());
                PermissionRV permission = orgClient.permission(1, userId, nodeId);
                if (permission == null) {
                    log.info("流水认领查询：该用户查询权限返回为空，userId:{}, nodeId:{}", userId, nodeId);
                    return false;
                }
                if (permission.getType() == 0) {
                    log.info("permission查询：该账户没有任何权限，userId:{}", userId);
                    return false;
                }
                List<Long> allNodes = permission.getAllNodes();
                if (allNodes != null && allNodes.size() > 1) {
                    field.setMethod(15);
                    field.setValue(allNodes);
                } else if (allNodes != null && allNodes.size() == 1) {
                    field.setValue(allNodes.get(0));
                } else {
                    log.info("流水认领查询：该用户查询权限返回列表无数据，userId:{}, nodeId:{}", userId, nodeId);
                    return false;
                }
                log.info("流水认领查询：该用户查询权限返回数据:{}", allNodes.stream().map(Objects::toString).collect(Collectors.joining(",")));
            }
        }
        return true;
    }


    public Boolean changeNodeIdSearchNew(SearchF<?> conditions, String costCenterIdName, Integer type) {
        List<Field> fields = conditions.getFields();
        for (Field field : fields) {
            if (field.getName().equals(costCenterIdName)) {
                String userId = ApiData.API.getUserId().orElse("administrator");
                // Long nodeId = (Long) field.getValue();
                List<String> nodeIds = (List<String>) field.getValue();
                List<Long> nodes = nodeIds.stream()
                        .map(f->Long.parseLong(f))
                        .collect(Collectors.toList());
                List<Long> allNodes = new ArrayList<>();
                nodes.stream().forEach(nodeId->{
                    PermissionRV permission = orgClient.permission(type, userId, nodeId);
                    if (permission != null && permission.getType() != 0) {
                        allNodes.addAll(permission.getAllNodes());
                    }
                });

                if (allNodes != null && allNodes.size() > 0) {
                    field.setMethod(15);
                    field.setValue(allNodes);
                }else {
                    log.info("流水认领查询：该用户查询权限返回列表无数据，userId:{}, type:{}， nodeId:{}", userId, type, allNodes);
                    return false;
                }
                log.info("流水认领查询：该用户查询权限返回数据:{}", allNodes.stream().map(Objects::toString).collect(Collectors.joining(",")));
            }
        }
        return true;
    }

}
