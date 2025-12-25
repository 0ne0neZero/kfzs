package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Api(tags = {"法定单位管理"})
@RestController
@RequestMapping("/statutoryBody")
@RequiredArgsConstructor
public class StatutoryBodyApi {

    private final OrgClient orgClient;

    @ApiOperation(value = "查询法定单位", notes = "查询法定单位")
    @PostMapping("/query")
    public List<StatutoryBodyRv> query(@RequestBody OrgFinanceF orgFinanceF) {
        return orgClient.getOrgFinanceList(orgFinanceF);
    }

}
