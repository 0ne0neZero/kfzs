package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导出预收率统计表报表
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportAdvanceCollectionRateReportData {

    @ColumnWidth(value = 20)
    @ExcelProperty("区域")
    private String region;

    @ColumnWidth(value = 20)
    @ExcelProperty("项目群")
    private String projectGroup;

    @ColumnWidth(value = 20)
    @ExcelProperty("组织代码")
    private String costCenterCode;

    @ColumnWidth(value = 20)
    @ExcelProperty("楼盘名称")
    private String communityName;

    @ColumnWidth(value = 20)
    @ExcelProperty("楼栋名称")
    private String buildingName;

    @ColumnWidth(value = 20)
    @ExcelProperty("房屋编码")
    private Long roomId;

    @ColumnWidth(value = 20)
    @ExcelProperty("房屋名称")
    private String roomName;

    @ColumnWidth(value = 20)
    @ExcelProperty("管家姓名")
    private String housekeeperName;

    @ColumnWidth(value = 20)
    @ExcelProperty("建筑面积")
    private BigDecimal buildingArea;

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
    @ExcelProperty("饱和应收")
    private BigDecimal totalAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("实收")
    private BigDecimal actualPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("未收")
    private BigDecimal actualUnPayAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("预收率")
    private BigDecimal advanceRate;

    @ColumnWidth(value = 20)
    @ExcelProperty("收款日期")
    private LocalDateTime chargeTime;
}
