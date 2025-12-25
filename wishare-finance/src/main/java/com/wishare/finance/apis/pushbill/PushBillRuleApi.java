package com.wishare.finance.apis.pushbill;


import com.wishare.finance.apps.event.receipt.FundReceiptsBillPublisher;
import com.wishare.finance.apps.pushbill.fo.*;
import com.wishare.finance.apps.pushbill.validation.AddGroup;
import com.wishare.finance.apps.pushbill.validation.UpdateGroup;
import com.wishare.finance.apps.pushbill.vo.BillRuleV;
import com.wishare.finance.apps.pushbill.vo.RuleRemindConfigDetailV;
import com.wishare.finance.apps.pushbill.vo.VoucherBillV;
import com.wishare.finance.apps.service.pushbill.BillRuleAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.voucher.entity.PushBillConditionOptionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.FyVoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.BillRuleConditionZJTypeEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = {"推单规则"})
@RestController
@RequestMapping("/bill/rule")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PushBillRuleApi {


    private final BillRuleAppService billRuleAppService;

    private final BillRuleRepository billRuleRepository;

    private final FundReceiptsBillPublisher fundReceiptsBillPublisher;

    @PostMapping("/execute")
    @ApiOperation(value = "运行推单规则")
    public boolean execute(@RequestBody @Validated RunBillRuleF RunBillRuleF){
        return billRuleAppService.executeBillRule(RunBillRuleF);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增推单规则")
    public Long addBillRule(@RequestBody @Validated(AddGroup.class) BillRuleF addBillRuleF) {
        return billRuleAppService.addRule(addBillRuleF);
    }
    @PostMapping("/update")
    @ApiOperation(value = "修改推单规则")
    public boolean updateBillRule(@RequestBody @Validated(UpdateGroup.class) BillRuleF updateBillRuleF) {
        return billRuleAppService.updateRule(updateBillRuleF);
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启用禁用凭证规则")
    public Boolean enable(@RequestBody @Validated EnableBillRuleF enableBillRuleF){
        return billRuleAppService.enableBillRule(enableBillRuleF);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除凭证规则")
    public boolean delete(@RequestBody @Validated DeleteBillRuleF deleteBillRuleF){
        return billRuleAppService.deleteBillRule(deleteBillRuleF);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "根据规则id查询推单规则详情")
    public VoucherBillV getDetailById(@RequestParam("voucherRuleId") @ApiParam(value = "凭证规则id",required = true) @NotNull(message = "凭证规则id不能为空") Long voucherRuleId){
        return Global.mapperFacade.map(billRuleRepository.getById(voucherRuleId), VoucherBillV.class);
    }
    @PostMapping("/page")
    @ApiOperation(value = "分页查询凭证规则")
    public PageV<BillRuleV> getPage(@RequestBody @Validated PageF<SearchF<?>> searchFPageF) {
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            return billRuleAppService.selectBySearch(searchFPageF);
        }else {
            return RepositoryUtil.convertPage(billRuleRepository.getPage(searchFPageF), BillRuleV.class);
        }
    }

    @GetMapping("/query/conditionOptions")
    @ApiOperation(value = "查询过滤条件列表")
    public List<PushBillConditionOptionOBV> getConditionOptions(@RequestParam("conditionType") @NotNull(message = "推单规则筛选条件类型不能为空") Integer conditionType){
        return billRuleAppService.getConditionOptions(BillRuleConditionZJTypeEnum.valueOfByCode(conditionType));
    }

    @GetMapping("/query/fyConditionOptions")
    @ApiOperation(value = "查询方圆报账过滤条件列表")
    public List<VoucherRuleConditionOptionOBV> getFyConditionOptions(
            @RequestParam("conditionType") @NotNull(message = "推单规则筛选条件类型不能为空") Integer conditionType){
        return billRuleAppService.getFYConditionOptions(FyVoucherRuleConditionTypeEnum.valueOfByCode(conditionType));
    }


    /**
     * 中交根据认领记录生成资金收款单
     */

    @PostMapping("/ZJFundReceiptsBill/execute")
    @ApiOperation(value = "运行推单规则中交定制")
    public boolean executeZjFundReceiptsBill(@RequestBody @Validated  FundReceiptsBillZJF receiptsBillZJF){
        //return billRuleAppService.executeZjFundReceiptsBill(receiptsBillZJF);
        fundReceiptsBillPublisher.publishEvent(receiptsBillZJF);
        return true;
    }


    @PostMapping("/revenueApprove")
    @ApiOperation(value = "收入确认单发起审批")
    public Boolean revenueApprove(@RequestBody @Validated RevenueApprove revenueApprove){
        return billRuleAppService.revenueApprove(revenueApprove);
    }

    @GetMapping("/remindRule/detail")
    @ApiOperation(value = "保存报账汇总规则提醒配置")
    public RuleRemindConfigDetailV remindRuleDetail(@RequestParam("id") Long id) {
        return billRuleAppService.remindRuleDetail(id);
    }

    @GetMapping("/remindRule/testSend")
    @ApiOperation(value = "测试消息发送")
    public Boolean testSend() {
        return billRuleAppService.testSend();
    }

    @GetMapping("/remindRule/send")
    @ApiOperation(value = "测试消息发送")
    public Boolean send() {
        billRuleAppService.send();
        return Boolean.TRUE;
    }

}
