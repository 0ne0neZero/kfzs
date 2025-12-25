package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("付款收票请求参数")
@Data
public class CollectionPlanPaymentInvoiceF {

    @ApiModelProperty(value = "合同id", required = true)
    @NotNull(message = "合同id不能为空")
    private Long contractId;

    @ApiModelProperty(value = "付款计划id", required = true)
    @NotNull(message = "付款计划id不能为空")
    private Long collectionPlanId;

    @ApiModelProperty(value = "付款类型 1 预付款 2结算收票", required = true)
    @NotNull(message = "付款类型不能为空")
    private Integer paymentType;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("本次收票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("本次付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty("付款方式  0现金  1银行转帐  2汇款  3支票")
    private Integer paymentMethod;

    @ApiModelProperty("备注")
    private String remark;
}
