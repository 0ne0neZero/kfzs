package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 诺诺开具发票行政区划信息
 * 远洋开具不动产发票时 查询该信息
 */
@Setter
@Getter
@TableName("invoice_zoning")
public class InvoiceZoningE {
    /**
     * 地区编码
     */
    @ApiModelProperty("地区编码")
    private String areaCode;

    /**
     * 地区名称
     */
    @ApiModelProperty("地区名称")
    private String areaName;

    /**
     * 地区级别
     */
    @ApiModelProperty("地区级别")
    private String areaLevel;

    /**
     * 父级编码
     */
    @ApiModelProperty("父级编码")
    private String superiorCode;


    @TableField(exist = false)
    private List<InvoiceZoningE> children;
}
