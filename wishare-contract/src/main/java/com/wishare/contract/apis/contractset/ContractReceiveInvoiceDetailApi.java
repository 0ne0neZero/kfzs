package com.wishare.contract.apis.contractset;

import org.springframework.web.bind.annotation.*;


import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractReceiveInvoiceDetailAppService;
import com.wishare.contract.domains.vo.contractset.ContractReceiveInvoiceDetailV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 付款计划收票明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-29
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"付款计划收票明细"})
@RequestMapping("/contractReceiveInvoiceDetail")
public class ContractReceiveInvoiceDetailApi {

    private final ContractReceiveInvoiceDetailAppService contractReceiveInvoiceDetailAppService;

    @ApiOperation(value = "付款收票列表", notes = "付款收票列表", response = ContractReceiveInvoiceDetailV.class)
    @GetMapping("/list/{collectionPlanId}")
    public List<ContractReceiveInvoiceDetailV> contractReceiveInvoiceDetailList(@PathVariable Long collectionPlanId) {
        return contractReceiveInvoiceDetailAppService.contractReceiveInvoiceDetailList(null, collectionPlanId);
    }

}
