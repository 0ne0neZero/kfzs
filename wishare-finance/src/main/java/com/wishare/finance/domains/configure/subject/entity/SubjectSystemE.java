package com.wishare.finance.domains.configure.subject.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 财务科目体系实体
 *
 * @author yancao
 */
@Getter
@Setter
@TableName(TableNames.SUBJECT_SYSTEM)
public class SubjectSystemE {

    /**
     * 科目体系id
     */
    @TableId
    private Long id;

    /**
     * 科目体系编码
     */
    private String code;

    /**
     * 科目体系名称
     */
    private String name;

    /**
     * 所属方唯一标识
     */
    private String pertainId;

    /**
     * 是否删除：0否，1是
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

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
    /**
     * 数据来源id
     */
    private String idExt;

    public void init(){
        if (Objects.isNull(id)){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.SUBJECT_SYSTEM);
        }
    }

}
