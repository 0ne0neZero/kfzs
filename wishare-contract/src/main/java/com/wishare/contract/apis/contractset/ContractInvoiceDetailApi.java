package com.wishare.contract.apis.contractset;

import com.wishare.contract.domains.vo.contractset.InvoiceDetailPlanV;
import org.springframework.web.bind.annotation.*;
import com.wishare.contract.apps.service.contractset.ContractInvoiceDetailAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 合同开票明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同开票明细"})
@RequestMapping("/contractInvoiceDetail")
public class ContractInvoiceDetailApi {

    private final ContractInvoiceDetailAppService contractInvoiceDetailAppService;

    @ApiOperation(value = "开票明细列表", notes = "开票明细列表", response = InvoiceDetailPlanV.class)
    @GetMapping("/list/{collectionPlanId}")
    public List<InvoiceDetailPlanV> contractInvoiceDetailList(@PathVariable Long collectionPlanId) {
        return contractInvoiceDetailAppService.contractInvoiceDetailList(null, collectionPlanId);
    }
}
