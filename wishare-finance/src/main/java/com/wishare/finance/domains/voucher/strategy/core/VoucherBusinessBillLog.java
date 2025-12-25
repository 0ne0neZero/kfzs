package com.wishare.finance.domains.voucher.strategy.core;

import lombok.*;

/**
 * 凭证业务单据日志vo
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/7
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherBusinessBillLog {

    private Long businessBillId;
    private String businessBillNo;
    private String supCpUnitId;

}
