package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 导出收费日报报表
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportChargeDailyReportData {

    @ColumnWidth(value = 20)
    @ExcelProperty("项目名称")
    private String communityName;

    @ColumnWidth(value = 20)
    @ExcelProperty("楼栋名称")
    private String buildingName;

    @ColumnWidth(value = 20)
    @ExcelProperty("房屋名称")
    private String roomName;

    @ColumnWidth(value = 20)
    @ExcelProperty("业主名称")
    private String ownerName;

    @ColumnWidth(value = 20)
    @ExcelProperty("单据编号")
    private String billNo;

    @ColumnWidth(value = 20)
    @ExcelProperty("票据编号")
    private String invoiceReceiptNo;

    @ColumnWidth(value = 20)
    @ExcelProperty("收款人")
    private String payeeName;

    @ColumnWidth(value = 20)
    @ExcelProperty("收款方式")
    private String payChannel;

    @ColumnWidth(value = 20)
    @ExcelProperty("收费项目")
    private String chargeItemName;

    @ColumnWidth(value = 20)
    @ExcelProperty("金额")
    private BigDecimal payAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("备注")
    private String remark;
}
