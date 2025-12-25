package com.wishare.finance.domains.bill.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 交账数量数据对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/12
 */
@Getter
@Setter
public class AccountHandCountDto {

    /**
     * 可交账数量
     */
    private Integer handCount;

    /**
     * 账单类型
     */
    private Integer billType;

}
