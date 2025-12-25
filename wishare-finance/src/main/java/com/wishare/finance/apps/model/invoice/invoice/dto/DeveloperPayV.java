package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author szh
 * @date 2024/5/13 16:30
 */
@Data
public class DeveloperPayV {
    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("收据编号")
    private String receiptNo;

    @ApiModelProperty("项目名称")
    private String communityName;
    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("票据明细")
    private List<DeveloperPayDetailV> invoiceReceiptDetail;


    @ApiModelProperty("合计金额（单位：元）")
    private String invoiceAmountTotal;

    @ApiModelProperty("合计金额（单位：元）大写中文")
    private String invoiceAmountTotalUppercase;


    private String remark;







}
