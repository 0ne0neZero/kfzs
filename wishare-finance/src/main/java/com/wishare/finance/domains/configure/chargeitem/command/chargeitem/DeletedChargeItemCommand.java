package com.wishare.finance.domains.configure.chargeitem.command.chargeitem;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 删除费项命令
 *
 * @author yancao
 */
@Getter
@Setter
public class DeletedChargeItemCommand {

    /**
     * 费项id
     */
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

}
