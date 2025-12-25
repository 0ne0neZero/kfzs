package com.wishare.contract.apps.remote.clients;


import com.wishare.contract.apps.remote.vo.config.AreaInfoListV;
import com.wishare.contract.apps.remote.vo.config.CfgExternalDeportData;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.remote.vo.config.DictionaryDetailRv;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 *
 * @author: fufengwen
 * @create: 2022-09-28
 * @description:
 **/
@OpenFeignClient(contextId = "wishare-config-old",name = "${wishare.feignClients.cfg.name:wishare-config}", serverName = "配置中心", path = "${wishare.feignClients.org.path:/config}")
public interface ConfigFeignClient {

    @GetMapping("/dictionary/getById")
    @ApiOperation(value = "获取数据项详情", notes = "获取数据项详情")
    DictionaryDetailRv getById(@RequestParam("id")String id);

    @GetMapping("/dictionary/getKeyAndValue")
    @ApiOperation(value = "查询数据项对应值", notes = "查询数据项对应值")
    List<DictionaryCode> getKeyAndValue(@RequestParam("dictionaryCode") @Value("UNIT") String dictionaryCode, @RequestParam("name")String name);

    @GetMapping("/dictionary/getKeyAndValue")
    @ApiOperation(value = "查询数据项对应值", notes = "查询数据项对应值")
    List<DictionaryCode> getKeyAndValueByCode(@RequestParam("dictionaryCode") @Value("UNIT") String dictionaryCode, @RequestParam("code")String name);

    @ApiOperation(value = "根据parentId查询归属省市区", notes = "根据parentId查询归属省市区")
    @GetMapping("/area/getByAdCode")
    AreaInfoListV getAreaByAdCode(@RequestParam(value = "adCode") String adCode);

    @ApiOperation(value = "根据name查询省", notes = "根据name查询省")
    @GetMapping("/area/getProvinceByName")
    AreaInfoListV getProvinceAreaByName(@RequestParam(value = "name") String name);

    @ApiOperation(value = "根据项目ID集合获取对应部门List", notes = "根据项目ID集合获取对应部门List")
    @PostMapping("/external/data/getDeportList")
    List<CfgExternalDeportData> getDeportList(@RequestBody List<String> communityIdList);

    @ApiOperation(value = "根据部门编码获取部门名称", notes = "根据部门编码获取部门名称")
    @GetMapping("external/data/getDeportNameByCode")
    String getDeportNameByCode(@RequestParam(value = "dataCode") String dataCode);
}
