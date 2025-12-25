package com.wishare.contract.apis.revision.pay;

import com.wishare.contract.apps.remote.fo.message.FirstExamineMessageF;
import com.wishare.contract.apps.service.contractset.ContractPayCostPlanService;
import com.wishare.contract.apps.service.revision.remind.ContractRemindMessageConfigService;
import com.wishare.contract.apps.service.revision.remind.ContractRemindMessageSendServiceImpl;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayCostPlanPageV;
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

import javax.annotation.Resource;

/**
 * @author wyt
 */
@RequiredArgsConstructor
@RestController
@Validated
@Api(tags = {"支付申请初审"})
@RequestMapping("/paymentApplication")
public class PaymentApplicationApi {

    @Resource
    private ContractRemindMessageConfigService contractRemindMessageConfigService;

    @ApiOperation(value = "支付申请初审通过")
    @PostMapping("/pass")
    public void pass(@RequestBody FirstExamineMessageF message) {
        contractRemindMessageConfigService.send(message, true);
    }

    @ApiOperation(value = "支付申请初审驳回")
    @PostMapping("/refuse")
    public void refuse(@RequestBody FirstExamineMessageF message) {
        contractRemindMessageConfigService.send(message, false);
    }
}
