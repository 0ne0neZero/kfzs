package com.wishare.finance.apps.model.invoice.invoicebook.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("票本领用详情")
public class ReceiveDetailDto {

    @ApiModelProperty("票本id")
    private Long invoiceBookId;

    @ApiModelProperty("票本编号")
    private String invoiceBookNumber;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("可以领用的张数")
    private Long canReceiveQuantity;

    @ApiModelProperty("可以领用的起始发票号")
    private String canReceiveInvoiceStartNumber;

    @ApiModelProperty("购入组织id")
    private Long buyOrgId;

    @ApiModelProperty("购入组织名称")
    private String buyOrgName;

    @ApiModelProperty("购入日期")
    private LocalDate buyDate;
}
