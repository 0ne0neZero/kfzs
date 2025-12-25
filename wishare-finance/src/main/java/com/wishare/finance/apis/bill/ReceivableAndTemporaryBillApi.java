package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.*;
import com.wishare.finance.apps.model.bill.vo.*;
import com.wishare.finance.apps.service.bill.ReceivableAndTemporaryBillService;
import com.wishare.finance.domains.bill.dto.BillTotalDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillGroupDetailDto;
import com.wishare.starter.annotations.ApiLogCustom;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author fengxiaolin
 * @date 2023/5/18
 */
@Api(tags = {"应收账单及临时账单混合查询api"})
@Validated
@RestController
@RequestMapping("/receivable/temporary")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ReceivableAndTemporaryBillApi {

    private final ReceivableAndTemporaryBillService receivableAndTemporaryBillService;

    @PostMapping("/groups")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分组分页查询应收账单和临时账单列表", notes = "分组分页查询应收账单列表")
    public PageV<ReceivableBillGroupDetailDto> getGroupPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableAndTemporaryBillService.getGroupPage(queryF, 0, true);
    }

    @PostMapping("/page")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "分页查询应收账单和临时账单列表", notes = "分页查询应收账单列表")
    public PageV<ReceivableBillPageV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableAndTemporaryBillService.getPage(queryF, ReceivableBillPageV.class);
    }

    @PostMapping("/getDeductionDetail")
    @ApiOperation(value = "分页获取减免明细", notes = "分页获取减免明细")
    PageV<ChargeDeductionDetailPageV> getDeductionDetail(@Validated @RequestBody PageF<SearchF<?>> pageF){
        return receivableAndTemporaryBillService.getDeductionDetail(pageF);
    }

    @PostMapping("/updateBillApprovedState")
    @ApiOperation(value = "批量修改应收账单与临时账单", notes = "批量修改应收账单与临时账单")
    Boolean updateBillApprovedState(@Validated @RequestBody UpdateBillApprovedStateF updateBillApprovedStateF) {
        return receivableAndTemporaryBillService.updateBillApprovedState(updateBillApprovedStateF);
    }

    @PostMapping("/getBatchDeductionBill")
    @ApiOperation(value = "获取批量减免账单信息", notes = "获取批量减免账单信息")
    Map<Integer,List<Long>> getBatchDeductionBill( @Validated @RequestBody GetBatchDeductionBillF getBatchDeductionBillF){
        return receivableAndTemporaryBillService.getBatchDeductionBill(getBatchDeductionBillF);
    }

    @PostMapping("/updateBillApproveState")
    @ApiOperation(value = "批量修改账单审批状态", notes = "批量修改账单审批状态")
    void updateBillApproveState(@RequestBody UpdateBillApproveStateF updateBillApproveStateF){
        receivableAndTemporaryBillService.updateBillApproveState(updateBillApproveStateF);
    }

    @PostMapping("/total")
    @ApiOperation(value = "账单总额查询", notes = "账单总额查询")
    public ReceivableAndTemporaryBillTotalV queryTotalMoney(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return receivableAndTemporaryBillService.queryTotalMoney(queryF);
    }

    @ApiOperation(value = "（通用）根据账单id集合获取应收和临时账单统计信息", notes = "（通用）根据账单id集合获取应收和临时账单统计信息")
    @PostMapping("/idListTotal")
    ReceivableAndTemporaryBillTotalV getTemporaryByIdsTotal(@Validated @RequestBody TemporaryBillF temporaryBillF){
        return receivableAndTemporaryBillService.queryTotalMoney(temporaryBillF);
    };

    @ApiOperation(value = "（通用）根据账单id集合获取应收和临时账单信息", notes = "（通用）根据账单id集合获取应收和临时账单信息")
    @PostMapping("/idList")
    public List<ReceivableBillDetailV> getTemporaryByIds(@Validated @RequestBody TemporaryBillF temporaryBillF){
       return receivableAndTemporaryBillService.getBillInfoByIds(temporaryBillF.getBillIds(), ReceivableBillDetailV.class,temporaryBillF.getCommunityId());
    }

    @ApiOperation(value = "根据查询条件获取应收和临时账单信息", notes = "根据查询条件获取应收和临时账单信息")
    @PostMapping("/conditionList")
    public List<ReceivableBillDetailV> getConditionList(@Validated @RequestBody TemporaryBillF temporaryBillF){
        return receivableAndTemporaryBillService.getConditionList(temporaryBillF, ReceivableBillDetailV.class,temporaryBillF.getCommunityId());
    }

    @ApiOperation(value = "根据账单id获取房间id", notes = "根据账单id获取房间id")
    @PostMapping("/roomIds")
    public List<Long> getRoomIds(@RequestBody TemporaryBillF temporaryBillF){
        return receivableAndTemporaryBillService.getRoomIds(temporaryBillF);
    }

    @PostMapping("/billTotal")
    @ApiLogCustom(switchClose = true)
    @ApiOperation(value = "统计应收账单和临时账单金额总数", notes = "统计账单金额总数")
    public BillTotalDto receivableAndTemporaryBillTotal(@Validated @RequestBody StatisticsBillAmountF statisticsBillAmountF){
        return receivableAndTemporaryBillService.receivableAndTemporaryBillTotal(statisticsBillAmountF);
    }

    @ApiOperation(value = "根据房号修改账单签约状态", notes = "根据房号修改账单签约状态")
    @PostMapping("/sign")
    public Boolean updateBillSign(@Validated @RequestBody UpdateSignF updateSignF) {
        return receivableAndTemporaryBillService.updateBillSign(updateSignF);
    }
    @ApiOperation(value = "根据房号集合修改账单签约状态", notes = "根据房号集合修改账单签约状态")
    @PostMapping("/batch/sign")
    public Boolean batchUpdateBillSign(@Validated @RequestBody BatchUpdateSignF batchUpdateSignF) {
        return receivableAndTemporaryBillService.batchUpdateBillSign(batchUpdateSignF);
    }



    // 临时账单、应收账单  添加标记 取消标记接口
    @ApiOperation(value = "临时账单、应收账单  添加标记 取消标记接口", notes = "临时账单、应收账单  添加标记 取消标记接口")
    @PostMapping("/editRecAndTemPushFlag")
    public Boolean editRecAndTemPushFlag(@Validated @RequestBody BillFlagF billFlagF){
        return receivableAndTemporaryBillService.editRecAndTemPushFlag(billFlagF);
    }


}
