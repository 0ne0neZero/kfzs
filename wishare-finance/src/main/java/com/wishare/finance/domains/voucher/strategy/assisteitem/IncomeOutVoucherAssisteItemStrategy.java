package com.wishare.finance.domains.voucher.strategy.assisteitem;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 存收支明细项-辅助核算项
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class IncomeOutVoucherAssisteItemStrategy implements VoucherAssisteItemStrategy {

    private final VoucherFacade voucherFacade;

    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.收支明细项;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherBusinessBill businessBill) {
        if (StrUtil.isBlank(businessBill.getType())){
            log.error("收支明细项获取辅助核算错误,入参:{}", JSONObject.toJSONString(businessBill));
            return voucherFacade.getAssisteItem(VoucherLoanTypeEnum.贷方.getCode(), type());
        }
        if (businessBill.getType().equals(VoucherLoanTypeEnum.贷方.getCode())){
            return voucherFacade.getAssisteItem(VoucherLoanTypeEnum.贷方.getCode(), type());
        }else {
            return voucherFacade.getAssisteItem(VoucherLoanTypeEnum.借方.getCode(), type());
        }

    }


}
