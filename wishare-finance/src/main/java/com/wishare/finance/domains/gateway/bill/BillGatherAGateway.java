package com.wishare.finance.domains.gateway.bill;

import com.wishare.finance.domains.bill.aggregate.BillGatherA;
import com.wishare.finance.domains.bill.entity.Bill;

/**
 * BillGatherA领域网关
 *
 * @author fengxiaolin
 * @date 2023/4/24
 */
public interface BillGatherAGateway<B extends Bill> {

    /**
     * 保存BillGatherDetailA
     *
     * @param billGatherDetailA billGatherDetailA
     * @return true 保存成功， false 保存失败
     */
    Long saveBillGatherA(BillGatherA<B> billGatherDetailA);
}
