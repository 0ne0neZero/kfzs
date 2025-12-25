package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddPeriodicBillCommand {

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称
     */
    private String statutoryBodyName;
    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 上级收费单元ID
     */
    private String supCpUnitId;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 计费方式
     */
    private Integer billMethod;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 积分面积
     */
    private BigDecimal chargingArea;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据
     */
    private String invoiceType;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号 (必传：用于账单的重复性校验)
     */
    private String outBusNo;

    /**
     * 账单说明
     */
    private String description;

    /**
     * 币种(货币代码)（默认：CNY:人民币）
     */
    private String currency;

    /**
     * 账单金额
     */
    private Long totalAmount;

    /**
     * 是否逾期：0未逾期，1已逾期
     */
    private Integer overdueState;

    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 应用名称
     */
    private String appName;

}
