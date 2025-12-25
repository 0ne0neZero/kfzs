package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.fo.contractset.ContractBondReceiptDetailF;
import com.wishare.contract.apps.remote.fo.ReceiptDetailRf;
import com.wishare.contract.apps.remote.vo.ReceiptDetailRv;
import com.wishare.contract.domains.vo.contractset.ContractBondReceiptDetailV;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractBondReceiptDetailAppService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * <p>
 * 保证金收据明细表
 * </p>
 *
 * @author ljx
 * @since 2022-12-12
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金收据明细"})
@RequestMapping("/contractBondReceiptDetail")
public class ContractBondReceiptDetailApi {

    private final ContractBondReceiptDetailAppService contractBondReceiptDetailAppService;

    @ApiOperation("收据明细列表")
    @GetMapping("/listByBondPlanId")
    public List<ContractBondReceiptDetailV> listByBondPlanId(Long bondPlanId) {
        return contractBondReceiptDetailAppService.listByBondPlanId(bondPlanId);
    }

    @ApiOperation(value = "查询收据详情",notes = "查询收据详情")
    @PostMapping("/receipt/detail")
    public ReceiptDetailRv receiptDetail(@RequestBody ReceiptDetailRf from) {
        return contractBondReceiptDetailAppService.receiptDetail(from);
    }

    @ApiOperation(value = "批量更新收据明细",notes = "批量更新收据明细")
    @PutMapping("/update")
    public List<Boolean> updateReceiptDetail(@RequestBody List<ContractBondReceiptDetailF> from) {
        return contractBondReceiptDetailAppService.updateReceiptDetail(from);
    }
}
