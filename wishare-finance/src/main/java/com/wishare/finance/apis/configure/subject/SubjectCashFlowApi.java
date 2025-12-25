package com.wishare.finance.apis.configure.subject;

import com.wishare.finance.apps.model.configure.subject.fo.SaveSubjectCashFlowCodesF;
import com.wishare.finance.apps.model.configure.subject.fo.SaveSubjectCashFlowF;
import com.wishare.finance.apps.service.configure.subject.SubjectCashFlowAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: pgq
 * @since: 2023/2/24 15:08
 * @version: 1.0.0
 */
@Api(tags = {"科目现金流量"})
@RestController
@RequestMapping("/subject/cashflow")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SubjectCashFlowApi {

    private final SubjectCashFlowAppService subjectCashFlowAppService;

    @ApiOperation(value = "关联现金流量", notes = "关联现金流量")
    @PostMapping("/concat")
    public Boolean concatCashFlow(@RequestBody List<SaveSubjectCashFlowF> form) {
        return subjectCashFlowAppService.concatCashFlow(form);
    }

    @ApiOperation(value = "通过code关联现金流量", notes = "通过code关联现金流量")
    @PostMapping("/concatByCodes")
    public Boolean concatCashFlowByCodes(@RequestBody List<SaveSubjectCashFlowCodesF> form) {
        return subjectCashFlowAppService.concatCashFlowByCodes(form);
    }
}
