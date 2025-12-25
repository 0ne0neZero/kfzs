package com.wishare.finance.apis.yuanyang;

import com.wishare.finance.apps.model.bill.vo.OrgFinanceCostInfoV;
import com.wishare.finance.apps.service.yuanyang.OrgFinanceCostAppService;
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

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title OrgFinanceCostApi
 * @date 2024.06.18  11:09
 * @description
 */
@Api(tags = {"成本中心"})
@Validated
@RestController
@RequestMapping("/orgFinanceCost")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrgFinanceCostApi {

    private final OrgFinanceCostAppService orgFinanceCostAppService;

    @PostMapping("/page")
    @ApiOperation(value = "分页获取成本中心信息", notes = "分页获取成本中心信息")
    public PageV<OrgFinanceCostInfoV> getPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return orgFinanceCostAppService.getPage(queryF);
    }
}
