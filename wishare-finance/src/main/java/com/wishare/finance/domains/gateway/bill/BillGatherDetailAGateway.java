package com.wishare.finance.domains.gateway.bill;

import com.wishare.finance.domains.bill.aggregate.BillGatherDetailA;
import com.wishare.finance.domains.bill.entity.Bill;

/**
 * BillGatherDetailA领域网关
 *
 * @author fengxiaolin
 * @date 2023/4/24
 */
public interface BillGatherDetailAGateway<B extends Bill> {

    /**
     * 保存BillGatherDetailA
     *
     * @param billGatherDetailA billGatherDetailA
     * @return true 保存成功， false 保存失败
     */
    Boolean saveBillGatherDetailA(BillGatherDetailA<B> billGatherDetailA);

}
