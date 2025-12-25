package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.infrastructure.remote.fo.config.DictionaryItemF;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import com.wishare.finance.infrastructure.remote.vo.cfg.DictionaryValueV;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.config.DictionaryItemRV;
import com.wishare.starter.annotations.OpenFeignClient;
import java.util.List;

import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author pgq
 * @date 2022/12/31
 * @Description:
 */
@OpenFeignClient(name = "wishare-config", serverName = "数据字典", path = "/config")
public interface ConfigClient {

    @PostMapping(value = "/dictionary/list",name = "获取数据项列表")
    List<DictionaryItemRV> listDictionary(@Validated @RequestBody DictionaryItemF dictionaryItemF);
    @ApiOperation(value = "通过数据编号获取映射外部数据", notes = "通过数据编号获取映射外部数据")
    @GetMapping("/external/data/getExternalMapByCode")
     List<CfgExternalDataV> getExternalMapByCode(@RequestParam("dictionaryItemCode") String dictionaryItemCode,
                                                 @RequestParam("dataCode") String dataCode);

    @ApiOperation(value = "查询数据项对应值", notes = "查询数据项对应值")
    @GetMapping("/dictionary/getKeyAndValue")
    List<DictionaryValueV> getKeyAndValue(@RequestParam(value = "dictionaryCode") String dictionaryCode,
                                          @RequestParam(value = "code", required = false) String code,
                                          @RequestParam(value = "name", required = false) String name);

    @ApiOperation(value = "根据项目ID集合获取对应部门List", notes = "根据项目ID集合获取对应部门List")
    @PostMapping("/external/data/getDeportList")
    List<CfgExternalDeportData> getDeportList(@RequestBody List<String> communityIdList);

    @ApiOperation(value = "根据部门编码获取部门名称", notes = "根据部门编码获取部门名称")
    @GetMapping("external/data/getDeportNameByCode")
    String getDeportNameByCode(@RequestParam(value = "dataCode") String dataCode);
}
