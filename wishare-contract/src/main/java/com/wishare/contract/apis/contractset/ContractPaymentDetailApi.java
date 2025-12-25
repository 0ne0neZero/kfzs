package com.wishare.contract.apis.contractset;

import com.wishare.contract.domains.vo.contractset.InvoiceDetailPlanV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.contractset.ContractPaymentDetailPageF;
import com.wishare.contract.domains.entity.contractset.ContractPaymentDetailE;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractPaymentDetailAppService;
import com.wishare.contract.domains.vo.contractset.ContractPaymentDetailV;
import com.wishare.contract.apps.fo.contractset.ContractPaymentDetailF;
import com.wishare.contract.apps.fo.contractset.ContractPaymentDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractPaymentDetailUpdateF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 合同付款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-29
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"合同付款明细表"})
@RequestMapping("/contractPaymentDetail")
public class ContractPaymentDetailApi {

    private final ContractPaymentDetailAppService contractPaymentDetailAppService;

    @ApiOperation(value = "付款明细列表", notes = "付款明细列表", response = ContractPaymentDetailV.class)
    @GetMapping("/list/{collectionPlanId}")
    public List<ContractPaymentDetailV> contractPaymentDetailList(@PathVariable Long collectionPlanId) {
        return contractPaymentDetailAppService.contractPaymentDetailList(null, collectionPlanId);
    }

}
