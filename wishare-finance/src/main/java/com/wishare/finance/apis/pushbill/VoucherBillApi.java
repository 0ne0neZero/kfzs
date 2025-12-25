package com.wishare.finance.apis.pushbill;


import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.pushbill.fo.DelPushBillF;
import com.wishare.finance.apps.pushbill.fo.SyncBatchPushBillF;
import com.wishare.finance.apps.pushbill.vo.VoucherBillMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillV;
import com.wishare.finance.apps.service.pushbill.VoucherBillAppService;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBill;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherPushBillRepository;
import com.wishare.finance.infrastructure.remote.vo.fy.FYSendresultV;
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

@Api(tags = {"汇总单据"})
@RestController
@RequestMapping("/voucherbill")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillApi {

    private final VoucherBillAppService voucherBillAppService;
    private final VoucherPushBillRepository voucherPushBillRepository;


    @PostMapping("/page")
    @ApiOperation(value = "获取单据(分页)", notes = "获取单据(分页)")
    public PageV<VoucherBillV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherPushBillRepository.pageBySearch(form), VoucherBillV.class);
    }
    @GetMapping("/getVoucherBill")
    @ApiOperation(value = "获取单据信息", notes = "获取单据信息")
    public VoucherBill getVoucherBill(@Validated @RequestParam  String voucherBillNo) {
        return voucherPushBillRepository.getVoucherBill(voucherBillNo);
    }
    @PostMapping("/getMoney")
    @ApiOperation(value = "获取单据总金额", notes = "获取单据总金额")
    public VoucherBillMoneyV getMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherPushBillRepository.getMoney(form);
    }
    @PostMapping("/infer/batch")
    @ApiOperation(value = "批量同步", notes = "批量同步")
    public Boolean syncBatchVoucher(@RequestBody SyncBatchPushBillF syncBatchPushBillF) {
        return voucherBillAppService.syncBatchPushBill(syncBatchPushBillF);
    }
    @PostMapping("/infer/del")
    @ApiOperation(value = "方圆删单接口", notes = "方圆删单接口")
    public FYSendresultV delVoucherBill(@RequestBody DelPushBillF delF) {
        return voucherBillAppService.delVoucherBill(delF);
    }
}
