package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* 合同采购物资清单 保存请求参数
* </p>
*
* @author wangrui
* @since 2022-12-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractMaterialListSaveF {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("合同id")
    private Long contractId;

    @ApiModelProperty("采购物资名称")
    private String name;

    @ApiModelProperty("品牌")
    private String brand;

    @ApiModelProperty("规格型号")
    private String model;

    @ApiModelProperty("计量单位")
    private String unit;

    @ApiModelProperty("当前库存数量")
    private Integer sQuantity;

    @ApiModelProperty("申请采购数量")
    private Integer pQuantity;

    @ApiModelProperty("预算单价")
    private BigDecimal unitPrice;

    @ApiModelProperty("预算总价")
    private BigDecimal total;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("具体说明")
    private String explain;

}
