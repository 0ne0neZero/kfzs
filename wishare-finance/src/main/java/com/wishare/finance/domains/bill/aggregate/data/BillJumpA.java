package com.wishare.finance.domains.bill.aggregate.data;

import com.wishare.finance.domains.bill.aggregate.BillRefundA;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 跳收聚合
 *
 * @Author zhenghui
 * @Date 2023/7/16
 * @Version 1.0
 */
public class BillJumpA<B extends Bill> extends BillJumpE {

    /**
     * 账单信息
     */
    private B bill;



    /**
     * 账单审核记录
     */
    private BillApproveE billApprove;




    public BillJumpA() {
    }

    public BillJumpA(B bill) {
        this.bill = bill;
    }

    public BillJumpA(B bill, BillApproveE billApprove) {
        this.bill = bill;
        this.billApprove = billApprove;
    }

    public BillJumpA(B bill, BillApproveE billApprove, BillJumpE billJumpE) {
        this.bill = bill;
        this.billApprove = billApprove;
        if (Objects.nonNull(billJumpE)) {
            Global.mapperFacade.map(billJumpE, this);
        }
    }



    public B getBill() {
        return bill;
    }
}
