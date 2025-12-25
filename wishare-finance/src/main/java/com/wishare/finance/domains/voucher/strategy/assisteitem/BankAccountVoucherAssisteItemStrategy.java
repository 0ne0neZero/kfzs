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
public class BankAccountVoucherAssisteItemStrategy implements VoucherAssisteItemStrategy {

    private final VoucherFacade voucherFacade;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.银行账户;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherBusinessBill businessBill) {
        return voucherFacade.getAssisteItem(String.valueOf(businessBill.getSbAccountId()), type());
    }


}
