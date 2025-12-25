package com.wishare.finance.apis.bill;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.apps.model.bill.fo.ChargeOverdueBatchDeleteF;
import com.wishare.finance.apps.model.bill.fo.StateUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillOverduePageV;
import com.wishare.finance.apps.model.bill.vo.BillOverdueStatisticsV;
import com.wishare.finance.apps.model.bill.vo.ReceivableBillDetailV;
import com.wishare.finance.domains.bill.entity.ChargeOverdueE;
import com.wishare.finance.domains.bill.service.ChargeOverdueService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"违约金api"})
@RestController
@RequestMapping("/overdue")
@RequiredArgsConstructor
public class ChargeOverdueApi implements ApiBase {

    private final ChargeOverdueService chargeOverdueService;


    @PostMapping("/page")
    @ApiOperation(value = "查询违约金列表", notes = "查询违约金列表")
    public PageV<BillOverduePageV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF){
        return chargeOverdueService.findPage(queryF, getTenantId().get());
    }

    @PostMapping("/count")
    @ApiOperation(value = "统计违约金", notes = "统计违约金")
    public BillOverdueStatisticsV queryCount(@Validated @RequestBody PageF<SearchF<?>> queryF){
        return chargeOverdueService.queryCount(queryF, getTenantId().get());
    }

    @PostMapping("/getOverdueByRefBillId")
    @ApiOperation(value = "获取违约金根据账单ID", notes = "获取违约金根据账单ID")
    public ChargeOverdueE getOverdueByRefBillId(@Validated @RequestParam Long refBillId){
        return chargeOverdueService.getOverdueByRefBillId(refBillId);
    }

    @PostMapping("/getOverdueByRefBillNo")
    @ApiOperation(value = "获取违约金信息根据账单编号", notes = "获取违约金信息根据账单编号")
    public List<ChargeOverdueE> getOverdueByRefBillNo(@Validated @RequestParam String refBillNo){
        return chargeOverdueService.getOverdueByRefBillNo(refBillNo);
    }

    @PostMapping("/getOverdueByOverdueNo")
    @ApiOperation(value = "获取违约金信息根据账单编号", notes = "获取违约金信息根据账单编号")
    public List<ChargeOverdueE> getOverdueByOverDueNo(@Validated @RequestParam String overdueNo){
        return chargeOverdueService.getOverdueByOverDueNo(overdueNo);
    }

    @PostMapping("/getById")
    @ApiOperation(value = "根据ID获取违约金", notes = "根据ID获取违约金")
    public ChargeOverdueE getById(@Validated @RequestParam Long overdueId){
        return chargeOverdueService.getById(overdueId);
    }

    @PostMapping("/updateSettleState")
    @ApiOperation(value = "更新结算状态", notes = "更新结算状态")
    public Integer updateSettleState(@Validated @RequestBody List<StateUpdateF> list){
        return chargeOverdueService.updateSettleState(list);
    }

    @PostMapping("/getOverdueByBillIds")
    @ApiOperation(value = "通过账单ID列表获取违约金管理数据", notes = "通过账单ID列表获取违约金管理数据")
    public List<ChargeOverdueE> getOverdueByBillIds(@Validated @RequestBody List<Long> billIdList){
        return chargeOverdueService.getOverdueByBillIds(billIdList);
    }

    @PostMapping("/getByIds")
    @ApiOperation(value = "根据主体账单获取违约金账单信息", notes = "根据主体账单获取违约金账单信息")
    public List<ReceivableBillDetailV> getByIds(@Validated @RequestBody List<Long> refBillIdList){
        return chargeOverdueService.getOverdueReceivableBillDetailVByBillIds(refBillIdList);
    }

    @PostMapping("/getOverdueByRefBillIds")
    @ApiOperation(value = "根据主体账单获取违约金账单信息", notes = "根据主体账单获取违约金账单信息")
    public List<ChargeOverdueE> getOverdueByRefBillIds(@Validated @RequestBody List<Long> refBillIdList){
        return chargeOverdueService.getOverdueByRefBillIds(refBillIdList);
    }

    @PostMapping("/updateById")
    @ApiOperation(value = "根据ID更新违约金", notes = "根据ID更新违约金")
    public Boolean updateById(@Validated @RequestBody ChargeOverdueE overdue){
        return chargeOverdueService.updateById(overdue);
    }

    @PostMapping("/getOverdueByBillId")
    @ApiOperation(value = "根据ID获取违约金", notes = "根据ID获取违约金")
    public ChargeOverdueE getOverdueByBillId(@RequestParam @NotNull(message = "账单ID不能为空") Long billId){
        return chargeOverdueService.getOverdueByBillId(billId);
    }

    @PostMapping("/deleteByBillIds")
    @ApiOperation(value = "根据IDs删除违约金", notes = "根据IDs删除违约金")
    public boolean deleteByBillIds(@Validated @RequestBody List<Long> billIds){
        return chargeOverdueService.deleteByBillIds(billIds);
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增违约金", notes = "新增违约金")
    public Boolean save(@Validated @RequestBody ChargeOverdueE overdue){
        return chargeOverdueService.save(overdue);
    }

    @PostMapping("/saveOrUpdateBatch")
    @ApiOperation(value = "更新违约金", notes = "更新违约金")
    public List<ChargeOverdueE> saveOrUpdateBatch(@Validated @RequestBody List<ChargeOverdueE> overdueEList){
        chargeOverdueService.saveOrUpdateBatch(overdueEList);
        return overdueEList;
    }

    @PostMapping("/delBatch")
    @ApiOperation(value = "批量删除违约金信息", notes = "批量删除违约金信息")
    public void del(@RequestParam("communityId") @ApiParam("项目ID") @NotNull(message = "项目ID不能为空")String communityId
            ,@Validated @RequestBody List<ChargeOverdueE> chargeOverdueEList){
        chargeOverdueService.del(communityId,chargeOverdueEList);
    }


    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除违约金信息", notes = "批量删除违约金信息")
    public Boolean delete(@Validated @RequestBody ChargeOverdueBatchDeleteF chargeOverdueBatchDeleteF){
        return chargeOverdueService.delete(chargeOverdueBatchDeleteF);
    }

}
