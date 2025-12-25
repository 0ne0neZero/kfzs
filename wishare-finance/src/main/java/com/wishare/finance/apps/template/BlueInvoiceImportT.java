package com.wishare.finance.apps.template;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.wishare.finance.apps.converter.ConverterLocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("蓝票导入")
public class BlueInvoiceImportT extends BaseExcelImport {

    @ExcelProperty(value = "项目名称")
    private String communityName;

    @ExcelIgnore
    private String communityId;

    @ExcelProperty(value = "发票号码")
    private String invoiceNo;

    @ExcelProperty(value = "发票代码")
    private String invoiceCode;

    @ExcelProperty(value = "票据类型")
    private String invoiceType;

    @ExcelProperty(value = "价税合计金额")
    private BigDecimal priceTaxAmount;

    @ExcelProperty(value = "税额")
    private BigDecimal taxAmount;
//
//    @ExcelProperty(value = "税率")
//    private String taxRate;

    @ExcelProperty(value = "开票时间", converter = ConverterLocalDateTime.class)
    private LocalDateTime billingTime;

    @ExcelProperty(value = "开票员")
    private String clerk;

    @ExcelProperty(value = "发票流水号（单据编号）")
    private String invoiceSerialNum;

    @ExcelProperty(value = "发票PDF地址")
    private String pdfUrl;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "发票抬头")
    private String invoiceTitleType;

    @ExcelProperty(value = "购方名称")
    private String buyerName;

    @ExcelProperty(value = "购方税号")
    private String buyerTaxNum;

    @ExcelProperty(value = "购方电话")
    private String buyerTel;

    @ExcelProperty(value = "购方地址")
    private String buyerAddress;

    @ExcelProperty(value = "购方银行开户行及账号")
    private String buyerAccount;

    @ExcelProperty(value = "销方税号")
    private String salerTaxNum;

    @ExcelProperty(value = "销方名称")
    private String salerName;

    @ExcelProperty(value = "销方电话")
    private String salerTel;

    @ExcelProperty(value = "销方地址")
    private String salerAddress;

    @ExcelProperty(value = "销方银行开户行及账号")
    private String salerAccount;

}
