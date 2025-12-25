package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author ℳ๓采韵
 * @project wishare-charge
 * @title GatherBillDetailListV
 * @date 2023.12.12 18:56
 * @description 收款明细单列表
 */
@Getter
@Setter
@ApiModel("收款明细单列表")
public class InvoiceGatherBillDetailListV {

    @ApiModelProperty("收款单明细id")
    private Long gatherDetailBillId;

    @ApiModelProperty("是否免税：0不免税，1免税， 默认不免税")
    private Integer freeTax;

    @ApiModelProperty("价税合计（开票金额含税）")
    private Long priceTaxAmount;

}

