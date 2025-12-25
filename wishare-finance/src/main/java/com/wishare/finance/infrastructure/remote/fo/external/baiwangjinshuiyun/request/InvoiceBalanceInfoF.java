package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

/**
 * 差额征收明细 征收方式为2和3时必填
 * @author dongpeng
 * @date 2023/10/25 20:00
 */
@Data
@ApiModel("差额征收明细")
public class InvoiceBalanceInfoF {

    @ApiModelProperty(value = "凭证类型(01:全电发票\n" +
            "02:增值税发票\n" +
            "03:增值税普通发票\n" +
            "04:营业税发票\n" +
            "05:财政票据\n" +
            "06:法院裁决书\n" +
            "07:契税完税凭证\n" +
            "08:其他发票类\n" +
            "09:其他扣除凭证)",required = true)
    private String pzlx;

    @ApiModelProperty(value = "发票代码(02,03,04必填)")
    private String fpdm;

    @ApiModelProperty(value = "发票号码(02,03必填)")
    private String fphm;

    @ApiModelProperty(value = "全电发票号码(01必填)")
    private String qdpfphm;

    @ApiModelProperty(value = "凭证号码")
    private String pzhm;

    @ApiModelProperty(value = "开具日期(2022-07-04)",required = true)
    private String kjrq;

    @ApiModelProperty(value = "合计金额(小数点2位)",required = true)
    private Decimal hjje;

    @ApiModelProperty(value = "扣除额(多个明细相加之和为外层报文节点kce)",required = true)
    private Decimal kce;

    @ApiModelProperty(value = "备注",required = true)
    private String bz;

    @ApiModelProperty(value = "来源(手工录入)",required = true)
    private String ly;

    @ApiModelProperty(value = "本次扣除额(小数点2位)",required = true)
    private Decimal bckcje;

    @ApiModelProperty(value = "凭证合计金额(小数点2位)",required = true)
    private Decimal pzhjje;

    @ApiModelProperty(value = "序号",required = true)
    private String xh;

}
