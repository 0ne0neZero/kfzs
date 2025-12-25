package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.vo.BillContractV;
import com.wishare.finance.apps.service.bill.contract.BillContractAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillContractApi
 * @date 2024.07.04  14:28
 * @description: 账单关联合同信息API
 */
@Api(tags = {"账单关联合同信息API"})
@Validated
@RestController
@RequestMapping("/billContract")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillContractApi {

    private final BillContractAppService billContractAppService;

    @GetMapping("/getByBillId")
    @ApiOperation(value = "根据账单id获取关联合同编号", notes = "根据账单id获取关联合同编号")
    public BillContractV getByBillId(@RequestParam("billId") Long billId) {
        return billContractAppService.getByBillId(billId);
    }
}
