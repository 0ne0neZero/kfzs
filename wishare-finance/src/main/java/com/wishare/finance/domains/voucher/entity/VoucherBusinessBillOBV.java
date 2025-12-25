package com.wishare.finance.domains.voucher.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 凭证业务账单单据值对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/30
 */
@Getter
@Setter
public class VoucherBusinessBillOBV {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 账单类型
     */
    private String billType;

    /**
     * 费项id
     */
    private String chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 账单金额
     */
    private Long totalAmount = 0L;

    /**
     * 应收金额
     */
    private Long receivableAmount = 0L;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount = 0L;

    /**
     * 实收减免金额
     */
    private Long discountAmount = 0L;

    /**
     * 实收金额
     */
    private Long settleAmount = 0L;

    /**
     * 退款金额
     */
    private Long refundAmount = 0L;

    /**
     * 结转金额
     */
    private Long carriedAmount = 0L;

    /**
     * 开票金额
     */
    private Long invoiceAmount = 0L;

    /**
     * 账单状态
     */
    private Integer state;

    /**
     * 结算状态
     */
    private Integer settleState;

    /**
     * 结转状态：0未结转，1待结转，2部分结转，3已结转
     */
    private Integer carriedState;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    private Integer refundState;

    /**
     * 归属年
     */
    private Integer accountYear;

    /**
     * 归属月（账期）
     */
    private LocalDate accountDate;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

}
