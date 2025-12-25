package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 凭证核算方案财务组织关联表
 * </p>
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@TableName(TableNames.VOUCHER_SCHEME_ORG)
@ApiModel(value="VoucherSchemeOrg对象", description="凭证核算方案财务组织关联表")
public class VoucherSchemeOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "核算方案id")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "财务组织id")
    private Long orgId;

    @ApiModelProperty(value = "财务组织编码")
    private String orgCode;

    @ApiModelProperty(value = "财务组织名称")
    private String orgName;

    @ApiModelProperty(value = "关联类型： 1法定单位，2成本中心")
    private Integer orgType;

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
