package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.service.bill.GatherAndPayAppService;
import com.wishare.finance.domains.bill.dto.GatherAndPayDto;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Api(tags = {"收付款记录"})
@Validated
@RestController
@RequestMapping("/gatherAndPay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherAndPayApi {

    private final GatherAndPayAppService gatherAndPayAppService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询收付款记录(明细维度)", notes = "分页查询收付款记录")
    public PageV<GatherAndPayDto> queryPage(@RequestBody PageF<SearchF<?>> form) {
        return gatherAndPayAppService.queryPage(form);
    }

    @PostMapping("/bill/queryPage")
    @ApiOperation(value = "分页查询收付款记录(收付款单维度)", notes = "分页查询收付款记录(收付款单维度)")
    public PageV<GatherAndPayDto> billQueryPage(@RequestBody PageF<SearchF<?>> form) {
        return gatherAndPayAppService.billQueryPage(form);
    }

    @PostMapping("/ignoreTenant/bill/queryPage")
    @ApiOperation(value = "分页查询收付款记录(收付款单维度无租户隔离)", notes = "分页查询收付款记录(收付款单维度无租户隔离)")
    public PageV<GatherAndPayDto> billQueryPageIgnoreTenant(@RequestBody PageF<SearchF<?>> form) {
        return gatherAndPayAppService.billQueryPageIgnoreTenant(form);
    }

    @PostMapping("/statistics")
    @ApiOperation(value = "统计收付款记录", notes = "统计收付款记录")
    public GatherAndPayStatisticsDto statistics(@RequestBody SearchF<?> form) {
        return gatherAndPayAppService.statistics(form);
    }

    @PostMapping("/ignoreTenant/statistics")
    @ApiOperation(value = "统计收付款记录(无租户隔离)", notes = "统计收付款记录(无租户隔离)")
    public GatherAndPayStatisticsDto statisticsIgnoreTenant(@RequestBody SearchF<?> form) {
        return gatherAndPayAppService.statisticsIgnoreTenant(form);
    }
}
