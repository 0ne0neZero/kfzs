package com.wishare.finance.apis.voucher;

import com.wishare.finance.apps.model.voucher.fo.CloseAccountF;
import com.wishare.finance.apps.model.voucher.fo.CloseAccountsF;
import com.wishare.finance.apps.model.voucher.vo.CloseAccountV;
import com.wishare.finance.apps.service.voucher.CloseAccountService;
import com.wishare.finance.domains.voucher.service.CloseAccountDomainService;
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

@Api(tags = {"关账管理"})
@RestController
@RequestMapping("/closeAccount")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CloseAccountApi {

    private final CloseAccountService closeAccountService;
    private final CloseAccountDomainService closeAccountDomainService;

    @PostMapping("/addCloseAccount")
    @ApiOperation(value = "新增关账记录")
    public Long addCloseAccount(@Validated @RequestBody CloseAccountF closeAccountF){
        return closeAccountService.addCloseAccount(closeAccountF);
    }

    @PostMapping("/closeAccountPageList")
    @ApiOperation(value = "关账列表查询")
    public PageV<CloseAccountV> closeAccountPageList(@RequestBody @Validated PageF<SearchF<?>> searchFPageF){
        return closeAccountDomainService.selectPageBySearch(searchFPageF);
    }

    @PostMapping("/closeAccountOrReversed")
    @ApiOperation(value = "关账/反关账")
    public boolean closeAccountOrReversed(@RequestBody @Validated CloseAccountF closeAccountF){
        return closeAccountService.operateCloseAccount(closeAccountF);
    }

    @PostMapping("/closeAccounts")
    @ApiOperation(value = "批量关账")
    public boolean closeAccounts(@Validated @RequestBody CloseAccountsF closeAccountsF){
        return closeAccountService.closeAccounts(closeAccountsF);
    }
}
