package com.wishare.finance.domains.configure.chargeitem.command.tax;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddTaxCategoryCommand  {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 税种编码
     */
    private String code;
    /**
     * 税种名称
     */
    private String name;
    /**
     * 父税种id
     */
    private Long parentId;
    /**
     * 税种id路径
     */
    private String path;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否启用
     */
    private Integer disabled;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 创建人ID
     */
    private String creator;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;
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
