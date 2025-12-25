package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.dto.GatherAndPayDto;
import com.wishare.finance.domains.bill.dto.GatherAndPayStatisticsDto;
import com.wishare.finance.domains.bill.repository.GatherAndPayRepository;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
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
public class GatherAndPayDomainService {

    private final GatherAndPayRepository gatherAndPayRepository;

    private final SharedBillAppService sharedBillAppService;

    /**
     * 分页查询收付款记录
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> queryPage(PageF<SearchF<?>> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "d." + BillSharedingColumn.收款明细.getColumnName());
        return gatherAndPayRepository.queryPage(form);
    }

    /**
     * 分页查询收付款记录(收付款单维度)
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> billQueryPage(PageF<SearchF<?>> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "gb." + BillSharedingColumn.收款明细.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(form, TableNames.GATHER_BILL, "gb." + BillSharedingColumn.收款明细.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(form, TableNames.GATHER_DETAIL, "gb." + BillSharedingColumn.收款明细.getColumnName());
        return gatherAndPayRepository.billQueryPage(form, gatherBillName,gatherDetailName);
    }

    /**
     * 分页查询收付款记录(收付款单维度无租户隔离)
     *
     * @param form
     * @return
     */
    public Page<GatherAndPayDto> billQueryPageIgnoreTenant(PageF<SearchF<?>> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "gb." + BillSharedingColumn.收款账单.getColumnName());
        PageQueryUtils.validQueryContainsFieldAndValue(form, "d." + BillSharedingColumn.收款明细.getColumnName());
        String gatherBillName = sharedBillAppService.getShareTableName(form, TableNames.GATHER_BILL, "gb." + BillSharedingColumn.收款账单.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(form, TableNames.GATHER_DETAIL, "d." + BillSharedingColumn.收款明细.getColumnName());
        return gatherAndPayRepository.billQueryPageIgnoreTenant(form, gatherBillName, gatherDetailName);
    }

    /**
     * 统计收付款记录
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statistics(SearchF<?> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "gb." + BillSharedingColumn.收款明细.getColumnName());
        return gatherAndPayRepository.statistics(form);
    }


    /**
     * 统计收付款记录(无租户隔离)
     *
     * @param form
     * @return
     */
    public GatherAndPayStatisticsDto statisticsIgnoreTenant(SearchF<?> form) {
        PageQueryUtils.validQueryContainsFieldAndValue(form, "d." + BillSharedingColumn.收款明细.getColumnName());
        String gatherDetailName = sharedBillAppService.getShareTableName(form, TableNames.GATHER_DETAIL, "d." + BillSharedingColumn.收款明细.getColumnName());
        return gatherAndPayRepository.statisticsIgnoreTenant(form,gatherDetailName);
    }
}
