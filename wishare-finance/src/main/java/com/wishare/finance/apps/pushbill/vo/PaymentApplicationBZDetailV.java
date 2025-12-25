package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报账明细
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
public class PaymentApplicationBZDetailV {


    @ApiModelProperty(value = "单据明细id")
    private Long id;
    @ApiModelProperty(value = "单据编号")
    private String voucherBillDetailNo;
    @ApiModelProperty(value = "单据编号")
    private String billNO;
    @ApiModelProperty(value = "收款单No")
    private String gatherBillNo;
    @ApiModelProperty(value = "账单类型 1:应收账单，2:预收账单，3:临时收费账单")
    private Integer billType;
    @ApiModelProperty(value = "项目id")
    private String communityId;
    @ApiModelProperty(value = "所属项目")
    private String communityName;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;
    @ApiModelProperty(value = "税率id")
    private Long taxRateId;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxIncludAmount;
    @ApiModelProperty(value = "不含税金额")
    private BigDecimal taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;
    @ApiModelProperty(value = "计提状态")
    private String provisionStatus;
    @ApiModelProperty(value = "实签状态")
    private String receiptConfirmationStatus;

}
