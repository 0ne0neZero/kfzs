package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "立项关联订单明细")
public class ContractProjectOrderInfoV {

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品数量")
    private Integer quantity;

    @ApiModelProperty(value = "商品型号")
    private String productModel;

    @ApiModelProperty(value = "商品税率")
    private String taxRate;

    @ApiModelProperty(value = "商品价格(默认含税价)")
    private BigDecimal amount;

    @ApiModelProperty(value = "商品税额(仅供参考，通过税率计算得出，与发票税金有差异)")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "商品价格不含税")
    private BigDecimal amountWithoutTax;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "销售单位")
    private String unit;

    @ApiModelProperty(value = "订单状态 0 待发货 1 已签收 2 已取消 3 已退货")
    private Integer orderStatus = 0;

    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime orderCreateTime;

    @ApiModelProperty(value = "下单账号")
    private String orderAccount;

}