package com.wishare.finance.apis.yuanyang;

import com.wishare.finance.apps.model.yuanyang.fo.YyNccCustomerRelAddF;
import com.wishare.finance.apps.model.yuanyang.fo.YyNccCustomerRelUpdateF;
import com.wishare.finance.apps.model.yuanyang.vo.YyNccCustomerRelV;
import com.wishare.finance.apps.service.yuanyang.YyNccCustomerRelAppService;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Api(tags = {"远洋ncc"})
@Validated
@RestController
@RequestMapping("/yy/nccCustomerRel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YyNccCustomerRelApi {

    private final YyNccCustomerRelAppService yyNccCustomerRelAppService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取远洋主数据与ncc客商信息", notes = "分页获取远洋主数据与ncc客商信息")
    public PageV<YyNccCustomerRelV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return yyNccCustomerRelAppService.selectPageBySearch(queryF);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增NCC", notes = "新增NCC")
    public Long add(@Validated @RequestBody YyNccCustomerRelAddF addF) {
        return yyNccCustomerRelAppService.addYyNccCustomerRel(addF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新NCC", notes = "更新NCC")
    public Long update(@Validated @RequestBody YyNccCustomerRelUpdateF updateF) {
        return yyNccCustomerRelAppService.updateYyNccCustomerRel(updateF);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除NCC", notes = "删除NCC")
    public void delete(@NotEmpty(message = "id不能为空") @Validated @RequestBody List<Long> ids) {
        yyNccCustomerRelAppService.deleteByIds(ids);
    }

    @GetMapping("/get")
    @ApiOperation(value = "根据ID获取远洋主数据与ncc客商", notes = "根据ID获取远洋主数据与ncc客商")
    public YyNccCustomerRelE getById(@Validated @RequestParam Long id){
        return yyNccCustomerRelAppService.getById(id);
    }
}
