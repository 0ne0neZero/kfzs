package com.wishare.finance.domains.bill.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单缴费信息
 *
 * @Author dxclay
 * @Date 2022/9/14
 * @Version 1.0
 */
@Getter
@Setter
public class BillChargeInfoDto {

    /**
     * 缴费开始时间
     */
    private LocalDateTime chargeStartTime;
    /**
     * 缴费结束时间
     */
    private LocalDateTime chargeEndTime;
    /**
     * 实际结算的金额
     */
    private Long settleAmount;
    /**
     * 缴费剩余金额
     */
    private Long remainingAmount;

}
