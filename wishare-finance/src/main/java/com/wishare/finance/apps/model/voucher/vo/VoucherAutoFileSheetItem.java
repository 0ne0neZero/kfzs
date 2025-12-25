package com.wishare.finance.apps.model.voucher.vo;

import lombok.Data;

/**
 * @author longhuadmin
 */
@Data
public class VoucherAutoFileSheetItem {

    private String contractName;
    private String contractNo;
    private String opposite;
    private String endDate;
    private String contractAmount;
    private String chargeItemName;
    private String preFlagName;
    private String curAmount;
    private String curTaxAmount;
    private String curNoTaxAmount;
    private String startTime;
    private String accountDate;
}
