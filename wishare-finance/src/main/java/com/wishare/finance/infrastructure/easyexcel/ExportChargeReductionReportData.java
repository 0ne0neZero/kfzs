package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导出收费减免统计报表
 *
 * @author yancao
 */
@Getter
@Setter
public class ExportChargeReductionReportData {

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
    @ExcelProperty("收费项目")
    private String chargeItemName;

    @ColumnWidth(value = 20)
    @ExcelProperty("减免方式")
    private String reductionType;

    @ColumnWidth(value = 20)
    @ExcelProperty("减免单号")
    private String reductionNo;

    @ColumnWidth(value = 20)
    @ExcelProperty("减免期间")
    private String reductionTime;

    @ColumnWidth(value = 20)
    @ExcelProperty("应收金额")
    private BigDecimal receivableAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("减免金额")
    private BigDecimal reductionAmount;

    @ColumnWidth(value = 20)
    @ExcelProperty("减免比例")
    private BigDecimal reductionRatio;

    @ColumnWidth(value = 20)
    @ExcelProperty("审批状态")
    private String approvedState;

    @ColumnWidth(value = 20)
    @ExcelProperty("审批通过日期")
    private LocalDateTime approvedTime;

    @ColumnWidth(value = 20)
    @ExcelProperty("流程发起日期")
    private LocalDateTime initiationTime;

    @ColumnWidth(value = 20)
    @ExcelProperty("原因分类")
    private String reductionReason;

    @ColumnWidth(value = 20)
    @ExcelProperty("具体说明")
    private String approvedRemark;
}
