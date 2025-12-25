package com.wishare.finance.domains.configure.businessunit.command.businessunit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 创建业务单元命令
 *
 * @author
 */
@Getter
@Setter
public class CreateBusinessUnitCommand {

    /**
     * 业务单元id
     */
    private Long id;

    /**
     * 业务单元名称
     */
    private String name;

    /**
     * 业务单元编码
     */
    private String code;

    /**
     * 父业务单元id
     */
    private Long parentId;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 是否末级：0否,1是
     */
    private Integer lastLevel;

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
