package com.wishare.finance.apis.bill;

import java.util.List;

import com.wishare.finance.apps.model.bill.fo.BillingCostCenterAdjustCommunityF;
import com.wishare.finance.apps.model.bill.fo.BillingCostCenterAdjustF;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.service.bill.BillAdjustAppService;
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

@Api(tags = {"账单调整"})
@Validated
@RestController
@RequestMapping("/adjust")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAdjustApi {

    private final BillAdjustAppService billAdjustAppService;


    @PostMapping("/page")
    @ApiOperation(value = "分页获取调整记录", notes = "分页获取调整记录")
    public PageV<BillAdjustV> getBillAdjustPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return billAdjustAppService.getBillAdjustPage(queryF);
    }

    @GetMapping("/list/billid")
    @ApiOperation(value = "根据账单id获取调整记录", notes = "根据账单id获取调整记录")
    public List<BillAdjustV> listByBillId(@RequestParam("billId") Long billId) {
        return billAdjustAppService.listByBillId(billId);
    }




    /**
     * 远洋 到期区
     * @param billingCostCenterAdjustF
     * @return
     */
    @PostMapping("/orgInfo")
    @ApiOperation(value = "账单成本中心调整(期区层)", notes = "账单成本中心调整")
    public Boolean orgInfo(@Validated @RequestBody BillingCostCenterAdjustF billingCostCenterAdjustF) {
        return billAdjustAppService.orgInfo(billingCostCenterAdjustF);
    }

    /**
     * 中交的层次 项目
     * @param billingCostCenterAdjustCommunityF 入参对象
     * @return true:获取成本中心正常
     */
    @PostMapping("/orgInfoCommunity")
    @ApiOperation(value = "账单成本中心调整(项目层)", notes = "账单成本中心调整")
    public Boolean orgInfoCommunity(@Validated @RequestBody BillingCostCenterAdjustCommunityF billingCostCenterAdjustCommunityF) {
        return billAdjustAppService.orgInfoCommunity(billingCostCenterAdjustCommunityF);
    }
}

