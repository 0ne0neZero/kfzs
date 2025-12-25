package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.vo.BillCarryV;
import com.wishare.finance.domains.bill.service.BillCarryDomainService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryAppService
 * @date 2024.05.21  17:05
 * @description:账单结转
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillCarryAppService {

    private final BillCarryDomainService billCarryDomainService;

    /**
     * 分页获取结转记录
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillCarryV}</>
     */
    public PageV<BillCarryV> getBillCarryPage(PageF<SearchF<?>> queryF) {
        return billCarryDomainService.getBillCarryPage(queryF);
    }

    /**
     * 分页获取结转记录包含临时账单
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillCarryV}</>
     */
    public PageV<BillCarryV> queryCarryoverPage(PageF<SearchF<?>> queryF) {
        return billCarryDomainService.queryCarryoverPage(queryF);
    }
}
