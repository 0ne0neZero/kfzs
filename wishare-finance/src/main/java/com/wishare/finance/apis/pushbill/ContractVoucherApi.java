package com.wishare.finance.apis.pushbill;

import com.wishare.finance.apps.pushbill.fo.ContractInvoiceInfoF;
import com.wishare.finance.apps.service.pushbill.ContractVoucherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成报账单合同数据推送
 */

@Api(tags = {"合同数据推送"})
@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContractVoucherApi {


    private final ContractVoucherService contractVoucherService;

    @PostMapping("/acceptInfo")
    @ApiOperation(value = "合同系统推送收票信息且生成对下结算单", notes = "合同系统推送收票信息且生成对下结算单")
    public Long  acceptContractInvoice(@RequestBody ContractInvoiceInfoF contractInvoiceInfoF){
        return  contractVoucherService.acceptContractInvoice(contractInvoiceInfoF);
    }
}
