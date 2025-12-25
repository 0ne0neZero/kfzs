package com.wishare.finance.apis.configure.cashflow;

import com.wishare.finance.apps.model.configure.cashflow.fo.CreateCashFlowF;
import com.wishare.finance.apps.service.configure.cashflow.CashFlowAppService;
import com.wishare.finance.domains.configure.cashflow.comamnd.CashFlowDtoQuery;
import com.wishare.finance.domains.configure.cashflow.dto.CashFlowDto;
import com.wishare.finance.domains.configure.cashflow.repository.CashFlowRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 现金流量接口
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/14
 */
@Api(tags = {"现金流量"})
@RestController
@RequestMapping("/cashflow")
@RequiredArgsConstructor
public class CashFlowApi {

    private final CashFlowRepository cashFlowRepository;
    private final CashFlowAppService cashFlowAppService;

    @ApiOperation(value = "同步现金流量", notes = "同步现金流量")
    @PostMapping("/sync")
    public Boolean syncCashFlow(@RequestBody @Validated @Size(max = 200, message = "单次同步现金流量大小不允许超过200条") List<CreateCashFlowF> cashFlowFS) {
        return cashFlowAppService.syncCashFlow(cashFlowFS);
    }

    @ApiOperation(value = "查询现金流量列表")
    @PostMapping("/list")
    public List<CashFlowDto> getCashFlowList(@RequestBody CashFlowDtoQuery query){
        return cashFlowRepository.getDtoByCodeName(query);
    }


}
