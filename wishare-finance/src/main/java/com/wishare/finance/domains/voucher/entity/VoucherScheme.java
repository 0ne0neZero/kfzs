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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证核算方案表
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@TableName(value = TableNames.VOUCHER_SCHEME, autoResultMap = true)
@ApiModel(value="凭证核算方案信息")
public class VoucherScheme {

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "方案名称")
    private String name;

    @ApiModelProperty(value = "是否启用：0未启用，1启用")
    private Integer disabled;

    @ApiModelProperty(value = "财务组织信息")
    @TableField(exist = false)
    private List<VoucherSchemeOrgOBV> orgs;

    @ApiModelProperty(value = "财务组织信息")
    @TableField(exist = false)
    private List<VoucherSchemeRuleOBV> rules;

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
