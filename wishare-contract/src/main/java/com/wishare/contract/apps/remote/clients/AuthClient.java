package com.wishare.contract.apps.remote.clients;

import com.wishare.contract.apps.remote.vo.TreeMenuV;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/11/4 0:05
 */
@OpenFeignClient(name = "wishare-auth", serverName = "鉴权中心", path = "/auth")
public interface AuthClient {

    @ApiOperation(value = "查询菜单树(关联角色权限)", notes = "查询菜单树(关联角色权限)")
    @GetMapping("/menu/tree")
    List<TreeMenuV> getTree(@RequestParam Boolean showAll);

    @GetMapping(value = "/roleMenus/treeByMenuId", name = "获取当前账号菜单树")
    List<TreeMenuV> treeByMenuId(@RequestParam List<Long> pidList);
}
