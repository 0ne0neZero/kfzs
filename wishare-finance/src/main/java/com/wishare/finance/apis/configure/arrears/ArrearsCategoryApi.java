package com.wishare.finance.apis.configure.arrears;

import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.UpdateArrearsCategoryF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryLastLevelV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsCategoryV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.SearchV;
import com.wishare.finance.apps.service.configure.arrears.ArrearsCategoryAppService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = {"欠费类别配置接口"})
@RestController
@RequestMapping("/arrearsCategory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsCategoryApi {

    private final ArrearsCategoryAppService arrearsCategoryAppService;


    @ApiOperation(value = "新增欠费类别", notes = "新增欠费类别")
    @PostMapping("/create")
    public Long create(@RequestBody @Validated CreateArrearsCategoryF createArrearsCategoryF) {
        return arrearsCategoryAppService.create(createArrearsCategoryF);
    }

    @ApiOperation(value = "更新欠费类别", notes = "更新欠费类别")
    @PostMapping("/update")
    public Boolean update(@RequestBody @Validated UpdateArrearsCategoryF updateArrearsCategoryF) {
        return arrearsCategoryAppService.update(updateArrearsCategoryF);
    }

    @ApiOperation(value = "启用/禁用欠费类别", notes = "启用/禁用欠费类别")
    @GetMapping("/updateStatus")
    public Boolean updateStatus(@RequestParam("id") @ApiParam("欠费类型id") @NotNull(message = "欠费类型id不能为空") Long id,
                          @RequestParam("status") @ApiParam("启用/禁用状态") @NotNull(message = "状态不能为空") int status) {
        return arrearsCategoryAppService.updateStatus(id,status);
    }

    @ApiOperation(value = "删除欠费类别", notes = "删除欠费类别")
    @PostMapping("/deleteBatch")
    public Boolean deleteBatch(@RequestBody List<Long> ids) {
        return arrearsCategoryAppService.delete(ids);
    }

    @ApiOperation("分页查询所有欠费类别")
    @PostMapping("/query/page")
    public PageV<ArrearsCategoryV> queryAllByPage(@RequestBody PageF<SearchF<?>> searchF) {
        return arrearsCategoryAppService.queryAllByPage(searchF);
    }

    @ApiOperation("查询所有启用的欠费类别(树形返回)")
    @GetMapping("/queryAllLastLevel")
    public List<ArrearsCategoryLastLevelV> queryAllLastLevel() {
        return arrearsCategoryAppService.queryAllLastLevel();
    }

    @ApiOperation("查询所有非末级欠费类别(可新增子级欠费类型的)")
    @GetMapping("/queryAllNotLastLevel")
    public List<ArrearsCategoryV> queryAllNotLastLevel() {
        return arrearsCategoryAppService.queryAllNotLastLevel();
    }

    @ApiOperation(value = "根据ID查欠费类别", notes = "根据ID查欠费类别")
    @GetMapping("/query")
    public ArrearsCategoryV getById(@RequestParam("id") Long id) {
        return arrearsCategoryAppService.getById(id);
    }


    @ApiOperation(value = "搜索单选框", notes = "搜索单选框")
    @GetMapping("/search-component/select")
    public List<SearchV> searchComponent(@RequestParam("searchKey") String searchKey, @RequestParam("FEKey") String FEKey) {
        return arrearsCategoryAppService.searchComponent(searchKey);
    }


}
