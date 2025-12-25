package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.service.bill.BillCarryoverDetailAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yyx
 * @project wishare-finance
 * @title BillCarryoverDetailApi
 * @date 2023.09.21  11:04
 * @description
 */
@Api(tags = {"账单结转详情api"})
@Validated
@RestController
@RequestMapping("/billCarryoverDetail")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryoverDetailApi {

    private final BillCarryoverDetailAppService billCarryoverDetailAppService;

    @PostMapping("/resetInfo")
    @ApiOperation(value = "解析结转表里的信息转储", notes = "解析结转表里的信息转储")
    public Boolean reverseCarryoverInfo(@RequestParam(required = true) Boolean isTodayOpr){
        return billCarryoverDetailAppService.reverseCarryoverInfo(isTodayOpr);
    }

    @ApiOperation(value = "根据账单id查询审核的结转账单id", notes = "根据账单id查询审核的结转账单id")
    @PostMapping("/getTargetIdByBillId")
    List<Long> getTargetIdByBillId(@RequestParam("billId") Long billId, @RequestParam("supCpUnitId") String supCpUnitId) {
        return billCarryoverDetailAppService.getTargetIdByBillId(billId, supCpUnitId);
    }

    /**
     *
     * @param targetId
     * @param supCpUnitId
     * @return
     */
    @ApiOperation(value = "根据结转目标id查询校验账单是否被结转过", notes = "根据结转目标id查询校验账单是否被结转过")
    @PostMapping("/checkTargetIdHadCarryoverd")
    public boolean checkTargetIdHadCarryoverd(@RequestParam("targetId") Long targetId) {
        return billCarryoverDetailAppService.checkTargetIdHadCarryoverd(targetId);
    }




}
