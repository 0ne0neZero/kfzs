package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导出收缴率报表
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportChargeCollectionRateReportData {

    @ColumnWidth(value = 20)
    @ExcelProperty("组织代码")
    private String costCenterCode;

    @ColumnWidth(value = 20)
    @ExcelProperty("项目名称")
    private String communityName;

    @ColumnWidth(value = 20)
    @ExcelProperty("楼栋名称")
    private String buildingName;

    @ColumnWidth(value = 20)
    @ExcelProperty("房屋编码")
    private String roomId;

    @ColumnWidth(value = 20)
    @ExcelProperty("房屋名称")
    private String roomName;

    @ColumnWidth(value = 20)
    @ExcelProperty("管家名称")
    private String housekeeperName;

    @ColumnWidth(value = 20)
    @ExcelProperty("计费面积")
    private BigDecimal chargingArea;

    @ColumnWidth(value = 20)
    @ExcelProperty("业主名称")
    private String ownerName;

    @ColumnWidth(value = 20)
    @ExcelProperty("开票类别")
    private String invoiceTypeList;

    @ColumnWidth(value = 20)
    @ExcelProperty("费项")
    private String chargeItemName;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期饱和应收")
    private BigDecimal totalAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期应收减免")
    private BigDecimal deductibleAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期实收")
    private BigDecimal actualPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期实收减免")
    private BigDecimal discountAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期未收")
    private BigDecimal actualUnPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("本期收缴率")
    private BigDecimal collectionRate;

    @ColumnWidth(value = 20)
    @ExcelProperty("收款截止日期")
    private LocalDateTime chargeTime;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期应收")
    private BigDecimal earlyReceivableAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期应收减免")
    private BigDecimal earlyDeductibleAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期实收")
    private BigDecimal earlyActualPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期实收减免")
    private BigDecimal earlyDiscountAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期未收")
    private BigDecimal earlyActualUnPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("前期收缴率")
    private BigDecimal earlyCollectionRate;

    @ColumnWidth(value = 20)
    @ExcelProperty("总和应收")
    private BigDecimal sumReceivableAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("总和实收")
    private BigDecimal sumActualPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("总和收缴率")
    private BigDecimal sumCollectionRate;
}
