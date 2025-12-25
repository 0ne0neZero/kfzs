package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.QueryReconcileGroupF;
import com.wishare.finance.apps.service.bill.BillReconciliationAppService;
import com.wishare.finance.domains.bill.dto.ReconciliationGroupDto;
import com.wishare.finance.infrastructure.support.mutiltable.MutilTableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"对账接口"})
@Validated
@RestController
@RequestMapping("/reconcile")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillReconciliationApi {

    private final BillReconciliationAppService billReconciliationAppService;

    @PostMapping("/dimensions")
    @ApiOperation(value = "查询所需对账的维度分组", notes = "查询所需对账的维度分组")
    public List<ReconciliationGroupDto> getReconcileGroups(@Validated @RequestBody QueryReconcileGroupF queryReconcileGroupF){
        MutilTableParam.supCpUnitId.set(queryReconcileGroupF.getSupCpUnitId());
        return billReconciliationAppService.getReconcileGroupsClear(queryReconcileGroupF);
    }

}
