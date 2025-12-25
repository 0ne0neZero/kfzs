package com.wishare.contract.apis.revision.bond;

import com.wishare.contract.apps.fo.revision.bond.BondVolumUpF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondBillF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectAddF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectEditF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayEditF;
import com.wishare.contract.apps.service.revision.bond.BondCollectBusinessService;
import com.wishare.contract.apps.service.revision.export.PayExportService;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import com.wishare.contract.domains.vo.revision.bond.BondNumShowV;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayV;
import com.wishare.contract.domains.vo.revision.bond.pay.VolumeUpV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeDetailV;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/27  15:30
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金改版-收取类保证金管理表"})
@RequestMapping("/manage/revisionBondCollect")
public class RevisionBondCollectManagetApi {

    private final BondCollectBusinessService collectBusinessService;

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String add(@Validated @RequestBody RevisionBondCollectAddF addF){
        return collectBusinessService.add(addF);
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @PostMapping("/edit")
    public Boolean edit(@Validated @RequestBody RevisionBondCollectEditF editF) {
        collectBusinessService.edit(editF);
        return true;
    }

    @ApiOperation(value = "提交保证金计划", notes = "提交保证金计划")
    @GetMapping("/post")
    public Boolean post(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return collectBusinessService.postBondPlan(id);
    }

    @ApiOperation(value = "根据保证金计划ID获取保证金详情", notes = "根据保证金计划ID获取保证金详情")
    @GetMapping("/detail")
    public BondCollectDetailV getDetailV(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return collectBusinessService.detail(id);
    }

    @ApiOperation(value = "收款", notes = "收款")
    @PostMapping("/collect")
    public Boolean collect(@Validated @RequestBody CollectBondBillF form) {
        return collectBusinessService.collect(form);
    }

    @ApiOperation(value = "结转", notes = "结转")
    @PostMapping("/transfer")
    public Boolean transfer(@Validated @RequestBody CollectBondBillF form) {
        return collectBusinessService.transfer(form);
    }

    @ApiOperation(value = "退款", notes = "退款")
    @PostMapping("/refund")
    public Boolean refund(@Validated @RequestBody CollectBondBillF form) {
        return collectBusinessService.refund(form);
    }

    @ApiOperation(value = "收据", notes = "收据")
    @PostMapping("/receipt")
    public Boolean receipt(@Validated @RequestBody CollectBondBillF form) {
        return collectBusinessService.receipt(form);
    }

    @ApiOperation(value = "获取列表金额统计数据", response = RevisionBondPayV.class)
    @PostMapping("/getNumShowForPage")
    public BondNumShowV getNumShowForPage(@RequestBody PageF<SearchF<RevisionBondCollectE>> request) {
        return collectBusinessService.getNumShowForPage(request);
    }

    @ApiOperation(value = "转履约", notes = "转履约")
    @PostMapping("/volumUp")
    public Boolean volumUp(@Validated @RequestBody BondVolumUpF form) {
        return collectBusinessService.volumeUp(form);
    }

    @ApiOperation(value = "获取转履约记录", notes = "获取转履约记录")
    @GetMapping("/getVolumRecord")
    public List<VolumeUpV> getVolumRecord(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return collectBusinessService.getVolumRecord(id);
    }

}
