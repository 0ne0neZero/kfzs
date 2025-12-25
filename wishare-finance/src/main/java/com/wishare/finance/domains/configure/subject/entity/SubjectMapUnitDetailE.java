package com.wishare.finance.domains.configure.subject.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 科目映射规则明细表(SubjectMapUnitDetail)实体类
 *
 * @author makejava
 * @since 2022-12-20 09:30:09
 */
@Getter
@Setter
@TableName("subject_map_unit_detail")
public class SubjectMapUnitDetailE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 科目映射规则id
     */
    private Long subMapRuleId;
    /**
     * 映射单元类型（1 费项 2 辅助核算）
     */
    private Integer subMapType;
    /**
     * 映射类别： 1科目，2现金流量
     */
    private Integer mapType;

    /**
     * 映射单元id
     */
    private Long subMapUnitId;
    /**
     * 一级科目id
     */
    private Long subjectLevelOneId;
    /**
     * 一级科目名称
     */
    private String subjectLevelOneName;
    /**
     * 末级科目id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long subjectLevelLastId;
    /**
     * 末级科目名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String subjectLevelLastName;
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

