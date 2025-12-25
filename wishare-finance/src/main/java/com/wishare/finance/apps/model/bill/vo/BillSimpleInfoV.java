package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/10/13
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取可以开票信息")
public class BillSimpleInfoV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("可开票金额")
    private Long canInvoiceAmount;

    @ApiModelProperty("账单数量")
    private Integer billNum;

    @ApiModelProperty("应收金额之和")
    private Long receivableAmountSum;

    @ApiModelProperty("实收金额之和")
    private Long actualPayAmountSum;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;
}
