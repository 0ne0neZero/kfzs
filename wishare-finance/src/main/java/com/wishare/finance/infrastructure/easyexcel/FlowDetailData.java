package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导入流水明细数据
 *
 * @author yancao
 */
@Getter
@Setter
public class FlowDetailData {

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "流水号",index = 0)
    private String serialNumber;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "交易日期",index = 1)
    private LocalDateTime payTime;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "流水类型",index = 2)
    private String type;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "交易金额（元）",index = 3)
    private BigDecimal settleAmount;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "对方账户",index = 4)
    private String oppositeAccount;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "对方名称",index = 5)
    private String oppositeName;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "对方开户行",index = 6)
    private String oppositeBank;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "本方账户",index = 7)
    private String ourAccount;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "本方名称",index = 8)
    private String ourName;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "本方开户行",index = 9)
    private String ourBank;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "交易平台",index = 10)
    private String tradingPlatform;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "摘要",index = 11)
    private String summary;

    @ColumnWidth(value = 20)
    @ExcelProperty(value = "资金用途",index = 12)
    private String fundPurpose;

    @ColumnWidth(value = 15)
    @ExcelProperty(value = "失败原因",index = 13)
    private String failReason;

}
