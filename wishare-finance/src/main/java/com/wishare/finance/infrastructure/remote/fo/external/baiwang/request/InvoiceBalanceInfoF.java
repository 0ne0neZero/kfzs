package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Linitly
 * @date: 2023/6/19 20:56
 * @descrption:
 */
@Data
@ApiModel(value = "全电发票差额扣除额凭证明细")
public class InvoiceBalanceInfoF {

    // 必填，长度10
    @ApiModelProperty(value = "序号")
    private String balanceNo;

    // 必填，长度10
    @ApiModelProperty(value = "凭证类型：01 全电发票、02 增值税专用发票、03 增值税普通发票 \n" +
            "、04 营业税发票、05 财政票据、06 法院裁决书、07 契税完税凭证 \n" +
            "、08 其他发票类、09 其他扣除凭证")
    private String balanceType;

    // 非必填，长度20
    @ApiModelProperty(value = "全电发票号码：凭证类型为01时必填")
    private String balanceElectricNo;

    // 非必填，长度12
    @ApiModelProperty(value = "发票代码：凭证类型为02、03、04时必填")
    private String balanceInvoiceCode;

    // 非必填，长度8
    @ApiModelProperty(value = "发票号码：凭证类型为02、03、04时必填")
    private String balanceInvoiceNo;

    // 非必填，长度20
    @ApiModelProperty(value = "凭证号码")
    private String balanceVoucherNo;

    // 非必填，长度20
    @ApiModelProperty(value = "开具日期：凭证类型为01、02、03、04时必填")
    private String balanceIssueDate;

    // 必填
    @ApiModelProperty(value = "凭证合计金额")
    private BigDecimal balanceTotalAmount;

    // 必填
    @ApiModelProperty(value = "本次扣除金额")
    private BigDecimal balanceDeductAmount;

    // 非必填，长度100
    @ApiModelProperty(value = "备注")
    private String balanceRemarks;

    // 填，长度2
    @ApiModelProperty(value = "来源：默认传0")
    private String balanceSource;

}
