package com.wishare.finance.domains.configure.businessunit.command.businessunit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 创建业务单元关联命令
 *
 * @author
 */
@Getter
@Setter
public class CreateBusinessUnitDetailCommand {

    /**
     * id
     */
    private Long id;

    /**
     * 业务单元id
     */
    private Long businessUnitId;

    /**
     * 关联id
     */
    private Long relevanceId;

    /**
     * 关联类型
     */
    private String type;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

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
