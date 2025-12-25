package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.vo.BillCarryV;
import com.wishare.finance.apps.service.bill.BillCarryAppService;
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
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryApi
 * @date 2024.05.21  17:04
 * @description:账单结转 api
 */
@Api(tags = {"账单结转"})
@Validated
@RestController
@RequestMapping("/billCarry")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryApi {

    private final BillCarryAppService billCarryAppService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取结转记录", notes = "分页获取结转记录")
    public PageV<BillCarryV> getBillCarryPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billCarryAppService.getBillCarryPage(queryF);
    }

    @PostMapping("/all/page")
    @ApiOperation(value = "分页获取结转记录(包括临时账单)", notes = "分页获取结转记录(包括临时账单)")
    public PageV<BillCarryV> queryCarryoverPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billCarryAppService.queryCarryoverPage(queryF);
    }
}
