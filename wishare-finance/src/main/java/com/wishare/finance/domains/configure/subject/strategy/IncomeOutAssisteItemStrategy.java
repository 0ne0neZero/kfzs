package com.wishare.finance.domains.configure.subject.strategy;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收支明细项-辅助核算项
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IncomeOutAssisteItemStrategy implements AssisteItemStrategy {


    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.收支明细项;
    }

    @Override
    public List<AssisteItemOBV> list(String name, String code, String sbId) {
        return List.of(toAssisteItem(type(), "1009", "其他收入"),
                toAssisteItem(type(), "1014", "其他支出")
        );
    }

    /**
     * @param isCredit 贷方
     * @return
     */
    @Override
    public AssisteItemOBV getById(String type) {
        return VoucherLoanTypeEnum.贷方.getCode().equals(type) ?
                toAssisteItem(type(), "1009", "其他收入") :
                toAssisteItem(type(), "1014", "其他支出");
    }

}
