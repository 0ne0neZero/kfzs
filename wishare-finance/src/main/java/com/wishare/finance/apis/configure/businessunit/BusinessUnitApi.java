package com.wishare.finance.apis.configure.businessunit;


import com.wishare.finance.apps.model.configure.businessunit.fo.AddBusinessDetailUnitF;
import com.wishare.finance.apps.model.configure.businessunit.fo.CreateBusinessUnitF;
import com.wishare.finance.apps.model.configure.businessunit.fo.QueryBusinessUnitF;
import com.wishare.finance.apps.model.configure.businessunit.fo.UpdateBusinessUnitF;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitAccountV;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitListV;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitStatutoryV;
import com.wishare.finance.apps.model.configure.businessunit.vo.BusinessUnitV;
import com.wishare.finance.apps.model.fangyuan.vo.BusinessUnitSyncDto;
import com.wishare.finance.apps.service.configure.businessunit.BusinessUnitAppService;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 业务单元控制器
 *
 * @author yangzhi
 */
@Api(tags = {"业务单元接口"})
@RestController
@RequestMapping("/businessUnit")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessUnitApi {

    private final BusinessUnitAppService businessUnitAppService;

    @ApiOperation("分页查询业务单元树")
    @PostMapping("/query/tree/page")
    PageV<BusinessUnitV> queryByTreePage(@RequestBody PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        return businessUnitAppService.queryByTreePage(queryBusinessUnitPageF);
    }

    @ApiOperation(value = "启用业务单元", notes = "启用业务单元")
    @PostMapping("/enable/id/{id}")
    public Boolean enable(@PathVariable("id") Long id) {
        return businessUnitAppService.enable(id);
    }

    @ApiOperation(value = "禁用业务单元", notes = "禁用业务单元")
    @PostMapping("/disable/id/{id}")
    public Boolean disable(@PathVariable("id") Long id) {
        return businessUnitAppService.disable(id);
    }

    @ApiOperation(value = "删除业务单元", notes = "删除业务单元")
    @DeleteMapping("/delete/id/{id}")
    public Boolean delete(@PathVariable("id") Long id) {
        return businessUnitAppService.delete(id);
    }

    @ApiOperation("查询业务单元树")
    @PostMapping("/query/tree")
    List<BusinessUnitV> queryTree(@RequestBody SearchF<BusinessUnitE> queryBusinessUnitPageF) {
        return businessUnitAppService.queryByTree(queryBusinessUnitPageF);
    }

    @ApiOperation("分页查询业务单元list")
    @PostMapping("/query/list/page")
    PageV<BusinessUnitListV> queryByListPage(@RequestBody PageF<SearchF<BusinessUnitE>> queryBusinessUnitPageF) {
        return businessUnitAppService.queryByListPage(queryBusinessUnitPageF);
    }

    @ApiOperation("查询业务单元list")
    @PostMapping("/query/list")
    List<BusinessUnitListV> queryByList(@RequestBody SearchF<BusinessUnitE> queryBusinessUnitPageF) {
        return businessUnitAppService.queryByList(queryBusinessUnitPageF);
    }

    @ApiOperation("查询业务单元详情")
    @PostMapping("/query/detail/id")
    BusinessUnitListV queryByDetailId(Long id) {
        return businessUnitAppService.queryByDetailId(id);
    }

    @ApiOperation("根据ID查询业务单元")
    @PostMapping("/getById")
    BusinessUnitV getBusinessById(@RequestParam Long id) {
        return businessUnitAppService.getById(id);
    }

    @ApiOperation("根据名称查询业务单元")
    @PostMapping("/getByName")
    BusinessUnitV getBusinessByName(@RequestParam String name) {
        return businessUnitAppService.getByName(name);
    }

    @ApiOperation(value = "修改业务单元", notes = "修改业务单元")
    @PostMapping("/update")
    public Boolean update(@RequestBody UpdateBusinessUnitF updateBusinessUnitF) {
        return businessUnitAppService.update(updateBusinessUnitF);
    }

    @ApiOperation(value = "新加业务单元", notes = "新加业务单元")
    @PostMapping("/create")
    public BusinessUnitSyncDto create(@RequestBody CreateBusinessUnitF createBusinessUnitF) {
        return businessUnitAppService.syncBusinessDataAddOrUpdate(createBusinessUnitF);
    }
    @ApiOperation(value = "数据刷新", notes = "数据刷新")
    @PostMapping("/push")
    public List<BusinessUnitSyncDto> push(String code, String name) {
        return businessUnitAppService.push(code,name);
    }
    @ApiOperation(value = "条件查询", notes = "条件查询")
    @PostMapping("/queryBusinessUnitList")
    public List<BusinessUnitListV> queryBusinessUnitList(@RequestBody QueryBusinessUnitF queryBusinessUnitF) {
        return businessUnitAppService.queryBusinessUnitList(queryBusinessUnitF);
    }
    @ApiOperation(value = "业务单元详情新增", notes = "业务单元详情新增")
    @PostMapping("/detailCreate")
    public Boolean detailCreate(@RequestBody AddBusinessDetailUnitF addBusinessDetailUnitF) {
        return businessUnitAppService.detailCreate(addBusinessDetailUnitF);
    }


    @ApiOperation(value = "根据法定单位id查询业务单元银行信息",notes = "根据法定单位id查询业务单元银行信息")
    @GetMapping("/queryBusinessUnitWithAccountByStatutoryBodysId")
    public List<BusinessUnitAccountV> queryBusinessUnitWithAccountByStatutoryBodysId( @RequestParam("id") Long id){
        return businessUnitAppService.queryBusinessUnitWithAccountByStatutoryBodysIdRemake(id);
    }

    @ApiOperation(value = "Old根据法定单位id查询业务单元银行信息",notes = "Old根据法定单位id查询业务单元银行信息")
    @GetMapping("/queryBusinessUnitWithAccountByStatutoryBodysIdOld")
    public List<BusinessUnitAccountV> queryBusinessUnitWithAccountByStatutoryBodysIdOld( Long id){
        return businessUnitAppService.queryBusinessUnitWithAccountByStatutoryBodysId(id);
    }

    @ApiOperation(value = "根据法定单位id查询业务单元以及银行信息",notes = "根据法定单位id查询业务单元以及银行信息")
    @PostMapping("/queryBusinessUnitWithByStatutoryBodysId")
    public List<BusinessUnitStatutoryV> queryBusinessUnitWithByStatutoryBodysId(@RequestParam(required = false) Long id){
        return businessUnitAppService.queryBusinessUnitWithByStatutoryBodysId(id);
    }
}
