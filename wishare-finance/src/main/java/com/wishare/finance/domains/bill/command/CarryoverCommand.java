package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 结转命令
 *
 * @author yancao
 */
@Getter
@Setter
public class CarryoverCommand {

    /**
     * 结转账单id
     */
    private Long carriedBillId;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 结转金额
     */
    private Long carryoverAmount;

    /**
     * 结转方式：1抵扣，2结转预收
     */
    private Integer carryoverType;

    /**
     * 结转附件文件路径
     */
    private List<String> fileUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否结转预收： 0不转预收，1转预收
     */
    private Integer advanceCarried;

    /**
     * 结转详情
     */
    private List<CarryoverDetail> carryoverDetail;

    @Setter
    @Getter
    public static class CarryoverDetail {

        /**
         * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
         */
        private Integer billType;

        /**
         * 被结转账单id
         */
        private Long targetBillId;

        /**
         * 被结转账单编号
         */
        private String targetBillNo;

        /**
         * 结转金额
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
         * 收费开始时间
         */
        private LocalDateTime chargeStartTime;

        /**
         * 收费结束时间
         */
        private LocalDateTime chargeEndTime;

    }

}
