package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:47
 * @descrption:
 */
@Data
@ApiModel(value = "红字确认请求体")
public class RedConfirmReqF {

    // 必填，长度20
    @ApiModelProperty(value = "税号")
    private String taxNo = "";

    // 必填，长度60
    @ApiModelProperty(value = "红字确认单流水号,调用方传递")
    private String redConfirmSerialNo = "";

    // 必填，长度2
    @ApiModelProperty(value = "录入方身份 01:销方,02:购方")
    private String entryIdentity = "";

    // 必填，长度20
    @ApiModelProperty(value = "销售方统一社会信用代码/纳税人识别号/身份证件号码")
    private String sellerTaxNo = "";

    // 必填，长度300
    @ApiModelProperty(value = "销售方名称")
    private String sellerTaxName = "";

    // 非必填，长度20
    @ApiModelProperty(value = "购买方统一社会信用代码/纳税人识别号/身份证件号码, 录入方身份为【购方】时必填")
    private String buyerTaxNo = "";

    // 必填，长度300
    @ApiModelProperty(value = "购买方名称")
    private String buyerTaxName = "";

    // 必填，长度2
    @ApiModelProperty(value = "是否纸质发票标志 Y：纸质发票 N：电子发票")
    private String originInvoiceIsPaper = "";

    // 非必填，长度20
    @ApiModelProperty(value = "蓝字发票全电发票号码，【发票来源】为2时必填")
    private String originalInvoiceNo = "";

    // 非必填，长度12
    @ApiModelProperty(value = "纸质、税控发票代码，【发票来源】为1时或全电纸票时必填")
    private String originalPaperInvoiceCode = "";

    // 非必填，长度8
    @ApiModelProperty(value = "纸质、税控发票号码，【发票来源】为1时或全电纸票时必填")
    private String	originalPaperInvoiceNo = "";

    // 必填，长度19
    @ApiModelProperty(value = "蓝字发票开票日期 yyyy-MM-dd HH:mm:ss")
    private String originInvoiceDate = "";

    // 必填
    @ApiModelProperty(value = "蓝字发票合计金额")
    private BigDecimal originInvoiceTotalPrice = BigDecimal.ZERO;

    // 必填
    @ApiModelProperty(value = "蓝字发票合计税额")
    private BigDecimal originInvoiceTotalTax = BigDecimal.ZERO;

    // 必填，长度2
    @ApiModelProperty(value = "蓝字发票票种代码 01:增值税专用发票 02:普通发票 03:机动车统一销售发票 04:二手车统一销售发票")
    private String originInvoiceType = "";

    // 非必填，长度2
    @ApiModelProperty(value = "蓝字发票特定要素类型代码 01：成品油发票 02：稀土发 \n" +
            "票 03：建筑服务发票 04：货物运输服务发票 05：不动 \n" +
            "产销售服务发票 06：不动产租赁服务发票 07：代收车船 \n" +
            "税 08：通行费 09：旅客运输服务发票 10：医疗服务 \n" +
            "（住院）发票 11：医疗服务（门诊）发票 12：自产农产 \n" +
            "品销售发票 13 拖拉机和联合收割机发票 14：机动车 \n" +
            "15：二手车 16：农产品收购发票 17：光伏收购发票\n" +
            "18：卷烟发票")
    private String originInvoiceSetCode = "";

    // 必填
    @ApiModelProperty(value = "红字冲销金额")
    private BigDecimal invoiceTotalPrice = BigDecimal.ZERO;

    // 必填
    @ApiModelProperty(value = "红字冲销税额")
    private BigDecimal invoiceTotalTax = BigDecimal.ZERO;

    // 必填，长度2
    @ApiModelProperty(value = "红字发票冲红原因代码 01:开票有误 02:销货退回 03:服务中止 04:销售折让")
    private String redInvoiceLabel = "";

    // 必填，长度2
    @ApiModelProperty(value = "发票来源：全电平台红冲必须要传递的字段 " +
            "1:增值税发票管理系统：表示此发票是通过原税控系统开具的增值税发票，红冲此类发票时，税控设备需注销后才可以申请全电的红字确认单； " +
            "2:电子发票服务平台：表示此发票是通过电子发票服务平台开具的全电发票（包括全电纸质发票），红冲此类发票时需要传递蓝票属性为此；")
    private String invoiceSource = "";

    // 必填
    @ApiModelProperty(value = "明细信息")
    private List<RedConfirmDetailReqF> redConfirmDetailReqEntityList;
}
