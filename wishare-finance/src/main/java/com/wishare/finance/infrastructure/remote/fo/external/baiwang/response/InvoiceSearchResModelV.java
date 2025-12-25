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
@ApiModel(value = "发票查询响应model")
public class InvoiceSearchResModelV {

    @ApiModelProperty(value = "开票流水号")
    private String serialNo;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "税盘号")
    private String machineNo;

    @ApiModelProperty(value = "设备类型：0税控服务器，1税控盘")
    private String deviceType;

    @ApiModelProperty(value = "购方纳税人识别号")
    private String buyerTaxNo;

    @ApiModelProperty(value = "购方单位名称")
    private String buyerName;

    @ApiModelProperty(value = "购方地址及电话")
    private String buyerAddressPhone;

    @ApiModelProperty(value = "购方开户行及账号")
    private String buyerBankAccount;

    @ApiModelProperty(value = "销方单位代码")
    private String sellerTaxNo;

    @ApiModelProperty(value = "销方单位名称")
    private String sellerName;

    @ApiModelProperty(value = "销方地址及电话")
    private String sellerAddressPhone;

    @ApiModelProperty(value = "销方开户行及账号")
    private String sellerBankAccount;

    @ApiModelProperty(value = "合计金额，保留两位小数")
    private BigDecimal invoiceTotalPrice;

    @ApiModelProperty(value = "合计税额，保留两位小数")
    private BigDecimal invoiceTotalTax;

    @ApiModelProperty(value = "价税合计，保留两位小数")
    private BigDecimal invoiceTotalPriceTax;

    @ApiModelProperty(value = "清单标志：0：无清单 1：带清单")
    private String invoiceListMark;

    @ApiModelProperty(value = "发票类型代码")
    private String invoiceTypeCode;

    @ApiModelProperty(value = "发票类型：0：正数；1：负数")
    private String invoiceType;

    @ApiModelProperty(value = "征收方式")
    private String taxationMethod;

    @ApiModelProperty(value = "特殊票种标志， 00：普通发票；01：农产品销售；02：农产 \n" +
            "品收购；08：成品油 12 机动车（默认是00普通发票）")
    private String invoiceSpecialMark;

    @ApiModelProperty(value = "开票点编码")
    private String invoiceTerminalCode;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;

    @ApiModelProperty(value = "税控码")
    private String taxControlCode;

    @ApiModelProperty(value = "扣除额，保留两位小数")
    private BigDecimal deductibleAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "开票人")
    private String drawer;

    @ApiModelProperty(value = "复核人")
    private String checker;

    @ApiModelProperty(value = "收款人")
    private String payee;

    @ApiModelProperty(value = "校验码")
    private String invoiceCheckCode;

    @ApiModelProperty(value = "二维码")
    private String invoiceQrCode;

    @ApiModelProperty(value = "购方客户邮箱")
    private String buyerEmail;

    @ApiModelProperty(value = "购方客户电话")
    private String buyerPhone;

    @ApiModelProperty(value = "电子发票下载地址")
    private String eInvoiceUrl;

    @ApiModelProperty(value = "电子发票预览地址")
    private String h5InvoiceUrl;

    @ApiModelProperty(value = "作废日期")
    private String invoiceInvalidDate;

    @ApiModelProperty(value = "发票状态")
    private String invoiceStatus;

    @ApiModelProperty(value = "验签标志：N00 未签名 Y00 已签名，未上传 Y11 验签成功 Y10 验签失败")
    private String invoiceCheckMark;

    @ApiModelProperty(value = "上传标志：1/0")
    private String invoiceUploadMark;

    @ApiModelProperty(value = "签名标志：Y/N")
    private String invoiceSignMark;

    @ApiModelProperty(value = "红字信息表编号")
    private String redInfoNo;

    @ApiModelProperty(value = "原发票代码")
    private String originalInvoiceCode;

    @ApiModelProperty(value = "原发票号码")
    private String originalInvoiceNo;

    @ApiModelProperty(value = "第三方系统id")
    private String systemId;

    @ApiModelProperty(value = "第三方系统名称")
    private String systemName;

    @ApiModelProperty(value = "来源标志 01 接口开具 02已开上传 03 开票申请单 04 流水单 \n" +
            "05 接口待开上传 06 销方待开导入 07 购方待开导入 08 非百 \n" +
            "望云开具 17 历史发票管理")
    private String sourceMark;

    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;

    @ApiModelProperty(value = "发票附加信息")
    private AttachInfoResV attachInfo;

    @ApiModelProperty(value = "发票详情")
    private List<InvoiceDetailF> invoiceDetailsList;

    @ApiModelProperty(value = "机动车二手车明细")
    private InvoiceVehicleInfoResV invoiceVehicleInfo;
}
