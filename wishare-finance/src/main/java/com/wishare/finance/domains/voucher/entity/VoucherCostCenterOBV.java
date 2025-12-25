package com.wishare.finance.domains.voucher.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 成本中心值对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Getter
@Setter
public class VoucherCostCenterOBV {

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    public VoucherCostCenterOBV() {
    }

    public VoucherCostCenterOBV(Long costCenterId, String costCenterName) {
        this.costCenterId = costCenterId;
        this.costCenterName = costCenterName;
    }
}
