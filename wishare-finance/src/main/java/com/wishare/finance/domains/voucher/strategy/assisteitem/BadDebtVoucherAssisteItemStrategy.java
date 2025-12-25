package com.wishare.finance.domains.voucher.strategy.assisteitem;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 银行账户辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BadDebtVoucherAssisteItemStrategy implements VoucherAssisteItemStrategy {

    private final VoucherFacade voucherFacade;

    public static final String DEFAULT_CODE = "202";
    public static final String DEFAULT_NAME = "资产核销减少";

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.坏账准备;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherBusinessBill businessBill) {
        return new AssisteItemOBV(type(), DEFAULT_CODE, DEFAULT_NAME);
    }


}
