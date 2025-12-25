package com.wishare.finance.apis.pushbill;

import com.wishare.finance.apps.pushbill.fo.VoucherBillRecMdm63F;
import com.wishare.finance.apps.pushbill.vo.*;
import com.wishare.finance.apps.scheduler.mdm.vo.ZjDictionaryResponse;
import com.wishare.finance.apps.service.bill.FinanceCloudService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Api(tags = {"财务云-公共接口"})
@RestController
@RequestMapping("/financeCloud")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommonApi {

    private final FinanceCloudService financeCloudService;


    @PostMapping("/paymentMethod")
    @ApiOperation(value = "结算方式", notes = "结算方式")
    public ZjDictionaryResponse<PaymentMethodV> queryPaymentMethod(@RequestParam Integer pageNum) {
        return financeCloudService.queryPaymentMethod(pageNum);
    }

    @PostMapping("/cashFlow")
    @ApiOperation(value = "现金流量", notes = "现金流量")
    public List<CashFlowV> queryCashFlow() {
        return financeCloudService.queryCashFlow();
    }

    @PostMapping("/sync/mdm11")
    @ApiOperation(value = "同步mdm11", notes = "同步mdm11")
    public void syncMdm11() {
        financeCloudService.doSyncMdm11();
    }

    @PostMapping("/sync/mdm97")
    @ApiOperation(value = "同步mdm97", notes = "同步mdm97")
    public void syncMdm97(@RequestParam("oid") String oid){
        financeCloudService.syncMdm97(oid);
    }

    @PostMapping("/sync/mdm73")
    @ApiOperation(value = "付款账户信息查询", notes = "付款账户信息查询")
    public void mockSyncMdm73(){
        financeCloudService.syncMdm73();
    }

    @PostMapping("/paymentAccount")
    @ApiOperation(value = "付款账户信息", notes = "付款账户信息")
    public ZjDictionaryResponse<PaymentAccountV> queryPaymentAccount(@RequestParam Integer pageNum) {
        return financeCloudService.queryPaymentAccount(pageNum);
    }

    @PostMapping("/payCode")
    @ApiOperation(value = "资金计划编号", notes = "资金计划编号")
    public ZjDictionaryResponse<PayPlanCodeV> payCode(@RequestBody @Validated PayPlanCodeReqV payPlanCodeReqV) {
        return financeCloudService.payCode(payPlanCodeReqV);
    }

    @PostMapping("/businessSubjects")
    @ApiOperation(value = "业务科目", notes = "业务科目")
    public ZjDictionaryResponse<BusinessSubjectV> businessSubjects(@RequestParam Integer pageNum,@RequestParam(required = false) Integer direct) {
        return financeCloudService.businessSubjects(pageNum,direct);
    }

    @PostMapping("/unitBankInfo")
    @ApiOperation(value = "往来单位银行信息", notes = "资金计划编号")
    public ZjDictionaryResponse<BankAccountV> unitBankInfo(@RequestBody BankAccountReqV bankAccountReqV) {
        return financeCloudService.unitBankInfo(bankAccountReqV);
    }

    @PostMapping("/payBankInfo")
    @ApiOperation(value = "付款账户信息查询", notes = "付款账户信息查询")
    public List<BankAccountV> payBankInfo(@RequestBody BankAccountReqV bankAccountReqV) {
        return financeCloudService.payBankInfo(bankAccountReqV);
    }

    @PostMapping("/mdm63/page")
    @ApiOperation(value = "核销应付清单列表筛选", notes = "核销应付清单列表筛选")
    public PageV<Mdm63FrontV> queryMdm63Page(@RequestBody PageF<VoucherBillRecMdm63F> pageF){
        return financeCloudService.queryMdm63Page(pageF);
    }

}
