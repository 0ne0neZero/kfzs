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

/**
 * 费用报账主表
 */
@Getter
@Setter
@TableName(TableNames.EXPENSE_REPORT)
public class ExpenseReportE {

    private Long id;
    /**
     * 业务场景id
     */
    private Long sceneId;
    /**
     * 业务场景：1、发票计提 2、收款
     */
    private Integer sceneType;
    /**
     * 外部系统业务单据编号（如金蝶）
     */
    private String externalBillNo;
    /**
     * 业务单据id
     */
    private Long businessBillId;
    /**
     * 业务单据编号
     */
    private String businessBillNo;
    /**
     * 业务日期
     */
    private String bizDate;
    /**
     * 收款类型
     */
    // private String receivebilltype;
    /**
     * 收款账户
     */
    // private String receiveaccount;
    /**
     * 客户
     */
    private String customer;
    /**
     * 发票/收据
     */
    // private String invoicetype;
    /**
     * 结算方式
     */
    // private String settletype;
    /**
     * 组织
     */
    private String org;

    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 初始化id
     */
    public void generateIdentifier() {
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.EXPENSE_REPORT));
        }
    }

    public ExpenseReportE() {
        generateIdentifier();
    }

}
