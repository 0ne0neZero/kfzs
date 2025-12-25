package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.service.bill.BillSettleAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@Api(tags = {"账单结算"})
@Validated
@RestController
@RequestMapping("/settle")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillSettleApi {

    private final BillSettleAppService billSettleAppService;

    @PostMapping("/list/ids")
    @ApiOperation(value = "根据结算id列表查询结算记录", notes = "根据结算id列表查询结算记录")
    public List<BillSettleV> getSettleByIds(@RequestBody List<Long> settleIds
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return billSettleAppService.getSettleByIds(settleIds, supCpUnitId);
    }

    @PostMapping("/list/{billId}")
    @ApiOperation(value = "根据账单id获取结算记录", notes = "根据账单id获取结算记录")
    public List<BillSettleV> getSettleByBillId(@PathVariable("billId") Long billId,@RequestParam(value = "billType",required = false) Integer billType
    ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return billSettleAppService.getSettleByBillId(billId,billType, supCpUnitId);
    }

    @PostMapping("/list/billIds")
    @ApiOperation(value = "根据账单id列表获取结算记录", notes = "根据账单id列表获取结算记录")
    public List<BillSettleV> getSettleByBillIds(@RequestBody List<Long> billIds
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId){
        return billSettleAppService.getSettleByBillIds(billIds, supCpUnitId);
    }

    @GetMapping("/list/channel")
    @ApiOperation(value = "根据账单id获取结算方式", notes = "根据账单id获取结算方式")
    public List<BillSettleChannelV> listBillSettleChannelByIds(@RequestParam List<Long> list
        ,@NotBlank(message = "上级收费单元id不能为空") @RequestParam("supCpUnitId") @ApiParam("上级收费单元id")  String supCpUnitId) {

        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return billSettleAppService.listBillSettleChannelByIds(list, supCpUnitId);
    }

    @PostMapping("/list/channel/ids")
    @ApiOperation(value = "根据账单id和结算方式获取账单id", notes = "根据账单id和结算方式获取账单id")
    public List<Long> listBillIdsByIdsAndChannel(@Validated @RequestBody SettleChannelAndIdsF form) {

        return billSettleAppService.listBillIdsByIdsAndChannel(form);
    }
}
