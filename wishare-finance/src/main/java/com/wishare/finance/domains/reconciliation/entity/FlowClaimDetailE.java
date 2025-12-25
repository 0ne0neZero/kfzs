package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 流水领用明细
 *
 * @author yancao
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flow_claim_detail")
@ToString
public class FlowClaimDetailE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 流水领用记录id
     */
    private Long flowClaimRecordId;

    /**
     * 关联的发票id
     */
    private Long invoiceId;

    /**
     * 关联的流水id
     */
    private Long flowId;

    /**
     * 发票金额
     */
    private Long invoiceAmount;

    /**
     * 流水金额
     */
    private Long flowAmount;

    /**
     * 状态：0正常，1已撤销
     */
    private Integer state;

    /**
     * 系统来源 1收费系统，2合同系统
     */
    private Integer sysSource;

    /**
     * 认领类型：1:发票认领;2:账单认领;
     */
    private Integer claimType;

    /**
     * 认领ID类型：1:发票;2:收款单;3:退款单;
     */
    private Integer claimIdType;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

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
            setId(IdentifierFactory.getInstance().generateLongIdentifier("flow_claim_detail_id"));
        }
    }

}
