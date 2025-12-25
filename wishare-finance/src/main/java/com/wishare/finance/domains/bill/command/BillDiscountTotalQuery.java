package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

/**
 * 统计账单合计查询
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
public class BillDiscountTotalQuery {

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单 66全部）
     */
    private Integer billType;

    /**
     * 上级收费单元ID
     */
    private String supCpUnitId;
}
