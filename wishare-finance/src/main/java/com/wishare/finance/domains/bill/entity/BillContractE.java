package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillContractE
 * @date 2024.07.03  11:48
 * @description 账单关联合同
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(TableNames.BILL_CONTRACT)
public class BillContractE {

    @TableId
    private Long id;

    @ApiModelProperty("账单ID")
    private Long billId;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("备注")
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

    public BillContractE() {
    }

    public BillContractE(TemporaryChargeBill bill) {
        this.id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CONTRACT);
        this.billId = bill.getId();
        this.communityId = bill.getCommunityId();
        this.contractNo = bill.getContractNo();
        this.contractName = bill.getContractName();
    }
}
