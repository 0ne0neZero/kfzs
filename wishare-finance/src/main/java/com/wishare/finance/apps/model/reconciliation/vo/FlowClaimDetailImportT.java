package com.wishare.finance.apps.model.reconciliation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FlowClaimDetailImportT{

    /**
     * 流水号
     */
    @ExcelProperty(value = "流水号")
    private String serialNumber;


    /**
     * 缴费时间
     */
    @ExcelProperty(value = "交易日期")
    private LocalDateTime payTime;


    /**
     * 流水类型：1收入 2退款
     */
    @ExcelProperty(value = "流水类型")
    private String typeName;

    private Integer type;

    /**
     * 缴费金额
     */
    @ExcelProperty(value = "交易金额（元）")
    private String stringsSettleAmount;

    private Long settleAmount;


    /**
     * 对方开户行
     */
    @ExcelProperty(value = "对方开户行")
    private String oppositeBank;

    /**
     * 对方账户
     */
    @ExcelProperty(value = "对方账户")
    private String oppositeAccount;

    /**
     * 对方名称
     */
    @ExcelProperty(value = "对方开户行")
    private String oppositeName;


    /**
     * 本方账户
     */
    @ExcelProperty(value = "本方账户")
    private String ourAccount;

    /**
     * 本方名称
     */
    @ExcelProperty(value = "本方名称")
    private String ourName;

    /**
     * 本方开户行
     */
    @ExcelProperty(value = "本方开户行")
    private String ourBank;

    /**
     * 交易平台
     */
    @ExcelProperty(value = "交易平台")
    private String tradingPlatform;

    /**
     * 摘要
     */
    @ExcelProperty(value = "摘要")
    private String summary;

    /**
     * 资金用途
     */
    @ExcelProperty(value = "资金用途")
    private String fundPurpose;

}
