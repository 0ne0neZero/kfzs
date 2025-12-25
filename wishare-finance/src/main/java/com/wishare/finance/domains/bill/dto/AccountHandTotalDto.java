package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 交账合计数据对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Getter
@Setter
@ApiModel("交账合计信息")
public class AccountHandTotalDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "收费单元数量")
    private String cpUnitCount;

    @ApiModelProperty(value = "收款金额")
    private Long payAmount;

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount;

    @ApiModelProperty(value = "票据总额")
    private Long invoiceTotalAmount;

    @ApiModelProperty(value = "实收金额")
    private Long totalAmount;

}
