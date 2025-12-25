package com.wishare.finance.apps.pushbill.vo.dx;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wishare.finance.apps.service.pushbill.mdm63.BaseWriteOffObj;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "对下结算单-款项明细-前端展示数据")
public class DxPaymentDetails extends BaseWriteOffObj {

    @ApiModelProperty(value = "变动编号")
    private String changeCode;

    @ApiModelProperty(value = "变动")
    private String changeName;

    @ApiModelProperty(value = "款项id")
    private String paymentId;

    @ApiModelProperty(value = "款项编号")
    private String paymentCode;

    @ApiModelProperty(value = "对应的款项名称")
    private String correspondingPaymentName;

    @ApiModelProperty(value = "对应的款项id")
    private String correspondingPaymentId;

    @ApiModelProperty(value = "应付应收编号")
    private String numberOfPayableReceivable;

    @ApiModelProperty(value = "未核销金额")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal notSettlementAmount;

    @ApiModelProperty(value = "原币币种")
    private String originCurrencyType = "CNY-人民币";

    @ApiModelProperty(value = "汇率")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal exchangeRate = BigDecimal.valueOf(1.000000);

    @ApiModelProperty(value = "金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal originCurrencyAmount;

    @ApiModelProperty(value = "金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal standardAmount;

    @ApiModelProperty(value = "到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date maturityDate;

    @ApiModelProperty(value = "预计付款日期")
    private Date expectedPayDate;

    @ApiModelProperty(value = "往来单位-名称")
    private String exchangeUnitName;

    @ApiModelProperty(value = "往来单位-主数据编码")
    private String exchangeUnitMainCode;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "合同名称")
    private String contractId;

    @ApiModelProperty("中交合同编码-CT码")
    private String conMainCode;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "税率id")
    private String taxRateId;

    @ApiModelProperty(value = "税率对应计税方式编码")
    private String taxType;

    @ApiModelProperty(value = "税率对应计税方式名称")
    private String taxTypeName;

    @ApiModelProperty(value = "含税金额",required = true)
    private BigDecimal taxIncludedAmount;

    private String voucherBillNo;


}
