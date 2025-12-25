package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.BillRefundConditionF;
import com.wishare.finance.apps.model.bill.fo.BillRefundF;
import com.wishare.finance.apps.service.bill.BillRefundAppService;
import com.wishare.finance.domains.bill.dto.AdvanceBillRefundDto;
import com.wishare.finance.domains.bill.dto.BillRefundDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillRefundDto;
import com.wishare.finance.domains.bill.dto.TempChargeBillRefundDto;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/6
 * @Description:
 */
@Api(tags = {"账单退款"})
@Validated
@RestController
@RequestMapping("/refund")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillRefundApi {

    private final BillRefundAppService billRefundAppService;


    @PostMapping("/receivable/page")
    @ApiOperation(value = "分页查询账应收账单退款列表", notes = "分页查询账应收账单退款列表")
    public PageV<ReceivableBillRefundDto> queryReceivableRefundPage(@Validated @RequestBody PageF<SearchF<?>> form){
        return billRefundAppService.queryReceivableRefundPage(form);
    }

    @PostMapping("/receivable/list")
    @ApiOperation(value = "查询账应收账单退款列表", notes = "查询账应收账单退款列表")
    public List<ReceivableBillRefundDto> queryReceivableRefundList(@Validated @RequestBody PageF<SearchF<?>> form){
        return billRefundAppService.queryReceivableRefundList(form);
    }

    @PostMapping("/tempCharge/page")
    @ApiOperation(value = "分页查询账临时账单退款列表", notes = "分页查询账临时账单退款列表")
    public PageV<TempChargeBillRefundDto> queryTempChargeRefundPage(@Validated @RequestBody PageF<SearchF<?>> form){
        return billRefundAppService.queryTempChargeRefundPage(form);
    }

    @PostMapping("/tempCharge/list")
    @ApiOperation(value = "查询临时账单退款列表", notes = "查询临时账单退款列表")
    public List<TempChargeBillRefundDto> queryTempChargeRefundList(@Validated @RequestBody PageF<SearchF<?>> form){
        return billRefundAppService.queryTempChargeRefundList(form);
    }

    @PostMapping("/advance/page")
    @ApiOperation(value = "分页查询预收账单退款列表", notes = "分页查询预收账单退款列表")
    public PageV<AdvanceBillRefundDto> queryAdvanceRefundPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return billRefundAppService.queryAdvanceRefundPage(form);
    }

    @GetMapping("/getByBillId")
    @ApiOperation(value = "根据账单id获取退款记录", notes = "根据账单id获取退款记录")
    public List<BillRefundDto> getByBillId(@RequestParam("billId") @ApiParam("账单id") @NotNull(message = "账单id不能为空") Long billId){
        return billRefundAppService.getByBillId(billId);
    }

    @PostMapping("/getByBillIds")
    @ApiOperation(value = "根据账单id获取退款记录", notes = "根据账单id获取退款记录")
    public List<BillRefundDto> getByBillIds(@RequestBody BillRefundConditionF billRefundF){
        return billRefundAppService.getByBillIds(billRefundF);
    }

    @GetMapping("/infer")
    @ApiOperation(value = "根据账单id获取退款记录", notes = "根据账单id获取退款记录")
    public void batchUpdateRefundInferenceState(@RequestBody List<Long> inferRefundIds){
        billRefundAppService.batchUpdateRefundInferenceState(inferRefundIds);
    }

}
