package com.wishare.finance.domains.reconciliation.support;

import com.wishare.finance.domains.reconciliation.entity.ReconcilePreconditionsOBV;
import com.wishare.finance.infrastructure.support.JSONListTypeHandler;

/**
 * 对账前置条件JSON List处理器
 *
 * @Author dxclay
 * @Date 2022/12/28
 * @Version 1.0
 */
public class ReconPreconditionsOBVJSONListTypeHandler extends JSONListTypeHandler<ReconcilePreconditionsOBV> {

    public ReconPreconditionsOBVJSONListTypeHandler(Class<?> type) {
        super(type, ReconcilePreconditionsOBV.class);
    }
}
