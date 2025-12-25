package com.wishare.contract.apis.contractset;

import com.wishare.contract.apps.service.contractset.CollectionPlanDerateDetailAppService;
import com.wishare.contract.domains.vo.contractset.CollectionPlanDerateDetailV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 收款计划减免明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-07
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"收款计划减免明细"})
@RequestMapping("/collectionPlanDerateDetail")
public class CollectionPlanDerateDetailApi {

    private final CollectionPlanDerateDetailAppService collectionPlanDerateDetailAppService;

    @ApiOperation(value = "减免明细列表", notes = "减免明细列表")
    @GetMapping("/listByCollectionPlanId")
    public List<CollectionPlanDerateDetailV> listByCollectionPlanId(@RequestParam Long collectionPlanId) {
        return collectionPlanDerateDetailAppService.listByCollectionPlanId(collectionPlanId);
    }
}
