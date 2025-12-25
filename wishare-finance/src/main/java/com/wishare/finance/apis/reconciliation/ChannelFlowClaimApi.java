package com.wishare.finance.apis.reconciliation;

import com.wishare.finance.apps.model.reconciliation.fo.ChannelClaimF;
import com.wishare.finance.apps.model.reconciliation.fo.ChannelClaimFlowF;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimRecordPageV;
import com.wishare.finance.apps.model.reconciliation.vo.ChannelFlowClaimStatisticsV;
import com.wishare.finance.apps.service.bill.AllBillAppService;
import com.wishare.finance.apps.service.reconciliation.ChannelFlowClaimAppService;
import com.wishare.finance.domains.bill.dto.AllBillPageDto;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付流水认领
 */
@Api(tags = {"支付流水认领接口"})
@Validated
@RestController
@RequestMapping("/channelFlowClaim")
@RequiredArgsConstructor
public class ChannelFlowClaimApi {
    private final ChannelFlowClaimAppService channelFlowClaimAppService;
    private final AllBillAppService allBillAppService;

    @PostMapping("/page/record")
    @ApiOperation(value = "分页查询支付流水", notes = "分页查询支付流水")
    public PageV<ChannelFlowClaimRecordPageV> pageRecord(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return channelFlowClaimAppService.pageRecord(queryF);
    }


    @PostMapping("/statistics")
    @ApiOperation(value = "支付流水数据统计", notes = "支付流水数据统计")
    public ChannelFlowClaimStatisticsV getStatistics(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return channelFlowClaimAppService.getStatistics(queryF);
    }

    @PostMapping("/claim")
    @ApiOperation(value = "认领流水", notes = "认领流水")
    public Boolean claim(@Validated @RequestBody ChannelClaimFlowF claimFlowF) {
        return channelFlowClaimAppService.claim(claimFlowF);
    }

    @PostMapping("/flow/page/record")
    @ApiOperation(value = "分页查询账单数据用于支付流水认领", notes = "分页查询账单数据用于支付流水认领")
    public PageV<AllBillPageDto> channelFlowClaimPage(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return allBillAppService.channelFlowClaimPage(queryF);
    }


    @PostMapping("/YY/page/record")
    @ApiOperation(value = "分页查询支付流水", notes = "分页查询支付流水")
    public PageV<ChannelFlowClaimRecordPageV> getPageRecordYY(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return channelFlowClaimAppService.getPageRecordYY(queryF);
    }

    @PostMapping("/YY/statistics")
    @ApiOperation(value = "支付流水数据统计", notes = "支付流水数据统计")
    public ChannelFlowClaimStatisticsV getYYStatistics(@Validated @RequestBody PageF<SearchF<?>> queryF) {
        return channelFlowClaimAppService.getYYStatistics(queryF);
    }
    @PostMapping("/offline")
    @ApiOperation(value = "线下流水自动认领", notes = "线下流水自动认领")
    public Boolean channelClaim(@RequestBody ChannelClaimF channelClaimF){
        return channelFlowClaimAppService.channelClaimOffLine(channelClaimF);
    }



}
