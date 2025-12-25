package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 应收单导出实体
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportReceivableBillData {

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
    @ExcelProperty(value = "账单金额(元)")
    private BigDecimal totalAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "应收金额(元)")
    private BigDecimal receivableAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "实收金额(元)")
    private BigDecimal settleAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "收款方")
    private String payeeName;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "结算状态")
    private String settleState;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "审核状态")
    private String approvedState;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "账单来源")
    private String sysSource;
}
