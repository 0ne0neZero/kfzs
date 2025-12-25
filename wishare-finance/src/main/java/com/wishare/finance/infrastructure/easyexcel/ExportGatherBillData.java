package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收款单导出实体
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportGatherBillData {

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单编号")
    private String billNo;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "法定单位")
    private String statutoryBodyName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "成本中心")
    private String costCenterName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "费项")
    private String chargeItemName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收费单元")
    private String cpUnitName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收款金额(元)")
    private BigDecimal totalAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "付款方")
    private String payerName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收款人")
    private String payeeName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收款方式")
    private String payChannel;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "开票状态")
    private String invoiceState;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单来源")
    private String sysSource;
}
