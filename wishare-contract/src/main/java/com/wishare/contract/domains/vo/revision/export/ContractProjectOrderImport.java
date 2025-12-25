package com.wishare.contract.domains.vo.revision.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wishare.component.imports.ExcelImport;
import com.wishare.component.imports.extension.easyexcel.EasyExcelImport;
import com.wishare.component.imports.extension.easyexcel.annotation.MustFill;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 立项订单导入
 */
@Getter
@Setter
@ToString
public class ContractProjectOrderImport extends EasyExcelImport implements ExcelImport {

    /**
     * 采购平台
     */
    @ExcelProperty({"采购平台"})
    @MustFill
    private String platform;

    /**
     * 订单号
     */
    @ExcelProperty({"订单号"})
    @MustFill
    private String orderNumber;

    /**
     * 商品名称
     */
    @ExcelProperty({"商品名称"})
    @MustFill
    private String name;

    /**
     * 规格\型号
     */
    @ExcelProperty({"规格\\型号"})
    @MustFill
    private String unit;

    /**
     * 单价
     */
    @ExcelProperty({"单价"})
    @MustFill
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    @ExcelProperty({"数量"})
    @MustFill
    private Integer quantity;

    /**
     * 总金额（含税）
     */
    @ExcelProperty({"总金额（含税）"})
    @MustFill
    private BigDecimal amount;

    /**
     * 总金额（不含税）
     */
    @ExcelProperty({"总金额（不含税）"})
    @MustFill
    private BigDecimal amountWithoutTax;

    /**
     * 下单人
     */
    @ExcelProperty({"下单人"})
    @MustFill
    private String orderAccount;

    /**
     * 下单时间
     */
    @MustFill
    @ExcelProperty(value = "下单时间", converter = ConverterLocalDateTime.class)
    private LocalDateTime orderCreateTime;

}
