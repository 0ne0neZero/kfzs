package com.wishare.finance.apps.model.voucher.dto;

import lombok.Data;

/**
 * 传参例子 {"tenantId":"108314314140208","taskId":134738843383201}
 * @author szh
 * @date 2024/3/29 9:51
 */
@Data
public class VoucherRuleSchedulerDto {
    private String tenantId;
    /**
     * voucher_rule#id
     */
    private Long taskId;
}
