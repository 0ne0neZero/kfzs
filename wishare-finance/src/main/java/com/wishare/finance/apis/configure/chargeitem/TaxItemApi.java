package com.wishare.finance.apis.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxItemF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxItemF;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemAppService;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 税目管理
 *
 * @author yancao
 */
@Api(tags = {"税目管理"})
@RestController
@RequestMapping("/taxItem")
@RequiredArgsConstructor
@Validated
public class TaxItemApi {

    private final TaxItemAppService taxItemAppService;

    @ApiOperation(value = "新增税目", notes = "新增税目")
    @PostMapping("/add")
    public Long add(@RequestBody AddTaxItemF addTaxItemF) {
        return taxItemAppService.add(addTaxItemF);
    }

    @ApiOperation(value = "更新税目", notes = "新增税目")
    @PostMapping("/update")
    public Boolean update(@RequestBody UpdateTaxItemF updateTaxItemF) {
        return taxItemAppService.update(updateTaxItemF);
    }

    @ApiOperation(value = "根据费项id查询税目", notes = "根据费项id查询税目")
    @PostMapping("/query/chargeId")
    public List<TaxChargeItemD> queryByChargeIdList(@RequestBody @Valid @Size(max = 1000, min = 1, message = "id集合大小不允许，仅允许区间为[1,1000]") List<Long> chargeItemIdList) {
        return taxItemAppService.queryByChargeIdList(chargeItemIdList);
    }

    @ApiOperation("分页查询税目")
    @PostMapping("/query/page")
    PageV<TaxItemD> queryByPage(@RequestBody PageF<SearchF<TaxItemE>> queryF) {
        return taxItemAppService.queryByPage(queryF);
    }

    @ApiOperation("根据id获取税目")
    @GetMapping("/query/id/{id}")
    public TaxItemD queryById(@PathVariable(value = "id") Long id) {
        return taxItemAppService.queryById(id);
    }

    @ApiOperation(value = "根据id删除税目", notes = "根据id删除税目")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return taxItemAppService.delete(id);
    }

    @ApiOperation(value = "导入税目", notes = "导入税目")
    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    public Boolean importTaxItem(@RequestParam("file") MultipartFile file) {
        return taxItemAppService.importTaxItem(file);
    }

    @ApiOperation(value = "查询税目信息列表", notes = "查询税目信息列表")
    @PostMapping("/queryTaxItemList")
    public List<TaxItemD> queryTaxItemList() {
        return taxItemAppService.queryTaxItemList();
    }
}
