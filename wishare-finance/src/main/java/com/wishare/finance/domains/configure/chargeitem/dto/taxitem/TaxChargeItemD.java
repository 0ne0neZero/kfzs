package com.wishare.finance.domains.configure.chargeitem.dto.taxitem;

import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemE;
import lombok.Getter;
import lombok.Setter;

/**
 * 税目dto
 *
 * @author yancao
 */
@Getter
@Setter
public class TaxChargeItemD {

    /**
     * 税目名称
     */
    private Long chargeItemId;

    /**
     * 税目信息
     */
    private TaxItemE taxItem;
}
