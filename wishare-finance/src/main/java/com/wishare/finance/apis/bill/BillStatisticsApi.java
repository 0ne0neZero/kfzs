package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.DiscountTotalF;
import com.wishare.finance.apps.model.bill.fo.StatisticsBillAmountF;
import com.wishare.finance.apps.model.bill.fo.StatisticsBillTotalByConditionF;
import com.wishare.finance.apps.model.bill.fo.StatisticsBillTotalF;
import com.wishare.finance.apps.model.bill.fo.StatisticsRoomBillAmountF;
import com.wishare.finance.apps.model.bill.vo.BillApproveStatisticsCountV;
import com.wishare.finance.apps.model.bill.vo.BillApproveTotalV;
import com.wishare.finance.apps.model.bill.vo.BillTotalV;
import com.wishare.finance.apps.service.bill.BillStatisticsAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.dto.BillDiscountTotalDto;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.ReasonBillTotalDto;
import com.wishare.finance.domains.bill.dto.RoomBillTotalDto;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
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
import java.util.Objects;

/**
 * 账单统计相关接口
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Api(tags = {"账单统计"})
@Validated
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillStatisticsApi {

    private final BillStatisticsAppService billStatisticsAppService;

    private final SpacePermissionAppService spacePermissionAppService;

    @PostMapping("/bills")
    @ApiOperation(value = "统计账单金额总数", notes = "统计账单金额总数")
    public BillTotalDto total(@Validated @RequestBody StatisticsBillTotalByConditionF statisticsBillTotalF){
        PageF<SearchF<?>> query = statisticsBillTotalF.getQuery();
        if(Objects.nonNull(query)){
            String alisa = "b";
            PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(query,alisa);
            if(Objects.isNull(queryPermissionF)){
                return new BillTotalDto();
            }else{
                statisticsBillTotalF.setQuery(queryPermissionF);
            }
        }
        return billStatisticsAppService.bills(statisticsBillTotalF);
    }

    @PostMapping("/billTotal")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "统计账单金额总数", notes = "统计账单金额总数")
    public BillTotalDto total(@Validated @RequestBody StatisticsBillAmountF statisticsBillAmountF){
       return billStatisticsAppService.queryTotal(statisticsBillAmountF);
    }

    @PostMapping("/receivable/billTotal")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "统计应收账单或临时账单金额总数", notes = "统计账单金额总数")
    public BillTotalDto receivableTotal(@Validated @RequestBody StatisticsBillAmountF statisticsBillAmountF){
        return billStatisticsAppService.queryReceivableTotal(statisticsBillAmountF);
    }

    @PostMapping("/billRefund")
    @ApiOperation(value = "退款账单统计", notes = "退款账单统计")
    public BillTotalDto billRefund(@Validated @RequestBody StatisticsBillAmountF statisticsBillAmountF){
        statisticsBillAmountF.setBillRefund(1);
        return billStatisticsAppService.queryRefundTotal(statisticsBillAmountF);
    }

    @PostMapping("/approve")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "根据状态查询账单数合计", notes = "根据状态查询账单数合计")
    public List<BillApproveTotalV> queryApproveTotal(@Validated @RequestBody StatisticsBillTotalF statisticsBillTotalF){
        return billStatisticsAppService.queryApproveTotal(statisticsBillTotalF);
    }

    @PostMapping("/approve/new")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "查询审核账单数合计", notes = "根据状态查询账单数合计")
    public BillApproveStatisticsCountV queryApproveTotalNew(
            @Validated @RequestBody StatisticsBillTotalF statisticsBillTotalF){

        return billStatisticsAppService.queryApproveTotalNew(statisticsBillTotalF);
    }

    @PostMapping("/discount")
    @ApiOperation(value = "统计减免合计金额", notes = "统计减免合计金额")
    public BillDiscountTotalDto queryDiscountTotal(@Validated @RequestBody DiscountTotalF discountTotalF){
        return billStatisticsAppService.queryDiscountTotal(discountTotalF);
    }

    @PostMapping("/call")
    @ApiOperation(value = "催缴欠缴账单统计", notes = "催缴欠缴账单统计")
    public BillTotalDto call(@Validated @RequestBody StatisticsBillAmountF form){
        return billStatisticsAppService.call(form);
    }

    @PostMapping("/callGroupByRoomAndItem")
    @ApiOperation(value = "催缴欠缴账单统计（根据房间、费项进行分组）", notes = "返回结果集合")
    public List<BillTotalDto> callGroupByRoomAndItem(@Validated @RequestBody StatisticsBillAmountF form){
        return billStatisticsAppService.callGroupByRoomAndItem(form);
    }


    @PostMapping("/roomBills")
    @ApiOperation(value = "根据房号统计账单", notes = "根据房号统计账单")
    public List<RoomBillTotalDto> roomBills(@Validated @RequestBody StatisticsRoomBillAmountF form){
        return billStatisticsAppService.roomBills(form);
    }

    @PostMapping("/batchReasonBillTotal")
    @ApiOperation(value = "含欠费原因的账单统计", notes = "含欠费原因的账单统计")
    public ReasonBillTotalDto batchReasonBillTotal(@RequestBody PageF<SearchF<?>> queryF){
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return new ReasonBillTotalDto();
        }
        return billStatisticsAppService.batchReasonBillTotal(queryF);
    }
}
