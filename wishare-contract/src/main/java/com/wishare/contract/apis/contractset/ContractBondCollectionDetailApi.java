package com.wishare.contract.apis.contractset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailPageF;
import com.wishare.contract.domains.entity.contractset.ContractBondCollectionDetailE;
import org.springframework.web.bind.annotation.RestController;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractBondCollectionDetailAppService;
import com.wishare.contract.domains.vo.contractset.ContractBondCollectionDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailF;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailUpdateF;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 保证金计划收款明细
 * </p>
 *
 * @author ljx
 * @since 2022-10-25
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金计划收款明细"})
@RequestMapping("/contractBondCollectionDetail")
public class ContractBondCollectionDetailApi {

    private final ContractBondCollectionDetailAppService contractBondCollectionDetailAppService;

    @ApiOperation("收款明细列表")
    @GetMapping("/listByBondPlanId")
    public List<ContractBondCollectionDetailV> listByBondPlanId(@RequestParam Long bondPlanId) {
        return contractBondCollectionDetailAppService.listByBondPlanId(bondPlanId);
    }
}
