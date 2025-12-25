package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AddPeriodicReceivableBillCommand {

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
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 周期开始时间 格式：yyyy-mm-dd HH:mm:ss
     */
    private LocalDateTime startTime;

    /**
     * 周期开始时间 格式：yyyy-mm-dd HH:mm:ss
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
     * 指定周期的账单列表
     */
    private List<AddPeriodicBillCommand> receivableBills;

    /**
     * 上级收费单元ID
     */
    private String supCpUnitId;

}
