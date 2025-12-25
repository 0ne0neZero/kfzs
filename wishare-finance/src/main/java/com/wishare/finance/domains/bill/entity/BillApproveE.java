package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 账单审核记录表，管理账单审核记录数据
 * </p>
 *
 * @author dxclay
 * @since 2022-08-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(TableNames.BILL_APPROVE)
public class BillApproveE implements BillDetailBase {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 账单ID
     */
    private Long billId;

    /**
     * 外部审批标识
     */
    private String outApproveId;

    /**
     * 审核原因
     */
    private String reason;

    /**
     * 审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款
     */
    private Integer operateType;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer billType;

    /**
     * 上级收费单元id（应收账单分表字段）
     */
    private String supCpUnitId;

    /**
     * 审核状态（1审核中，2已审核，3未通过）
     */
    private Integer approvedState;

    /**
     * 审核结果说明
     */
    private String approvedRemark;

    /**
     * 上次审批的状态
     */
    private Integer lastApproveState;

    /**
     * 审核类型： 0内部审核，1外部审核
     */
    private Integer approveType;

    /**
     * 扩展字段1(根据业务需要自行设置)
     */
    private String extField1;

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
     * 审核后操作：1退款，2转预收
     */
    @TableField(exist = false)
    private Integer approvedAction;

    /**
     * 转预收的费项id
     */
    @TableField(exist = false)
    private Long chargeItemId;

    /**
     * 转预收的费项名称
     */
    @TableField(exist = false)
    private String chargeItemName;

    /**
     * 操作id
     */
    private Long operationId;

    /**
     * 自动加载账单id
     * @return
     */
    public BillApproveE generateIdentifier() {
        if (id == null){
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_APPROVE);
        }
        return this;
    }
}
