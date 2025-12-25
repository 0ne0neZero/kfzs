package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * @author xujian
 * @date 2022/9/28
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取收据详情入参")
public class ReceiptDetailF {


    @ApiModelProperty("票据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("票据id")
    private Long invoiceReceiptId;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

}
