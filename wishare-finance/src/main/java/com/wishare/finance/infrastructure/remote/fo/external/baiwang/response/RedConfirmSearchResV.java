package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import com.wishare.finance.infrastructure.remote.fo.external.baiwang.request.RedConfirmDetailReqF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 11:34
 * @descrption: list
 */
@Data
@ApiModel(value = "红字查询响应")
public class RedConfirmSearchResV {

    @ApiModelProperty(value = "红字确认单UUID")
    private String redConfirmUuid;

    @ApiModelProperty(value = "红字确认单编号")
    private String redConfirmNo;

    @ApiModelProperty(value = "红字确认单流水号")
    private String redConfirmSerialNo;

    @ApiModelProperty(value = "录入方身份 01:销方,02:购方")
    private String entryIdentity;

    @ApiModelProperty(value = "销售方统一社会信用代码/纳税人识别号/身份证件号码")
    private String sellerTaxNo;

    @ApiModelProperty(value = "销售方名称")
    private String sellerName;

    @ApiModelProperty(value = "购买方统一社会信用代码/纳税人识别号/身份证件号码")
    private String buyerTaxNo;

    @ApiModelProperty(value = "购买方名称")
    private String buyerName;

    @ApiModelProperty(value = "纸质、税控发票代码")
    private String originalPaperInvoiceCode;

    @ApiModelProperty(value = "纸质、税控发票号码")
    private String originalPaperInvoiceNo;

    @ApiModelProperty(value = "全电蓝字发票号码")
    private String originalInvoiceNo;

    @ApiModelProperty(value = "是否纸质发票标志 Y :纸质发票 N:电子发票")
    private String originInvoiceIsPaper;

    @ApiModelProperty(value = "蓝字发票开票日期 yyyy-MM-dd HH:mm:ss")
    private String originInvoiceDate;

    @ApiModelProperty(value = "蓝字发票金额")
    private BigDecimal originInvoiceTotalPrice;

    @ApiModelProperty(value = "蓝字发票税额")
    private BigDecimal originInvoiceTotalTax;

    @ApiModelProperty(value = "蓝字发票票种代码 01: 增值税专用发票 02: 普通发票 03: 机动车统一销售发票 04: 二手车统一销售发票")
    private String originInvoiceType;

    @ApiModelProperty(value = "蓝字发票特定要素类型代码 01：成品油发票 \n" +
            "02：稀土发票 03：建筑服务发票 04：货物运输服务发票 05：不动产销售服务发票 06：不动产租赁 \n" +
            "服务发票 07：代收车船税 08：通行费 09：旅客 \n" +
            "运输服务发票 10：医疗服务（住院）发票 11：医 \n" +
            "疗服务（门诊）发票 12：自产农产品销售发票 13 \n" +
            "拖拉机和联合收割机发票 14：机动车 15：二手车 \n" +
            "16：农产品收购发票 17：光伏收购发票 18：卷烟 \n" +
            "发票")
    private String originInvoiceSetCode;

    @ApiModelProperty(value = "红字冲销金额")
    private BigDecimal invoiceTotalPrice;

    @ApiModelProperty(value = "红字冲销税额")
    private BigDecimal invoiceTotalTax;

    @ApiModelProperty(value = "红字发票冲红原因代码 01：开票有误 02：销货退回 03：服务中止 04：销售折让")
    private String redInvoiceLabel;

    @ApiModelProperty(value = "红字确认单状态 01：无需确认； 02：销方录入待购方确认； 03：购方录入待销方确认； " +
            "04：购销双方已确认； 05：作废（销方录入购方否认）； \n" +
            "06：作废（购方录入销方否认）； 07：作废（超72小时未确认）； 08：作废（发起方已撤销）； \n" +
            "09：作废（确认后撤销）；10： 作废（异常凭证）")
    private String confirmState;

    @ApiModelProperty(value = "确认日期 yyyy-MM-dd HH:mm:ss")
    private String confirmDate;

    @ApiModelProperty(value = "已开具红字发票标记 Y:已开具,N:未开具")
    private String alreadyRedInvoiceFlag;

    @ApiModelProperty(value = "红字发票号码")
    private String redInvoiceNo;

    @ApiModelProperty(value = "红字开票日期 yyyy-MM-dd HH:mm:ss")
    private String redInvoiceDate;

    @ApiModelProperty(value = "录入日期 yyyy-MM-dd HH:mm:ss")
    private String entryDate;

    @ApiModelProperty(value = "修改日期 yyyy-MM-dd HH:mm:ss")
    private String modifyDate;

    @ApiModelProperty(value = "有效标志 Y：有效；N：无效")
    private String validFlag;

    @ApiModelProperty(value = "差额征税类型代码 01：差额征税-全额开票；02：差额征税-差额开票")
    private String taxationTypeCode;

    @ApiModelProperty(value = "详情")
    private List<RedConfirmDetailReqF> electricInvoiceDetails;
}
