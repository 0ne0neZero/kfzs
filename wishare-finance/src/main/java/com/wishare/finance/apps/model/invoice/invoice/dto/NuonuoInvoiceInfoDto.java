package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTitleTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/2/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票信息")
public class NuonuoInvoiceInfoDto {

    @ApiModelProperty("发票id")
    private Long invoiceReceiptId;

    @ApiModelProperty("诺诺开票url地址")
    private String nuonuoUrl;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败 8.部分红冲")
    private Integer state;

    @ApiModelProperty("失败原因")
    private String failReason;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("抬头类型")
    private Integer invoiceTitleType;

    @ApiModelProperty("抬头类型描述")
    private String invoiceTitleTypeStr;

    @ApiModelProperty("购方名称")
    private String buyerName;

    @ApiModelProperty("价税合计金额，发票金额")
    private Long priceTaxAmount;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    public String getInvoiceTitleTypeStr() {
        InvoiceTitleTypeEnum invoiceTitleTypeEnum = InvoiceTitleTypeEnum.valueOfByCode(invoiceTitleType);
        return invoiceTitleTypeEnum.getDes();
    }
}
