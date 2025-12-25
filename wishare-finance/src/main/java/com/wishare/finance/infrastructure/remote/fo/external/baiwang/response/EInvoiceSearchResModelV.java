package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.InvoiceDetailF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: Linitly
 * @date: 2023/6/25 14:15
 * @descrption:
 */
@Data
@ApiModel(value = "全电发票查询响应model")
public class EInvoiceSearchResModelV {

    @ApiModelProperty(value = "开票流水号")
    private String serialNo;

    @ApiModelProperty(value = "发票类型代码")
    private String invoiceTypeCode;

    @ApiModelProperty(value = "00：普通发票 01：成品油发票 02：稀土发票 03：建筑服务发票\n" +
            "04：货物运输服务发票 05：不动产销售服务发票 06：不动产租赁服 \n" +
            "务发票 07：代收车船税 08：通行费 09：旅客运输服务发票 10：医疗 \n" +
            "服务（住院）发票 11：医疗服务（门诊）发票 12：自产农产品销售 \n" +
            "发票 13 ：拖拉机和联合收割机发票 14：机动车 15：二手车 16：农产 \n" +
            "品收购发票 17：光伏收购发票 18：卷烟发票")
    private String invoiceSpecialMark;

    @ApiModelProperty(value = "销方单位税号")
    private String sellerTaxNo;

    @ApiModelProperty(value = "销方单位名称")
    private String sellerName;

    @ApiModelProperty(value = "销方地址")
    private String sellerAddress;

    @ApiModelProperty(value = "销方电话")
    private String sellerTelphone;

    @ApiModelProperty(value = "销方开户行")
    private String sellerBankName;

    @ApiModelProperty(value = "销方银行账号")
    private String sellerBankNumber;

    @ApiModelProperty(value = "购方单位税号")
    private String buyerTaxNo;

    @ApiModelProperty(value = "购方单位名称")
    private String buyerName;

    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    @ApiModelProperty(value = "购房电话")
    private String buyerTelphone;

    @ApiModelProperty(value = "购方开户行")
    private String buyerBankName;

    @ApiModelProperty(value = "购方银行账号")
    private String buyerBankNumber;

    @ApiModelProperty(value = "开票人")
    private String drawer;

    @ApiModelProperty(value = "发票类型：0：正数；1：负数")
    private String invoiceType;

    @ApiModelProperty(value = "全电发票号码")
    private String einvoiceNo;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;

    @ApiModelProperty(value = "第三方系统id")
    private String systemId;

    @ApiModelProperty(value = "第三方系统名称")
    private String systemName;

    @ApiModelProperty(value = "购方客户邮箱")
    private String buyerEmail;

    @ApiModelProperty(value = "购方客户电话")
    private String buyerPhone;

    @ApiModelProperty(value = "作废原因")
    private String invoiceInvalidReason;

    @ApiModelProperty(value = "作废人")
    private String invoiceInvalidOperator;

    @ApiModelProperty(value = "作废日期")
    private String invoiceInvalidDate;

    @ApiModelProperty(value = "发票状态：00：开具成功 02：空白发票作废 03：已开发票作废 05：正票全额红冲 06：正票部分红冲")
    private String invoiceStatus;

    @ApiModelProperty(value = "清单标志：0：无清单 1：带清单")
    private String invoiceListMark;

    @ApiModelProperty(value = "全电红字确认单编号")
    private String redInfoNo;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "原全电发票号码")
    private String originalEinvoiceNo;

    @ApiModelProperty(value = "征收方式")
    private String taxationMethod;

    @ApiModelProperty(value = "差额征税类型代码：01:差额征税-差额开具，02:差额征税-全额开具")
    private String deductibleType;

    @ApiModelProperty(value = "扣除额，保留两位小数")
    private BigDecimal deductibleAmount;

    @ApiModelProperty(value = "合计金额，保留两位小数")
    private BigDecimal invoiceTotalPrice;

    @ApiModelProperty(value = "合计税额，保留两位小数")
    private BigDecimal invoiceTotalTax;

    @ApiModelProperty(value = "价税合计，保留两位小数")
    private BigDecimal invoiceTotalPriceTax;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "购买方证件号码")
    private String credentialsNo;

    @ApiModelProperty(value = "来源标志 01 接口开具 02已开上传 03 开票申请单 04 流水单 \n" +
            "05 接口待开上传 06 销方待开导入 07 购方待开导入 08 非百 \n" +
            "望云开具 17 历史发票管理")
    private String sourceMark;

    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;

    @ApiModelProperty(value = "发票详情")
    private List<InvoiceDetailF> invoiceDetailsList;

    @ApiModelProperty(value = "特定业务信息")
    private List<InvoiceSpecialInfoResV> invoiceSpecialInfoList;
}
