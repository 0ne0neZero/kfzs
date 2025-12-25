package com.wishare.finance.domains.bill.aggregate;

import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.helpers.UidHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * 退款聚合
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
@Getter
@Setter
public class BillRefundA<B extends Bill> extends BillRefundE {

    private B bill;

    public BillRefundA(B bill) {
        this.bill = bill;
    }

    /**
     * 发起退款
     * @return
     */
    public boolean refund(){
        setRefundTime(LocalDateTime.now());
        setRefundNo(StringUtils.isBlank(getRefundNo())? IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20) :getRefundNo());
        setState(RefundStateEnum.已退款.getCode());
        return bill.refund(getRefundAmount());
    }

}
