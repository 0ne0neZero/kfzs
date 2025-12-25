package com.wishare.finance.apis.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxItemGoodsF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxItemGoodsF;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemGoodsAppService;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemGoodsE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 税收商品管理
 *
 * @author yancao.
 * taxitemgoods
 */
@Api(tags = {"税收商品管理"})
@RestController
@RequestMapping("/taxItemGoods")
@RequiredArgsConstructor
@Validated
public class TaxItemGoodsApi {

    private final TaxItemGoodsAppService taxItemAppService;

    @ApiOperation(value = "新增税收商品", notes = "新增税收商品")
    @PostMapping("/add")
    public Long add(@RequestBody AddTaxItemGoodsF addTaxItemGoodsF) {
        return taxItemAppService.add(addTaxItemGoodsF);
    }

    @ApiOperation(value = "更新税收商品", notes = "更新税收商品")
    @PostMapping("/update")
    public Boolean update(@RequestBody UpdateTaxItemGoodsF updateTaxItemF) {
        return taxItemAppService.update(updateTaxItemF);
    }

    @ApiOperation("分页查询税收商品")
    @PostMapping("/query/page")
    PageV<TaxItemGoodsD> queryByPage(@RequestBody PageF<SearchF<TaxItemGoodsE>> queryF) {
        return taxItemAppService.queryByPage(queryF);
    }

    @ApiOperation("根据id获取税收商品")
    @GetMapping("/query/id/{id}")
    public TaxItemGoodsD queryById(@PathVariable(value = "id") Long id) {
        return taxItemAppService.queryById(id);
    }

    @ApiOperation(value = "根据id删除税收商品", notes = "根据id删除税收商品")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return taxItemAppService.delete(id);
    }

}
