package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.domains.bill.consts.enums.BillApproveRefundStateEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 扣款单表(DeductionRecord)实体类
 *
 * @author 杨志
 * @since 2023-05-11
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(TableNames.BILL_DEDUCTION)
public class BillDeductionE {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 账单id
     */
    private Long billId;
    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer billType;
    /**
     * 退款金额（单位：分）
     */
    private Long deductionAmount;
    /**
     * 备注
     */
    private String remark;
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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
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
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 自动加载账单id
     * @return
     */
    public BillDeductionE generateIdentifier() {
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_APPROVE);
        }
        return this;
    }


}

