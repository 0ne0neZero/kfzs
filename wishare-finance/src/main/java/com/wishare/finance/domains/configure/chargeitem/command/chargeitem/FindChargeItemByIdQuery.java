package com.wishare.finance.domains.configure.chargeitem.command.chargeitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据id查询费项命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindChargeItemByIdQuery {

    /**
     * 费项id
     */
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;
}
