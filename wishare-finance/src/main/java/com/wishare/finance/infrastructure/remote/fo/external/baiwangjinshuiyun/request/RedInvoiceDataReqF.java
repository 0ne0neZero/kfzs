package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 发票主体信息
 * @author dongpeng
 * @date 2023/10/25 20:19
 */
@Data
@ApiModel(value = "发票主体信息")
public class RedInvoiceDataReqF {

    @ApiModelProperty(value = "单据编号(全局唯一)", required = true)
    private String djbh;

    @ApiModelProperty(value = "原全电发票号码(红冲时使用)", required = true)
    private String yqdfphm;

    @ApiModelProperty(value = "原单据编号(红冲时使用，与原发票代码任选一个值，同时有值时以单据编号为准)", required = true)
    private String ydjbh;

    @ApiModelProperty(value = "原发票代码(红冲时使用)")
    private String yfpdm;

    @ApiModelProperty(value = "原发票号码(红冲时使用)")
    private String yfphm;

    @ApiModelProperty(value = "冲红原因代码(红冲时使用) " +
            "01:开票有误" +
            "02:销货退回" +
            "03:服务中止" +
            "04:销售折让", required = true)
    private String chyydm;

    @ApiModelProperty(value = "数据类型", required = true)
    private String sjlx;

    @ApiModelProperty(value = "含税金额（价税合计 小数点2位）", required = true)
    private BigDecimal hsje;

    @ApiModelProperty(value = "合计金额（小数点2位）")
    private BigDecimal hjje;

    @ApiModelProperty(value = "合计税额（小数点2位）")
    private BigDecimal hjse;

    @ApiModelProperty(value = "特定约束类型代码（见附录8.2，默认00）")
    private String tdyslxdm;

    @ApiModelProperty(value = "商品明细信息",required = true)
    private List<RedInvoiceDetailF> mxxx;

    @ApiModelProperty(value = "建筑服务信息")
    private InvoiceBuildingServiceInfoF jzfw;

    @ApiModelProperty(value = "不动产经营租赁服务")
    private InvoiceRealEstateLeaseInfoF bdcjyzlfw;

}
