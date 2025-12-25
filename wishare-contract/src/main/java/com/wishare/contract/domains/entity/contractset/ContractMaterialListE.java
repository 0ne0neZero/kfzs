package com.wishare.contract.domains.entity.contractset;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合同采购物资清单
 * @author ljx
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contract_material_list")
public class ContractMaterialListE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 清单id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 采购物资名称
     */
    private String name;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 规格型号
     */
    private String model;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 当前库存数量
     */
    private Integer sQuantity;
    /**
     * 申请采购数量
     */
    private Integer pQuantity;
    /**
     * 预算单价
     */
    private BigDecimal unitPrice;
    /**
     * 预算总价
     */
    private BigDecimal total;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 具体说明
     */
    private String explain;
    /**
     * 合同id
     */
    private Long contractId;
    /**
     * 是否删除：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

}
