package com.wishare.finance.domains.configure.organization.command;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Getter
@Setter
public class UpdateCostCenterCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 成本中心编码
     */
    private String code;
    /**
     * 关联对象类型:0组织；1项目
     */
    private Integer relationType;
    /**
     * 成本中心名称-中文
     */
    private String nameCn;
    /**
     * 成本中心名称-英文
     */
    private String nameEn;
    /**
     * 成本中心类型：1费用；2基本生产；3辅助生产
     */
    private Integer type;
    /**
     * 负责人名称
     */
    private String principalName;
    /**
     * 关联组织id
     */
    private Long orgId;
    /**
     * 关联组织名称
     */
    private String orgName;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 是否与主数据库同步：0否 1是
     */
    private Integer dataSyn;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;
}
