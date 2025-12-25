package com.wishare.finance.apps.model.configure.chargeitem.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RemissionManagementDetailDTO {
    private String communityId;
    private String communityName;
    private String billNo;
    /**
     * 批次号
     */
    private String batchNumber;
    private Long spaceId;
    private String spaceNo;
    private Long chargeItem;

    private String chargeItemName;
    /**
     * 账单金额
     */
    private BigDecimal billAmount;
    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;
    /**
     * 实收金额
     */
    private BigDecimal receiptAmount;
    /**
     * 减免形式：1-应收减免，2-实收减免，3-不减免
     */
    private int deductionMethod;
    private String deductionMethodStr;
    /**
     * 减免金额
     */
    private BigDecimal deductibleAmount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 收费对象类型
     */
    private int payerType;
    private String payerTypeStr;
    /**
     * 收费对象
     */
    private String payerId;
    private String payerName;
    /**
     * 账单来源
     */
    private String source;
    /**
     * 结算状态（0未结算，1部分结算，2已结算）
     */
    private int settleState;
    private String settleStateStr;
    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 修改人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

    private int billType;
    private Long billid;
}
