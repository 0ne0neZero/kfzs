package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 增值税逻辑策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxRateLogicStrategy implements LogicStrategy {

    private final VoucherFacade voucherFacade;

    @Override
    public VoucherTemplateLogicCodeEnum logicCode() {
        return VoucherTemplateLogicCodeEnum.增值税税率;
    }

    @Override
    public BigDecimal logicValue(VoucherBusinessBill businessBill) {
        BigDecimal taxRate = businessBill.getTaxRate();
        if (Objects.isNull(taxRate)){
            AssisteItemOBV assisteItem = voucherFacade.getAssisteItem(String.valueOf(businessBill.getTaxRateId()), AssisteItemTypeEnum.增值税税率);
            String name = assisteItem.getName();
            taxRate = StringUtils.isBlank(name) ? BigDecimal.ZERO :
                    new BigDecimal(name.replaceAll("%", ""))
                            .divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
        }
        return taxRate;
    }

}
