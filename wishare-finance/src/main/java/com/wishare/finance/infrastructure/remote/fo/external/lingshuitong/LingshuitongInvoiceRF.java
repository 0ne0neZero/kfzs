package com.wishare.finance.infrastructure.remote.fo.external.lingshuitong;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("灵税通发票内容")
public class LingshuitongInvoiceRF {

    @ApiModelProperty(value = "凭证编号",required = true)
    @NotBlank(message = "凭证编号不能为空")
    private String voucherNumber;

    /*有枚举  LingshuitongInvoiceKindEnum */
    @ApiModelProperty(value = "发票类型：" +
            "增值税纸质专用发票\n" +
            "增值税电子专用发票\n" +
            "增值税纸质普通发票\n" +
            "增值税电子普通发票\n" +
            "增值税普通发票（卷式）\n" +
            "增值税电子通行费发票\n" +
            "机动车销售统一发票\n" +
            "二手车销售统一发票\n" +
            "火车票\n" +
            "客运汽车\n" +
            "航空运输电子客票行程单\n" +
            "船票\n" +
            "出租车发票\n" +
            "通用机打发票\n" +
            "电子机打发票\n" +
            "定额发票\n" +
            "过路费发票\n" +
            "区块链发票\n" +
            "国际小票\n" +
            "出行行程单\n" +
            "其他发票\n" +
            "全电发票（增值税专用发票）\n" +
            "全电发票（增值税普通发票",required = true)
    @NotBlank(message = "发票类型不能为空")
    private String invoiceKind;

    @ApiModelProperty(value = "发票代码",required = true)
    @NotBlank(message = "发票代码不能为空")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码",required = true)
    @NotBlank(message = "发票号码不能为空")
    private String invoiceNo;
}
