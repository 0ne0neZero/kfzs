package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.remote.fo.BillApplyBatchRf;
import com.wishare.contract.apps.remote.fo.BillApplyRf;
import com.wishare.contract.apps.remote.fo.StatisticsBillTotalRf;
import com.wishare.contract.apps.remote.vo.BillPageInfoRv;
import com.wishare.contract.apps.remote.vo.BillTotalRv;
import com.wishare.contract.apps.service.contractset.FinanceBillAppService;
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

@Api(tags = {"收入单成本单"})
@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FinanceBillApi {

    private final FinanceBillAppService financeBillAppService;

    @ApiOperation(value = "收入单/成本单分页查询", notes = "收入收入单/成本单分页查询")
    @PostMapping("/page")
    public PageV<BillPageInfoRv> getBillPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return financeBillAppService.getBillPage(queryF);
    }

    @ApiOperation(value = "发起审核申请", notes = "发起审核申请")
    @PostMapping("/apply")
    public Boolean billApply(@Validated @RequestBody BillApplyRf from) {
        return financeBillAppService.billApply(from);
    }


    @ApiOperation(value = "批量发起审核申请", notes = "批量发起审核申请")
    @PostMapping("/apply/batch")
    public Boolean billApplyBatch(@Validated @RequestBody BillApplyBatchRf from) {
        return financeBillAppService.billApplyBatch(from);
    }

    @ApiOperation(value = "统计账单金额总数", notes = "统计账单金额总数")
    @PostMapping("/statistics/bills")
    public BillTotalRv statisticsBills(@Validated @RequestBody StatisticsBillTotalRf from) {
        return financeBillAppService.statisticsBills(from);
    }
}
