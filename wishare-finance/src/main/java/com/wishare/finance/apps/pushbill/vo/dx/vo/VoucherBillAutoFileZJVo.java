package com.wishare.finance.apps.pushbill.vo.dx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
public class VoucherBillAutoFileZJVo {

    private String id;

    private String contractId;

    private String chargeItemId;

    private String chargeItemName;

    private BigDecimal amount;

    private BigDecimal noTaxAmount;

    private BigDecimal taxAmount;

    private Date startTime;

    private Date endTime;

    private String accountDate;

}
