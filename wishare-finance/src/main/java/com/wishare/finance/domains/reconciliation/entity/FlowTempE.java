package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 流水、暂存记录关联表
 */
@Setter
@Getter
@TableName("flow_temp")
public class FlowTempE {
    @TableId
    private Long id;

    private Long flowTempRecordId;

    private String flowTempRecordName;

    private Long flowDetailId;

    private String flowSerialNumber;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
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

    public void init() {
        if (Objects.isNull(id)){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("flow_temp"));
        }
    }
}
