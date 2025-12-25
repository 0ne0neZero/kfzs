package com.wishare.finance.apis.pushbill;


import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDetailMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillZJDetailV;
import com.wishare.finance.apps.pushbill.vo.dx.*;
import com.wishare.finance.apps.service.pushbill.VoucherBillDxDetailAppService;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailDxZJRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"对下结算单-推单明细中交"})
@RestController
@RequestMapping("/pushbillDxZJ")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDxZJDetailApi {

    private final VoucherBillDetailDxZJRepository voucherBillDetailRepository;
    private final VoucherBillDxDetailAppService voucherBillDxDetailAppService;

    @PostMapping("/page")
    @ApiOperation(value = "获取推单(分页)", notes = "获取推单(分页)")
    public PageV<VoucherBillZJDetailV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherBillDetailRepository.pageBySearch(form), VoucherBillZJDetailV.class);
    }

    @PostMapping("/queryMoney")
    @ApiOperation(value = "获取推单明细金额", notes = "获取推单明细金额")
    public VoucherBillZJDetailMoneyV queryMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryMoneyByGroup(form);
    }

    @GetMapping("/queryDetailsOfPayments")
    @ApiOperation(value = "获取收款明细", notes = "获取收款明细")
    public List<DxPaymentDetails> queryDetailsOfPayments(@RequestParam("voucherBillNo") String voucherBillNo){
        return voucherBillDxDetailAppService.queryDetailsOfPayments(voucherBillNo);
    }

    @GetMapping("/getCostDetails")
    @ApiOperation(value = "成本明细", notes = "成本明细")
    public List<DxCostDetails> getCostDetails(@RequestParam("voucherBillNo") String voucherBillNo) {
        return voucherBillDxDetailAppService.getCostDetails(voucherBillNo);
    }

    @GetMapping("/getInvoiceDetails")
    @ApiOperation(value = "发票明细", notes = "发票明细")
    public List<DxInvoiceDetails> getInvoiceDetails(@RequestParam("voucherBillNo") String voucherBillNo) {
        return voucherBillDxDetailAppService.getInvoiceDetails(voucherBillNo);
    }

    @GetMapping("/getMeasurementDetails")
    @ApiOperation(value = "计量清单", notes = "计量清单")
    public List<DxMeasurementDetail> queryMeasurements(@RequestParam("voucherBillNo") String voucherBillNo){
        return voucherBillDxDetailAppService.getMeasurementDetails(voucherBillNo);
    }


    @PostMapping("/queryCommonIncomeDetail")
    @ApiOperation(value = "收入确认-通用收入确认明细", notes = "支付申请单-支付明细")
    public List<GeneralDetails> queryCommonIncomeDetail(@RequestParam("voucherBillNo") String voucherBillNo) {
        return voucherBillDxDetailAppService.queryCommonIncomeDetail(voucherBillNo);
    }

}
