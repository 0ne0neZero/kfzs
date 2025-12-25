package com.wishare.finance.domains.bill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author fengxiaolin
 * @date 2023/5/11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmTransactDto {

    /**
     * 收款单Id
     */
    private Long gatherBillId;

    /**
     * 处理的账单id
     */
    private List<String> billIds;
}
