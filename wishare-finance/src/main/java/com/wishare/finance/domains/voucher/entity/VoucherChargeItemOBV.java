package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 凭证费项值对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class VoucherChargeItemOBV {

    @ApiModelProperty(value = "凭证费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "凭证费项名称")
    private String chargeItemName;

    public VoucherChargeItemOBV() {
    }

    public VoucherChargeItemOBV(Long chargeItemId, String chargeItemName) {
        this.chargeItemId = chargeItemId;
        this.chargeItemName = chargeItemName;
    }
}
