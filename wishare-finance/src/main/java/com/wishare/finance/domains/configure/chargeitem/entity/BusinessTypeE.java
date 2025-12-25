package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 业务类型(BusinessType)实体类
 *
 * @author makejava
 * @since 2022-12-06 18:22:43
 */
@Getter
@Setter
@TableName("business_type")
public class BusinessTypeE  {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * code
     */
    private String code;
    /**
     * name
     */
    private String name;
    /**
     * fcode
     */
    private String fcode;
    /**
     * fname
     */
    private String fname;
    /**
     * pk_group
     */
    private String pkGroup;
    /**
     * pk_org
     */
    private String pkOrg;
    /**
     * 系统来源：1 收费系统 2 合同系统 22 用友系统
     */
    private Integer sysSource;
    /**
     * 状态  1,'未启用',2,'已启用',3,'已停用'
     */
    private Integer state;
    /**
     * 状态描述 1,'未启用',2,'已启用',3,'已停用'
     */
    private String stateStr;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}

