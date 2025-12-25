package com.wishare.finance.apis.third;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.third.BillInfoResponse;
import com.wishare.finance.apps.model.third.QueryBillReq;
import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.starter.beans.PageV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账单api
 *
 * @author yancao
 */
@Api(tags = {"三方查询账单API"})
@Validated
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/third")
@Slf4j
public class ThirdBillApi {

    private final ReceivableBillAppService receivableBillAppService;


    @PostMapping("/queryBillInfo")
    @ApiOperation(value = "根据账单id列表查询账单详情", notes = "根据账单id列表查询账单详情")
    PageV<BillInfoResponse> queryBillInfo(@RequestBody @Validated QueryBillReq queryBillReq) {
        log.info("根据账单id列表查询账单详情,{}", JSON.toJSONString(queryBillReq));
        return receivableBillAppService.queryBillInfo(queryBillReq);
    }
}
