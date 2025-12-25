package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 账单转结命令
 *
 * @Author dxclay
 * @Date 2022/9/9
 * @Version 1.0
 */
@Getter
@Setter
public class ApplyCarryoverCommand {

    /**
     * 结转账单id
     */
    private Long carriedBillId;

    /**
     * 结转额度
     */
    private Long carriedAmount;

    /**
     * 被结转账单列表
     */
    private List<CarryInfo> targetBills;

    /**
     * 结转方式：1抵扣，2结转预收
     */
    private Integer carryoverType;

    /**
     * 结转附件文件路径
     */
    private List<String> fileUrl;

    /**
     * 申请结转原因
     */
    private String applyReason;

    /**
     * 是否结转预收： 0不转预收，1转预收
     */
    private Integer advanceCarried;

    @Setter
    @Getter
    public static class CarryInfo {

        /**
         * 结转账单id
         */
        private Long targetBillId;

        /**
         * 结转账单编号
         */
        private String targetBillNo;

        /**
         * 结转金额（单位：分）
         */
        private Long carryoverAmount;

        /**
         * 费项id
         */
        private Long chargeItemId;

        /**
         * 费项名称
         */
        private String chargeItemName;

        /**
         * 收费开始时间 (可选字段，存在收费周期的账单必传)
         */
        private LocalDateTime chargeStartTime;

        /**
         * 收费结束时间 (可选字段，存在收费周期的账单必传)
         */
        private LocalDateTime chargeEndTime;

    }

}
