package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author: Linitly
 * @date: 2023/6/25 13:34
 * @descrption:
 */
@Data
@ApiModel(value = "发票查询数据请求体")
public class InvoiceSearchDataReqF {

    // 非必填，长度5
    @ApiModelProperty(value = "是否查询税号下所有发票(如果机构代码为空且该字段为true，则查询税号下所有符合条件的发票")
    private Boolean queryAll;

    // 非必填，长度50
    @ApiModelProperty(value = "开票流水号")
    private String serialNo;

    // 非必填，长度
    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    // 非必填，长度8
    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    // 非必填，长度20
    @ApiModelProperty(value = "全电发票号码")
    private String einvoiceNo;

    // 非必填，长度3
    @ApiModelProperty(value = "发票类型代码 004：增值税专用发票；007：增值税普通发票；026：增 \n" +
            "值税电子发票；025：增值税卷式发票；028:增值税电子专用发票 005 机 \n" +
            "动车发票 006 二手车发票")
    private String invoiceTypeCode;

    // 非必填，长度30
    @ApiModelProperty(value = "开票终端代码")
    private String invoiceTerminalCode;

    // 非必填，长度80
    @ApiModelProperty(value = "来源标志 01 接口开具 02已开上传 03 开票申请单 04 流水单 05 接口待开 \n" +
            "上传 06 销方待开导入 07 购方待开导入 08 非百望云开具 17 历史发票管 \n" +
            "理")
    private String sourceMark;

    // 非必填，长度2
    @ApiModelProperty(value = "特殊票种标志， 00：普通发票；01：农产品销售；02：农产品收购；08：成品油 12 机动车")
    private String invoiceSpecialMark;

    // 非必填，长度20
    @ApiModelProperty(value = "购方税号")
    private String buyerTaxNo;

    // 非必填，长度80
    @ApiModelProperty(value = "购方单位名称")
    private String buyerName;

    // 非必填，长度1
    @ApiModelProperty(value = "打印状态 0，1")
    private String printStatus;

    // 非必填，长度2
    @ApiModelProperty(value = "发票状态：发票状态默认为空，00开具成功 02空白发票作废 03:已开发票作废 05：正票全额红冲 06：部分红冲")
    private String invoiceStatus;

    // 非必填，长度1
    @ApiModelProperty(value = "开票类型：0:正数 1：负数")
    private String invoiceType;

    // 非必填，长度14
    @ApiModelProperty(value = "开票日期起，格式：yyyy-MM-dd")
    private String invoiceStartDate;

    // 非必填，长度14
    @ApiModelProperty(value = "开票日期止，格式：yyyy-MM-dd")
    private String invoiceEndDate;

    // 非必填，长度50
    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    // 非必填，长度50
    @ApiModelProperty(value = "保单号")
    private String contractNo;

    // 非必填，长度50
    @ApiModelProperty(value = "业务请求流水号")
    private String orderNo;

    // 非必填，长度1
    @ApiModelProperty(value = "征收方式 0：普通征收 2：差额征收")
    private String taxationMethod;

    // 非必填，长度1
    @ApiModelProperty(value = "清单标志 0：非请单 1：清单")
    private String invoiceListMark;

    // 非必填，长度10
    @ApiModelProperty(value = "机器编号")
    private String machineNo;

    // 非必填，长度150
    @ApiModelProperty(value = "第三方系统名称")
    private String systemName;

    // 非必填，长度50
    @ApiModelProperty(value = "第三方系统id")
    private String systemId;

    // 非必填，长度3
    @ApiModelProperty(value = "验签状态(N00 未签名,Y00 已签名,未上传 Y11,验签成功 Y10 验签失败)")
    private String invoiceCheckMark;

    // 非必填，长度5
    @ApiModelProperty(value = "页码")
    private Integer pageNo = 1;

    // 非必填，长度3
    @ApiModelProperty(value = "每页条数 最多100条")
    private Integer pageSize = 100;

    // 非必填
    @ApiModelProperty(value = "扩展字段")
    private Map<String, Object> ext;
}
