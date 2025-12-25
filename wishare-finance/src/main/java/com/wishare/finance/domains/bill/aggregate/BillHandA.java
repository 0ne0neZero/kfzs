package com.wishare.finance.domains.bill.aggregate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.bill.consts.enums.BillAccountHandedStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillOnAccountEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillHandE;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 交账记录整合
 * @author: pgq
 * @since: 2022/10/9 15:31
 * @version: 1.0.0
 */
@Getter
@Setter
public class BillHandA<B extends Bill> extends BillHandE {

    /**
     * 账单信息
     */
    private B bill;

    public BillHandA() {
    }

    public BillHandA(B bill) {
        this.bill = bill;
    }

    public BillHandA initDefaultHand() {
        setBillId(bill.getId());
        setBillType(bill.getType());
        setTotalAmount(bill.getTotalAmount());
        return this;
    }

    /**
     * 交账
     * @param invoiceReceipts
     */
    public void hand(String invoiceReceipts) {

        // 交账状态根据结算状态  部门结算 -> 部分结账   已结算 -> 已结账
        if (bill.getSettleState() == BillSettleStateEnum.未结算.getCode() && bill.getOnAccount() == BillOnAccountEnum.已挂账.getCode()) {
            bill.setAccountHanded(BillAccountHandedStateEnum.已交账.getCode());
        } else {
            bill.setAccountHanded(bill.getSettleState());
        }

        setAccountHand(bill.getAccountHanded());

        if (StringUtils.isEmpty(invoiceReceipts)) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(invoiceReceipts);
        if (jsonObject.containsKey(bill.getId().toString())) {
            JSONArray array = jsonObject.getJSONArray(bill.getId().toString());

            setInvoiceReceipts(jsonObject.getString(bill.getId().toString()));
            if (array.isEmpty()) {
                return;
            }
            long handAmount = 0L;
            for (int i = 0; i < array.size(); i++) {
                JSONObject invoiceReceipt = array.getJSONObject(i);
                handAmount += invoiceReceipt.containsKey("settleAmount") ? invoiceReceipt.getLong("settleAmount") : 0L;
            }
            setHandAmount(handAmount);

        }
    }

    /**
     * 反交账
     * @return
     */
    public BillHandA handReversal() {
        setBillId(bill.getId());
        setBillType(bill.getType());
        setTotalAmount(bill.getTotalAmount());
        //setAccountHand(BillAccountHandedEnum.未交账.getCode());
        setHandAmount(bill.getSettleAmount() - bill.getRefundAmount() - bill.getCarriedAmount());
        bill.handReversal();
        return this;
    }
}
