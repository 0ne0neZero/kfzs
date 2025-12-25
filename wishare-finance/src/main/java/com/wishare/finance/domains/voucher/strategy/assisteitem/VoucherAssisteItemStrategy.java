package com.wishare.finance.domains.voucher.strategy.assisteitem;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

/**
 * 辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
public interface VoucherAssisteItemStrategy {

    /**
     * 辅助核算类型
     * @return
     */
    AssisteItemTypeEnum type();

    /**
     * 根据辅助核算id获取辅助核算信息
     * @param businessBill 凭证业务单据
     * @return
     */
    AssisteItemOBV getByBus(VoucherBusinessBill businessBill);


}
