package com.wishare.finance.apis.pushbill;


import com.wishare.finance.apps.pushbill.vo.VoucherBillDetailMoneyV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillDetailV;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.domains.voucher.entity.VoucherBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
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

import java.util.List;

@Api(tags = {"推单明细"})
@RestController
@RequestMapping("/pushbill")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillDetailApi {

    private final VoucherBillDetailRepository voucherBillDetailRepository;

    @PostMapping("/page")
    @ApiOperation(value = "获取推单(分页)", notes = "获取推单(分页)")
    public PageV<VoucherBillDetailV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return RepositoryUtil.convertMoneyPage(voucherBillDetailRepository.pageBySearch(form), VoucherBillDetailV.class);
    }

    @PostMapping("/queryMoney")
    @ApiOperation(value = "获取推单明细金额", notes = "获取推单明细金额")
    public VoucherBillDetailMoneyV queryMoney(@Validated @RequestBody PageF<SearchF<?>> form) {
        return voucherBillDetailRepository.queryMoney(form);
    }

    @PostMapping("/getPushDetails")
    @ApiOperation(value = "根据账单id获取推单明细", notes = "根据账单id获取推单明细")
    public List<VoucherPushBillDetail> getPushDetails(@Validated @RequestBody List<Long> ids) {
        return voucherBillDetailRepository.getPushDetails(ids);
    }
}
