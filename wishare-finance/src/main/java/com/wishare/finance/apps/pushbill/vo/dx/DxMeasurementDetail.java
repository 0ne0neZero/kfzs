package com.wishare.finance.apps.pushbill.vo.dx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "对下结算单-计量清单-前端展示数据")
public class DxMeasurementDetail {

    @ApiModelProperty(value = "原币币种")
    private String originCurrencyType = "CNY-人民币";

    @ApiModelProperty(value = "汇率")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal exchangeRate = BigDecimal.ONE.setScale(6);

    @ApiModelProperty(value = "税率")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal taxRate;

    @ApiModelProperty(value = "含税金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal originAmountWithTax;
    @ApiModelProperty(value = "含税金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal standardAmountWithTax;

    @ApiModelProperty(value = "税额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal originTaxAmount;
    @ApiModelProperty(value = "税额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal standardTaxAmount;

    @ApiModelProperty(value = "不含税金额-原币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal originAmountWithoutTax;
    @ApiModelProperty(value = "不含税金额-本位币")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal standardAmountWithoutTax;




}
