package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("资金收款单下的应收款明细")
public class VoucherBillZJRecDetailV {

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同CT码")
    private String conMainCode;

    @ApiModelProperty("合同编号 有合同时=CT码 无合同时=9999999999")
    private String contractNo = "9999999999";
    @ApiModelProperty("合同名称")
    private String contractName = "不适用";

    @ApiModelProperty("合同付款人 有合同时=往来单位名称 无合同时=其他")
    private String contractPay = "其他";
    @ApiModelProperty("税率id-无合同才有意义")
    private Long taxRateId;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    @ApiModelProperty("计税方式")
    private String taxType = "一般计税";

    @ApiModelProperty("业务科目")
    private String subjectName;

    @ApiModelProperty("业务科目来源id")
    private String subjectIdExt;

    @ApiModelProperty("含税金额")
    private BigDecimal taxIncludAmount;

}
