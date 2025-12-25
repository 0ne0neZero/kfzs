package com.wishare.contract.apis.contractset;

import com.wishare.contract.domains.vo.contractset.CollectionDetailPlanV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.contract.apps.fo.contractset.ContractCollectionDetailPageF;
import com.wishare.contract.domains.entity.contractset.ContractCollectionDetailE;
import com.wishare.starter.enums.PromptInfo;
import com.wishare.tools.starter.fo.search.SearchF;
import org.springframework.validation.annotation.Validated;
import com.wishare.contract.apps.service.contractset.ContractCollectionDetailAppService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionDetailV;
import com.wishare.contract.apps.fo.contractset.ContractCollectionDetailF;
import com.wishare.contract.apps.fo.contractset.ContractCollectionDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractCollectionDetailUpdateF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
/**
 * <p>
 * 合同收款明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@RequiredArgsConstructor
@RestController
@Api(tags = {"合同收款明细"})
@RequestMapping("/contractCollectionDetail")
public class ContractCollectionDetailApi {

    private final ContractCollectionDetailAppService contractCollectionDetailAppService;

    @ApiOperation(value = "收款明细列表", notes = "收款明细列表", response = CollectionDetailPlanV.class)
    @GetMapping("/list/{collectionPlanId}")
    public List<CollectionDetailPlanV> contractCollectionDetailList(@PathVariable Long collectionPlanId) {
        return contractCollectionDetailAppService.contractCollectionDetailList(null, collectionPlanId);
    }
}
