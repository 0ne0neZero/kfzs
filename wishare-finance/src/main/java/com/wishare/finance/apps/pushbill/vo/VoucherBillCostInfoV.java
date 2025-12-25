package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("对下结算单下的 成本明细")
public class VoucherBillCostInfoV {
    @ApiModelProperty("业务科目")
    private String subjectName;

    @ApiModelProperty("价税合计")
    private Double priceTaxAmount;

    @ApiModelProperty("可抵扣金额")
    private Double deductibleAmount;
}
