package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.apps.model.configure.organization.vo.FinanceOrganizationV;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.starter.Global;
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
 * 财务组织接口（法定单位，成本中心）
 * @author dxclay
 * @since  2023/4/7
 * @version 1.0
 */
@Api(tags = {"财务组织"})
@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class FinanceOrganizationApi {

    private final OrgClient orgClient;

    @PostMapping("/page")
    @ApiOperation("分页查询财务组织")
    PageV<FinanceOrganizationV> getOrgFinancePage(@RequestBody @Validated PageF<SearchF<?>> queryPageF){
        PageV<StatutoryBodyRv> orgFinancePage = orgClient.getOrgFinancePageV2(queryPageF);
        return PageV.of(queryPageF, orgFinancePage.getTotal(), Global.mapperFacade.mapAsList(orgFinancePage.getRecords(), FinanceOrganizationV.class));
    }

}
