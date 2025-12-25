package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 第三方流程创建中间表
 *
 * @author long
 * @date 2023/7/18 11:58
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_process_record")
@ApiModel("第三方流程创建中间表")
public class ContractProcessRecordE {
    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 流程id
     */
    @ApiModelProperty("流程id")
    private String processId;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String contractId;

    /**
     * 类型（1合同订立支出2合同订立收入）
     */
    @ApiModelProperty("类型（1合同订立支出2合同订立收入）")
    private Integer type;

    /**
     * 提交类型（1合同提交2修改提交）
     */
    @ApiModelProperty("提交类型（1合同提交2修改提交）")
    private Integer subType;

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    private String tenantId;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 是否删除（0否1是）
     */
    @ApiModelProperty("是否删除（0否1是）")
    @TableLogic
    private Boolean deleted;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;

    /**
     * 审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批
     */
    @ApiModelProperty("审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批")
    private Integer reviewStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectReason;

    /**
     * 提交类型
     */
    public static final String SUBTYPE = "subType";
}
