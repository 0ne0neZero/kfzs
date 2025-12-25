package com.wishare.finance.domains.configure.businessunit.command.businessunit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 删除业务单元命令
 *
 * @author
 */
@Getter
@Setter
public class DeletedBusinessUnitCommand {

    /**
     * 业务单元id
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
