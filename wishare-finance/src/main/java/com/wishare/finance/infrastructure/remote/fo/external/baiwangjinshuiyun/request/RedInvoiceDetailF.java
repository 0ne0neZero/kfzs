package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 红冲发票商品明细
 * @author dongpeng
 * @date 2023/10/25 19:32
 *
 */
@Data
@ApiModel("发票商品明细")
public class RedInvoiceDetailF {

    @ApiModelProperty(value = "单据行号(商品明细序号，整数)",required = true)
    private String djhh;

    @ApiModelProperty(value = "商品数量(数电差额征收票商品数量传空值)")
    private BigDecimal spsl;

    @ApiModelProperty(value = "含税单价(商品含税单价，数电差额征收票含税单价传空值)")
    private BigDecimal hsdj;

    @ApiModelProperty(value = "含税金额（含税标志为1时必填 小数点2位）")
    private BigDecimal hsje;

    @ApiModelProperty(value = "不含税单价(商品含税单价，数电差额征收票不含税单价传空值)")
    private BigDecimal bhsdj;

    @ApiModelProperty(value = "不含税金额（含税标志为0时必填 小数点2位）")
    private BigDecimal bhsje;

    @ApiModelProperty(value = "税额(含税标志为0时必填 小数点2位)")
    private BigDecimal se;
}
