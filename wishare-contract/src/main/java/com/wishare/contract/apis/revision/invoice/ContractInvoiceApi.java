package com.wishare.contract.apis.revision.invoice;

import com.wishare.contract.apps.fo.revision.invoice.ContractInvoiceF;
import com.wishare.contract.apps.service.revision.invoice.ContractInvoiceAppService;
import com.wishare.contract.domains.vo.contractset.ContractConcludeV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/1/20:15
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"新合同收票信息表"})
@RequestMapping("/contractInvoice")
public class ContractInvoiceApi {

    private final ContractInvoiceAppService contractInvoiceAppService;

    @ApiOperation(value = "收票信息删除", notes = "收票信息删除")
    @DeleteMapping
    public boolean removeById(@RequestParam("id") String id){
        return contractInvoiceAppService.removeById(id);
    }

    @ApiOperation(value = "根据供应商客户id查询关联票据", notes = "根据供应商客户id查询关联票据", response = ContractPayBillV.class)
    @PostMapping("/getBillsByOppositeid")
    public List<ContractPayBillV> getBillsByOppositeid(@RequestBody PageF<SearchF<ContractInvoiceF>> contractInvoiceF) {
        return contractInvoiceAppService.getDetailsByIdSettle(contractInvoiceF);
    }
}
