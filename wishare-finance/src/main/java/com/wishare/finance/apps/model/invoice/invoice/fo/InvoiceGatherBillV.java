package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author Hello
 */
@Getter
@Setter
@ApiModel("开票收款单信息")
public class InvoiceGatherBillV {

    @ApiModelProperty(value = "所属项目")
    private String supCpUnitName;

    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;

    @ApiModelProperty(value = "房号")
    private String cpUnitName;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "开票金额（单位：元）")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "计费周期")
    private String expensePeriod;


}
