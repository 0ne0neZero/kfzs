package com.wishare.finance.domains.configure.subject.command.system;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 更新科目体系命令
 *
 * @author yancao
 */
@Getter
@Setter
public class UpdateSubjectSystemCommand {

    /**
     * 科目体系id
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
     * 是否启用
     */
    private Integer disabled;

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
