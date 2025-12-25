package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author: Linitly
 * @date: 2023/6/25 14:43
 * @descrption:
 */
@Data
@ApiModel(value = "机动车二手车明细")
public class InvoiceVehicleInfoResV {

    @ApiModelProperty(value = "车牌照号")
    private String licensePlate;

    @ApiModelProperty(value = "车辆类型")
    private String vehicleType;

    @ApiModelProperty(value = "登记证号")
    private String registryNo;

    @ApiModelProperty(value = "厂牌型号")
    private String brandModel;

    @ApiModelProperty(value = "转入地车辆管理所名")
    private String vehicleAdministration;

    @ApiModelProperty(value = "车辆识别代号/车架号码")
    private String vehicleNo;

    @ApiModelProperty(value = "车购税完税凭证号码")
    private String carPaymentVoucherNo;

    @ApiModelProperty(value = "开票标识：0未开票、1已开票")
    private String invoiceMark;

    @ApiModelProperty(value = "增值税标志：0免征增值税、1不征增值税")
    private String vatMark;

    @ApiModelProperty(value = "已开发票代码")
    private String issuedInvoiceCode;

    @ApiModelProperty(value = "已开发票号码")
    private String issuedInvoiceNo;

    @ApiModelProperty(value = "已开票销售额")
    private BigDecimal issuedTotalPrice;

    @ApiModelProperty(value = "已开票税额")
    private int issuedTotalTax;

    @ApiModelProperty(value = "已开票税率")
    private int issuedTaxRate;

    @ApiModelProperty(value = "开具完税证明标识")
    private String paymentVoucherMark;

    @ApiModelProperty(value = "完税凭证号码")
    private String paymentVoucherNo;

    @ApiModelProperty(value = "完税凭证销售额")
    private BigDecimal paymentVoucherToralPrice;

    @ApiModelProperty(value = "完税凭证税率")
    private int paymentVoucherTaxRate;

    @ApiModelProperty(value = "完税凭证税额")
    private int paymentVoucherTotalTax;

    // (总局固定19位编码)，不能修改 +自行编码，(以2位为一级 ，共10级，每级可用编码值为00-99 或AA-ZZ)
    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "增值税特殊管理")
    private String vatSpecialManagement;

    // 空代表无1 出口免税和其他免税优惠政策2 不征增值税3 普通零税率
    @ApiModelProperty(value = "零税率标识")
    private String freeTaxMark;

    @ApiModelProperty(value = "优惠政策标识：0:未使用，1:使用")
    private String preferentialMarkFlag;

    @ApiModelProperty(value = "卖方单位代码")
    private String sellerTaxNo;

    @ApiModelProperty(value = "卖方单位名称")
    private String sellerName;

    @ApiModelProperty(value = "卖方单位地址")
    private String sellerAddress;

    @ApiModelProperty(value = "卖方单位电话")
    private String sellerPhone;

    @ApiModelProperty(value = "产地")
    private String manufacturingEnterprise;

    @ApiModelProperty(value = "合格证号")
    private String certificateNo;

    @ApiModelProperty(value = "进口证明书号")
    private String importCertificateNo;

    @ApiModelProperty(value = "商检单号")
    private String inspectionListNo;

    @ApiModelProperty(value = "发动机号码")
    private String engineNo;

    @ApiModelProperty(value = "吨位")
    private String tonnage;

    @ApiModelProperty(value = "限乘人数")
    private String passengersLimited;

    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;

    @ApiModelProperty(value = "机动车新旧规范标识 0 旧规范 1 新规范 默认为0")
    private String vehicleSample;
}
