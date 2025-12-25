package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票领用")
public class InvoiceReceiveDto {

    @ApiModelProperty("票本领用id")
    private Long id;

    @ApiModelProperty("领用票本id")
    private Long receiveInvoiceBookId;

    @ApiModelProperty("票本编号")
    private String invoiceBookNumber;

    @ApiModelProperty("票据类型：\n" +
            "1: 增值税普通发票\n" +
            "2: 增值税专用发票\n" +
            "3: 增值税电子发票\n" +
            "4: 增值税电子专票\n" +
            "5: 收据\n" +
            "6：电子收据\n" +
            "7:纸质收据")
    private Integer type;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("票本购入数量")
    private Long buyQuantity;

    @ApiModelProperty("领用数量")
    private Long receiveNumber;

    @ApiModelProperty("已使用数量")
    private Long usedNumber;

    @ApiModelProperty("发票起始号码")
    private Long invoiceStartNumber;

    @ApiModelProperty("发票结束号码")
    private Long invoiceEndNumber;

    @ApiModelProperty("领用时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("领用组织id")
    private Long receiveOrgId;

    @ApiModelProperty("领用组织名称")
    private String receiveOrgName;

    @ApiModelProperty("项目id")
    private String community;

    @ApiModelProperty("操作人名称")
    private String operatorName;

}
