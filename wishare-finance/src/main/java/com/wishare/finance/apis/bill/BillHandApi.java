package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.BatchAccountHandF;
import com.wishare.finance.apps.model.bill.fo.BillHandAccountRuleF;
import com.wishare.finance.apps.model.bill.fo.HandAccountF;
import com.wishare.finance.apps.model.bill.fo.ReversalHandAccountF;
import com.wishare.finance.apps.model.bill.vo.BillAccountHandPageV;
import com.wishare.finance.apps.model.bill.vo.BillHandAccountRuleV;
import com.wishare.finance.apps.model.bill.vo.BillHandV;
import com.wishare.finance.apps.service.bill.HandAccountAppService;
import com.wishare.finance.apps.service.bill.factory.BillAppServiceFactory;
import com.wishare.finance.domains.bill.command.BatchRefreshAccountHandCommand;
import com.wishare.finance.domains.bill.dto.AccountHandTotalDto;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.HandAccountDto;
import com.wishare.finance.domains.bill.entity.BillHandAccountRule;
import com.wishare.finance.domains.bill.repository.BillAccountHandRepository;
import com.wishare.finance.domains.bill.repository.BillAccountHandRuleRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: 交账相关api
 * @author: pgq
 * @since: 2022/10/8 15:50
 * @version: 1.0.0
 */

@Api(tags = {"账单交账"})
@Validated
@RestController
@RequestMapping("/hand")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillHandApi {

    private final BillAppServiceFactory billAppServiceFactory;

    @GetMapping("/bill/list")
    @ApiOperation(value = "根据账单id获取交账信息列表", notes = "根据账单id获取交账信息列表")
    public List<BillHandV> listByBillIds(@RequestParam @ApiParam("账单id集合") List<Long> billIds, @RequestParam @ApiParam("账单类型") Integer billType
        ,@RequestParam(value = "supCpUnitId" ,required = false) @ApiParam("上级收费单元id")String supCpUnitId){
        return billAppServiceFactory.getBillServiceByBillType(billType).listBillHand(billIds,supCpUnitId);
    }

    @PostMapping("/reversal/{billType}/{billId}")
    @ApiOperation(value = "反交账", notes = "反交账")
    Boolean handReversal(@PathVariable("billId") Long billId, @PathVariable("billType") Integer billType
        ,@RequestParam(value = "supCpUnitId" ,required = false) @ApiParam("上级收费单元id")String supCpUnitId) {

        return billAppServiceFactory.getBillServiceByBillType(billType).handReversal(billId,supCpUnitId);
    }

    //------------------- 交账新逻辑 ----------------------//



    private final HandAccountAppService handAccountAppService;
    private final BillAccountHandRepository billAccountHandRepository;

    @PostMapping("/page")
    @ApiOperation(value = "查询交账分页列表", notes = "查询交账分页列表")
    public PageV<BillAccountHandPageV> getAccountHandPage(@Validated @RequestBody PageF<SearchF<?>> queryF){
        return RepositoryUtil.convertPage(billAccountHandRepository.getPageBySearch(queryF), BillAccountHandPageV.class);
    }

    @PostMapping("/domain")
    @ApiOperation(value = "发起交账", notes = "发起交账")
    public HandAccountDto handAccount(@Validated @RequestBody HandAccountF handAccountF){
        return handAccountAppService.handAccount(handAccountF.getBillId(), handAccountF.getBillType(),handAccountF.getSupCpUnitId());
    }

    @PostMapping("/batch")
    @ApiOperation(value = "发起批量交账", notes = "发起批量交账")
    public BillBatchResultDto batchHandAccount(@Validated @RequestBody BatchAccountHandF batchAccountHandF){
        return handAccountAppService.batchHandAccount(batchAccountHandF.getQuery(), batchAccountHandF.getBillIds(),batchAccountHandF.getSupCpUnitId());
    }

    @PostMapping("/reversal")
    @ApiOperation(value = "发起反交账", notes = "发起反交账")
    public Boolean reversal(@Validated @RequestBody ReversalHandAccountF handAccountF){
        return handAccountAppService.reversal(handAccountF.getBillId(), handAccountF.getBillType(), handAccountF.getSupCpUnitId());
    }

    @PostMapping("/total")
    @ApiOperation(value = "查询列表合计", notes = "查询列表合计")
    public AccountHandTotalDto getTotal(@Validated @RequestBody PageF<SearchF<?>> queryF){
        return billAccountHandRepository.getTotal(queryF);
    }

    @GetMapping("/rule/query/{ruleId}")
    @ApiOperation(value = "查询交账规则", notes = "查询交账规则")
    public BillHandAccountRuleV getHandRule(@PathVariable("ruleId") @ApiParam("交账规则id 没有值时默认传： -66") Long ruleId){
        return Global.mapperFacade.map(handAccountAppService.getHandAccountRuleBySpcId(ruleId), BillHandAccountRuleV.class);
    }

    @PostMapping("/rule/update")
    @ApiOperation(value = "更新交账规则", notes = "更新交账规则")
    public Boolean enableAutoHand(@Validated @RequestBody BillHandAccountRuleF billHandAccountRuleF){
        return handAccountAppService.updateHandAccountRule(Global.mapperFacade.map(billHandAccountRuleF, BillHandAccountRule.class));
    }

    @PostMapping("/refreshBill")
    @ApiOperation(value = "更新交账账单信息", notes = "更新交账账单信息")
    public void batchRefreshOrAddAccountHand(@RequestBody BatchRefreshAccountHandCommand batchRefreshAccountHandCommand){
        handAccountAppService.batchRefreshOrAddAccountHand(batchRefreshAccountHandCommand);
    }

}