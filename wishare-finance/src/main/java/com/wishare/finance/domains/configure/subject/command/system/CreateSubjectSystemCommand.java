package com.wishare.finance.domains.configure.subject.command.system;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 创建科目体系命令
 *
 * @author yancao
 */
@Getter
@Setter
public class CreateSubjectSystemCommand {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 法定单位
     */
    private String pertainId;

    /**
     * 科目体系编码
     */
    private String code;

    /**
     * 科目体系名称
     */
    private String name;

    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;

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
