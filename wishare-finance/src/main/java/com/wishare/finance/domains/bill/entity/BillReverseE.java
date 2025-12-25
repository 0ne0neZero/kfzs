package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 账单冲销表(BillReverse)实体类
 *
 * @author makejava
 * @since 2022-10-20 19:50:59
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_REVERSE, autoResultMap = true)
public class BillReverseE  implements BillDetailBase{
    /**
     * 主键id
     */
    private Long id;
    /**
     * 账单类型：1.应收账单 2.预收账单 3.临时缴费账单
     */
    private Integer billType;
    /**
     * 原账单id
     */
    private Long billId;
    /**
     * 审核记录id
     */
    private Long billApproveId;
    /**
     * 项目ID
     */
    private String communityId;
    /**
     * 申请冲销时间
     */
    private LocalDateTime approveTime;
    /**
     * 冲销通过时间
     */
    private LocalDateTime reverseTime;
    /**
     * 退款状态：冲销状态：0未生效，1已审核
     */
    private Integer state;
    /**
     * 原账单金额
     */
    private Long totalAmount;
    /**
     * 冲销账单id
     */
    private Long reverseBillId;
    /**
     * 冲销金额
     */
    private Long reverseAmount;

    /**
     * 结算渠道
     */
    private String payChannel;

    /**
     * 冲销原因
     */
    private String reason;

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

    /**
     * 初始化结算信息
     */
    public void init(){
        //自动加载结算账单id
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_REVERSE);
        }
    }

}

