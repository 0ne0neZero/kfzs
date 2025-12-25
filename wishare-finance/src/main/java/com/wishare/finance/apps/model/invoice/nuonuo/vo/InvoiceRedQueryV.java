package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字专用发票信息表查询接口反参")
public class InvoiceRedQueryV {

    @ApiModelProperty("24位申请单号:12位金税盘编号+12位开票时间(YYMMDDHHMMSS)")
    private String billNo;

    @ApiModelProperty("信息表编号")
    private String billInfoNo;

    @ApiModelProperty("信息表状态(0:申请中 1:审核成功 2: 审核失败 3:申请成功 4:申请失败)")
    private Integer billStatus;

    @ApiModelProperty("[信息表状态代码]信息表状态描述")
    private String billMessage;

    @ApiModelProperty("信息表类型(0:正常 1:逾期 2:机动车类专票信息表（涉及退货和开具错误等，合格证退回）3:机动车类专票信息表（仅涉及销售折让，合格证不退回）)")
    private String BillType;

    @ApiModelProperty("对应蓝票代码")
    private String oriInvoiceCode;

    @ApiModelProperty("对应蓝票号码")
    private String oriInvoiceNumber;

    @ApiModelProperty("税种类别(0:营业税 1:增值税)")
    private String taxType;

    @ApiModelProperty("多税率标志(0:一票一税率 1:一票多税率)")
    private String multTaxRate;

    @ApiModelProperty("填开日期")
    private String billTime;

    @ApiModelProperty("销方税号")
    private String sellerTaxNo;

    @ApiModelProperty("销方名称")
    private String sellerName;

    @ApiModelProperty("购方税号")
    private String buyerTaxNo;

    @ApiModelProperty("购方名称")
    private String buyerName;

    @ApiModelProperty("合计金额(不含税)")
    private String taxExcludedAmount;

    @ApiModelProperty("合计税额")
    private String taxAmount;

    @ApiModelProperty("申请说明")
    private String applyRemark;

    @ApiModelProperty("申请表pdf地址")
    private String pdfUrl;

    @ApiModelProperty("商品编码版本号")
    private String codeVersion;

    @ApiModelProperty("营业税标志")
    private String businessTaxCode;

    @ApiModelProperty("红字信息表明细信息列表")
    private List<DetailV> details;
}
