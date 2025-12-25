package com.wishare.finance.domains.voucher.strategy.assisteitem;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务类型辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BizTypeVoucherAssisteItemStrategy implements VoucherAssisteItemStrategy {

    private final VoucherFacade voucherFacade;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.业务类型;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherBusinessBill businessBill) {
        AssisteItemOBV assisteItemOBV = new AssisteItemOBV();
        assisteItemOBV.setType(AssisteItemTypeEnum.业务类型.getCode());
        assisteItemOBV.setCode(businessBill.getBusinessCode());
        assisteItemOBV.setName(businessBill.getBusinessName());
        assisteItemOBV.setAscCode(AssisteItemTypeEnum.业务类型.getAscCode());
        assisteItemOBV.setAscName(AssisteItemTypeEnum.业务类型.name());
         return assisteItemOBV;
    }


}
