package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.BatchAddBillInferenceF;
import com.wishare.finance.apps.model.bill.fo.BatchDelBillInferenceF;
import com.wishare.finance.apps.service.bill.BillInferenceAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 账单推凭
 * @author: pgq
 * @since: 2022/10/26 9:32
 * @version: 1.0.0
 */
@Api(tags = {"账单推凭"})
@Validated
@RestController
@RequestMapping("/inference")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillInferenceApi {

    private final BillInferenceAppService billInferenceAppService;

    @PostMapping("/add/batch")
    @ApiOperation(value = "批量新增账单推凭状态")
    List<Long> batchInsertInference(@RequestBody BatchAddBillInferenceF form) {

        return billInferenceAppService.batchInsertInference(form);
    }
    @PostMapping("/del/batch")
    @ApiOperation(value = "批量新增账单推凭状态")
    boolean batchDeleteInference(@RequestBody BatchDelBillInferenceF form) {

        if (CollectionUtils.isEmpty(form.getInferIds())) {
            return true;
        }
        return billInferenceAppService.batchDeleteInference(form);
    }
}
