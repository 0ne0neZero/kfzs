package com.wishare.finance.domains.expensereport.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(TableNames.EXPENSE_REPORT_DETAIL)
public class ExpenseReportDetailE {

    private Long id;

    /**
     * 费用报账主表id
     */
    private Long expenseReportId;
    /**
     * 价税合计
     */
    private Long totalAmount = 0L;
    /**
     * 不含税金额
     */
    private Long amount = 0L;
    /**
     * 税率（%）
     */
    private String taxRate;
    /**
     * 税额
     */
    private Long taxAmount;
    /**
     * 项目
     */
    private String item;
    /**
     * 业务板块
     */
    private String businessSegmentCode;
    /**
     * 收费系统费项
     */
    private String chargeItemCode;
    /**
     * 是否删除：0否，1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;
    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.EXPENSE_REPORT_DETAIL));
        }
    }

    public ExpenseReportDetailE() {
        generateIdentifier();
    }
}
