package com.wishare.finance.apps.service.bill;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.GatherAndPayDto;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.service.GatherAndPayDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GatherAndPayAppService {

    private final GatherAndPayDomainService gatherAndPayDomainService;

    /**
     * 分页查询收付款记录
     *
     * @param form
     * @return
     */
    public PageV<GatherAndPayDto> queryPage(PageF<SearchF<?>> form) {
        Page<GatherAndPayDto> pageResult = gatherAndPayDomainService.queryPage(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), GatherAndPayDto.class));
    }

    /**
     * 分页查询收付款记录(收付款单维度)
     *
     * @param form
     * @return
     */
    public PageV<GatherAndPayDto> billQueryPage(PageF<SearchF<?>> form) {
        Page<GatherAndPayDto> pageResult = gatherAndPayDomainService.billQueryPage(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), GatherAndPayDto.class));
    }

    /**
     * 分页查询收付款记录(收付款单维度无租户隔离)
     *
     * @param form
     * @return
     */
    public PageV<GatherAndPayDto> billQueryPageIgnoreTenant(PageF<SearchF<?>> form) {
        Page<GatherAndPayDto> pageResult = gatherAndPayDomainService.billQueryPageIgnoreTenant(form);
        return PageV.of(form, pageResult.getTotal(), Global.mapperFacade.mapAsList(pageResult.getRecords(), GatherAndPayDto.class));

    }

    /**
     * 统计收付款记录
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statistics(SearchF<?> form) {
        return gatherAndPayDomainService.statistics(form);
    }

    /**
     * 统计收付款记录(无租户隔离)
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statisticsIgnoreTenant(SearchF<?> form) {
        return  gatherAndPayDomainService.statisticsIgnoreTenant(form);
    }
}
