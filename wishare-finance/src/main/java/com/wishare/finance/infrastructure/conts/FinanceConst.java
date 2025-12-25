package com.wishare.finance.infrastructure.conts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title FinanceConst
 * @date 2023.11.27  19:10
 * @description 财务中台常量
 */
public interface FinanceConst {

    String SERVER_NAME = "wishare-finance";

    Map<String, String> MONEY_FIELD_MAP = new HashMap<>() {
        {
            put("receivableAmountTotal", "receivableAmountTotal");
            put("deductibleAmountTotal", "deductibleAmountTotal");
            put("discountAmountTotal", "discountAmountTotal");
            put("settleAmountTotal", "settleAmountTotal");
            put("amountTotal", "amountTotal");
            put("refundAmountTotal", "refundAmountTotal");
            put("overdueAmountTotal", "overdueAmountTotal");
            put("totalAmount", "totalAmount");
            put("receivableAmount", "receivableAmount");
            put("deductibleAmount", "deductibleAmount");
            put("overdueAmount", "overdueAmount");
            put("discountAmount", "discountAmount");
            put("settleAmount", "settleAmount");
            put("unitPrice", "unitPrice");
            put("payAmount", "payAmount");
            put("advanceAmount", "advanceAmount");
            put("refundAmount", "refundAmount");
            put("carryoverAmount", "carryoverAmount");
            put("carriedAmount", "carriedAmount");
            put("adjustAmount", "adjustAmount");
            put("priceTaxAmount", "priceTaxAmount");
            put("actualUnpayAmount", "actualUnpayAmount");
            put("actualPayAmount", "actualPayAmount");
            put("carriedAmountTotal", "carriedAmountTotal");
            put("actualPayAmountTotal", "actualPayAmountTotal");
            put("amountOwed", "amountOwed");
            put("flowAmount", "flowAmount");
            put("invoiceAmount","invoiceAmount");
            put("recPayAmount","recPayAmount");
            put("amount", "amount");
            put("cashAmount", "cashAmount");
            put("remitAmount", "remitAmount");
            put("swipingCardAmount", "swipingCardAmount");
            put("chequeAmount", "chequeAmount");
            put("otherAmount", "otherAmount");
            put("bankPosAmount", "bankPosAmount");
            put("bankUnionPosAmount", "bankUnionPosAmount");
            put("propertyReductionAmount", "propertyReductionAmount");
            put("remainingCarriedAmount", "remainingCarriedAmount");
            put("unionPayPOSAmount", "unionPayPOSAmount");
            put("offlineAlipayAmount", "offlineAlipayAmount");
            put("offlineWeChatAmount", "offlineWeChatAmount");
            put("jumpAccount","jumpAccount");
            put("actualPayAmountSum", "actualPayAmountSum");
        }
    };
}
