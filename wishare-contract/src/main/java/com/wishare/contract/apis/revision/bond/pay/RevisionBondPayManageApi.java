package com.wishare.contract.apis.revision.bond.pay;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.bond.BondVolumUpF;
import com.wishare.contract.apps.fo.revision.bond.pay.PayBondBillF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayAddF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayEditF;
import com.wishare.contract.apps.service.revision.bond.pay.BondPayBusinessService;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.vo.revision.bond.BondNumShowV;
import com.wishare.contract.domains.vo.revision.bond.pay.BondPayDetailV;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayV;
import com.wishare.contract.domains.vo.revision.bond.pay.VolumeUpV;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/28  11:30
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"保证金改版-缴纳类保证金管理表"})
@RequestMapping("/manage/revisionBondPay")
public class RevisionBondPayManageApi {

    private final BondPayBusinessService payBusinessService;

    @ApiOperation(value = "新增保存", notes = "新增保存")
    @PostMapping("/add")
    public String add(@Validated @RequestBody RevisionBondPayAddF addF){
        return payBusinessService.add(addF);
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @PostMapping("/edit")
    public Boolean edit(@Validated @RequestBody RevisionBondPayEditF editF) {
        payBusinessService.edit(editF);
        return true;
    }

    @ApiOperation(value = "提交保证金计划", notes = "提交保证金计划")
    @GetMapping("/post")
    public Boolean post(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return payBusinessService.postBondPlan(id);
    }

    @ApiOperation(value = "根据保证金计划ID获取保证金详情", notes = "根据保证金计划ID获取保证金详情")
    @GetMapping("/detail")
    public BondPayDetailV getDetailV(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return payBusinessService.detail(id);
    }

    @ApiOperation(value = "收款", notes = "收款")
    @PostMapping("/collect")
    public Boolean collect(@Validated @RequestBody PayBondBillF form) {
        return payBusinessService.collect(form);
    }

    @ApiOperation(value = "结转", notes = "结转")
    @PostMapping("/transfer")
    public Boolean transfer(@Validated @RequestBody PayBondBillF form) {
        return payBusinessService.transfer(form);
    }

    @ApiOperation(value = "付款", notes = "付款")
    @PostMapping("/pay")
    public Boolean refund(@Validated @RequestBody PayBondBillF form) {
        return payBusinessService.pay(form);
    }

    @ApiOperation(value = "收据", notes = "收据")
    @PostMapping("/receipt")
    public Boolean receipt(@Validated @RequestBody PayBondBillF form) {
        return payBusinessService.receipt(form);
    }

    @ApiOperation(value = "获取列表金额统计数据", response = RevisionBondPayV.class)
    @PostMapping("/getNumShowForPage")
    public BondNumShowV getNumShowForPage(@RequestBody PageF<SearchF<RevisionBondPayE>> request) {
        return payBusinessService.getNumShowForPage(request);
    }

    @ApiOperation(value = "转履约", notes = "转履约")
    @PostMapping("/volumUp")
    public Boolean volumUp(@Validated @RequestBody BondVolumUpF form) {
        return payBusinessService.volumeUp(form);
    }

    @ApiOperation(value = "获取转履约记录", notes = "获取转履约记录")
    @GetMapping("/getVolumRecord")
    public List<VolumeUpV> getVolumRecord(@RequestParam("id") @NotBlank(message = "保证金计划ID不可为空") String id) {
        return payBusinessService.getVolumRecord(id);
    }
    
}
