package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.bill.vo.BillCarryV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.repository.mapper.BillCarryMapper;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryRepository
 * @date 2024.05.21  17:09
 * @description:账单结转
 */
@Service
public class BillCarryRepository extends ServiceImpl<BillCarryMapper, BillCarryoverE> {

    @Setter(onMethod_ = @Autowired)
    private SharedBillAppService sharedBillAppService;

    /**
     * 分页查询结转记录
     * @param page
     * @param wrapper
     * @return
     */
    public Page<BillCarryV> queryPageBySearch(Page<SearchF<?>> page, QueryWrapper<?> wrapper) {
       return baseMapper.queryPageBySearch(page,wrapper);
    }

    public Page<BillCarryV> queryCarryoverPage(Page<SearchF<?>> page, QueryWrapper<?> wrapper, String supCpUnitId) {
        String shareTableName = sharedBillAppService.getShareTableName(supCpUnitId, TableNames.TEMPORARY_CHARGE_BILL);
        return baseMapper.queryCarryoverPage(shareTableName, page, wrapper);
    }
}
