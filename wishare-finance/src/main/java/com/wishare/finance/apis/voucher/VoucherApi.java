package com.wishare.finance.apis.voucher;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.voucher.fo.*;
import com.wishare.finance.apps.model.voucher.vo.BillPostedStatusV;
import com.wishare.finance.apps.model.voucher.vo.StaticVoucherAmountV;
import com.wishare.finance.apps.model.voucher.vo.SyncBatchVoucherResultV;
import com.wishare.finance.apps.model.voucher.vo.VoucherV;
import com.wishare.finance.apps.service.voucher.VoucherAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.repository.VoucherInfoRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * @description: 凭证明细
 * @author: pgq
 * @since: 2022/10/25 9:52
 * @version: 1.0.0
 */
@Api(tags = {"凭证明细"})
@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherApi {

    private final VoucherAppService voucherAppService;

    private final VoucherInfoRepository voucherInfoRepository;


    @PostMapping("/page")
    @ApiOperation(value = "获取推凭明细(分页)", notes = "获取推凭明细(分页)")
    public PageV<VoucherV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertPage(voucherInfoRepository.pageBySearch(form), VoucherV.class);
    }

    @PostMapping("/static")
    @ApiOperation(value = "按条件统计推凭金额", notes = "按条件统计推凭金额")
    public StaticVoucherAmountV staticVoucherAmount(@Validated @RequestBody PageF<SearchF<?>> form) {
        return new StaticVoucherAmountV(voucherInfoRepository.staticVoucherAmount(form));
    }

    @GetMapping("/bill/{billId}")
    @ApiOperation(value = "根据账单id查询凭证", notes = "根据账单id查询凭证")
    public List<VoucherV> listByBillId(@PathVariable("billId") Long billId, @RequestParam("billType") Integer billType) {
        return Global.mapperFacade.mapAsList(voucherInfoRepository.listByBusinessId(billId, BillTypeEnum.valueOfByCode(billType).getCode()), VoucherV.class);
    }

    @PostMapping("/save")
    @ApiOperation(value = "预制凭证", notes = "预制凭证")
    public Long save(@RequestBody PrefabricationVoucherSaveF voucher) {
        return voucherAppService.save(voucher);
    }

    @PostMapping("/infer/batch")
    @ApiOperation(value = "批量同步", notes = "批量同步")
    public SyncBatchVoucherResultV syncBatchVoucher(@RequestBody SyncBatchVoucherF syncBatchVoucherF) {
        return voucherAppService.syncBatchVoucher(syncBatchVoucherF);
    }

    @PostMapping("/cancel/batch")
    @ApiOperation(value = "批量作废", notes = "批量作废")
    public Map<String,Object> cancelBatch(@RequestParam("voucherIds") @Size(min = 1, max = 20, message = "凭证作废条数仅允许1~20条")List<Long> voucherIds) {
        return voucherAppService.cancelBatch(voucherIds);
    }

    @PostMapping("/checkPostedStatus")
    @ApiOperation(value = "查询账单是否推凭", notes = "查询账单是否推凭")
    public BillPostedStatusV checkPostedStatus(@Validated @RequestBody CheckPostedStatusF checkPostedStatusF) {
        return voucherAppService.checkPostedStatus(checkPostedStatusF);
    }

    @PostMapping("/setDetails")
    @ApiOperation(value = "设置凭证明细", notes = "设置凭证明细")
    public Boolean setDetails(@Validated @RequestBody SetDetailsF setDetailsF) {
        return voucherAppService.setDetails(setDetailsF);
    }


    @PostMapping("/updateVoucherDetail")
    @ApiOperation(value = "修改凭证明细", notes = "修改凭证明细")
    public Boolean updateVoucherDetail(@Validated @RequestBody UpdateVoucherDetailF updateVoucherDetailF) {
        return voucherInfoRepository.updateVoucherDetail(updateVoucherDetailF);
    }


}
