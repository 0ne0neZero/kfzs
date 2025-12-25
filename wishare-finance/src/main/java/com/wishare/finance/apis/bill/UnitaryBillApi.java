package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.BillGatherF;
import com.wishare.finance.apps.model.bill.fo.BillPayF;
import com.wishare.finance.apps.model.bill.vo.BillPayDataV;
import com.wishare.finance.domains.bill.command.BillSettleCommand;
import com.wishare.finance.domains.bill.command.BillUnitaryEnterCommand;
import com.wishare.finance.apps.model.bill.fo.DifferenceRefundF;
import com.wishare.finance.apps.model.bill.fo.InvoiceReverseF;
import com.wishare.finance.apps.model.bill.vo.BillTransactionV;
import com.wishare.finance.apps.service.bill.UnitaryBillAppService;
import com.wishare.finance.domains.bill.command.MaxGatherTimeQuery;
import com.wishare.finance.domains.bill.dto.TransactionDto;
import com.wishare.finance.domains.bill.dto.BillUnitaryEnterResultDto;
import com.wishare.finance.domains.bill.repository.UnitaryBillRepository;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.UNIFIED_ACCOUNTING_REPEAT;
import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.UNIFIED_ACCOUNTING;

/**
 * 统一账单接口
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Api(tags = {"统一账单接口"})
@RestController
@RequestMapping("/unitary")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UnitaryBillApi {

    private final UnitaryBillAppService unitaryBillAppService;
    private final UnitaryBillRepository unitaryBillRepository;

    @PostMapping("/refundDifference")
    @ApiOperation(value = "差额退款", notes = "差额退款")
    public Boolean refundDifference(@Validated @RequestBody DifferenceRefundF differenceRefundF){
        return unitaryBillAppService.refundDifference(differenceRefundF);
    }

    @PostMapping("/invoice/reverse")
    @ApiOperation(value = "票据红冲", notes = "单笔账单的票据进行红冲")
    public Boolean reverseInvoice(@Validated @RequestBody InvoiceReverseF invoiceReverseF){
        return unitaryBillAppService.reverseInvoice(invoiceReverseF);
    }

    @PostMapping("/pay")
    @ApiOperation(value = "统一付款")
    public BillTransactionV pay(@Validated @RequestBody BillPayF billPayF) {
        return unitaryBillAppService.pay(billPayF);
    }


    @PostMapping("/pay111")
    @ApiOperation(value = "统一付款(111)")
    public TransactionDto pay111(@Validated @RequestBody BillPayF billPayF) {
        return null;
    }


    @GetMapping("/query/pay/Detail")
    @ApiOperation("查询付款信息")
    public BillPayDataV getPayDetails(@ApiParam("交易单号") @RequestParam("transactionNo") String transactionNo){
        return new BillPayDataV();
    }


    @PostMapping("/gather")
    @ApiOperation(value = "统一收款")
    public BillTransactionV gather(@Validated @RequestBody BillGatherF billGatherF) {
        return new BillTransactionV();
    }

    @PostMapping("/enter")
    @ApiOperation(value = "统一入账")
    public List<BillUnitaryEnterResultDto> enter(@Validated @RequestBody @Size(min = 1, max = 200, message = "账单数量不允许，数量区间为[1,200]")
                              List<BillUnitaryEnterCommand> billUnitaryEnterFS,@NotBlank(message = "上级收费单元ID(项目ID)不能为空") @ApiParam("上级收费单元ID") @RequestParam("supCpUnitId")String supCpUnitId) {
        BillUnitaryEnterCommand billUnitaryEnterCommand = billUnitaryEnterFS.get(0);
        BillSettleCommand settleInfo = billUnitaryEnterCommand.getSettleInfo();
        String tradeNo = settleInfo.getTradeNo();
        IdempotentUtil.setIdempotent(tradeNo, UNIFIED_ACCOUNTING, 3, UNIFIED_ACCOUNTING_REPEAT);
        return unitaryBillAppService.enter(billUnitaryEnterFS,supCpUnitId);
    }

    @PostMapping("/query/maxGatherTime")
    @ApiOperation(value = "查询最大账单周期")
    public String getMaxGatherTime(@Validated @RequestBody MaxGatherTimeQuery query){
        return unitaryBillRepository.getMaxGatherTime(query);
    }

}
