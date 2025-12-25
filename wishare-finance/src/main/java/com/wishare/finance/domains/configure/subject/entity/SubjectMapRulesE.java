package com.wishare.finance.domains.configure.subject.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 科目映射规则表(SubjectMapRules)实体类
 *
 * @author makejava
 * @since 2022-12-19 15:59:10
 */
@Getter
@Setter
@TableName("subject_map_rules")
public class SubjectMapRulesE{
    /**
     * 主键id
     */
    private Long id;
    /**
     * 映射规则名称
     */
    private String subMapName;
    /**
     * 科目体系id
     */
    private Long subSysId;
    /**
     * 科目体系名称
     */
    private String subSysName;
    /**
     * 凭证系统
     */
    private Integer voucherSys;
    /**
     * 映射单元类型（1 费项 2 辅助核算）
     */
    private Integer subMapType;
    /**
     * 费项属性： 1收入,2支出 3代收代付及其他
     */
    private Integer chargeItemAttribute;
    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;
    /**
     * 一级科目json [
    {
        "subjectId":"",
        "subjectName":""
    }
]
     */
    private String subjectLevelJson;
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

