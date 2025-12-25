package com.wishare.contract.domains.entity.revision.pay.bill;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/17/14:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_settlements_fund")
public class ContractSettlementsFundE {


    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    private String settlementId;

    /**
     * 收付款单编号
     */
    private String payNotecode;


    private String contractId;

    /**
     * 收付方式:0收，1付
     */
    private String fundType;

    private String type;

    /**
     * 收付款金额
     */
    private BigDecimal amount;


    /**
     * 收付款日期
     */
    private LocalDate collectTime;

    /**
     * 备注
     */
    private String remark;

    private String attachments;


    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

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
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    private Integer reviewStatus;


    /**
     * 主键ID
     */
    public static final String ID = "id";

}
