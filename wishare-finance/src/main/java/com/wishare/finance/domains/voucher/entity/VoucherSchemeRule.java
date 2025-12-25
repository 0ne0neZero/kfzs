package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 凭证核算方案凭证规则关联表
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(TableNames.VOUCHER_SCHEME_RULE)
@ApiModel(value="VoucherSchemeRule对象", description="凭证核算方案凭证规则关联表")
public class VoucherSchemeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "核算方案id")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "凭证规则id")
    private Long voucherRuleId;

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

}
