package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 辅助核算（部门）
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Getter
@Setter
@ApiModel(value="辅助核算（部门）", description="辅助核算（部门）")
public class AssisteOrgDeptF {

    @ApiModelProperty(value = "辅助核算部门编码")
    private String code;

    @ApiModelProperty(value = "辅助核算部门名称")
    private String name;

    @ApiModelProperty(value = "辅助核算编码")
    private String ascCode;

    @ApiModelProperty(value = "辅助核算名称")
    private String ascName;

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "组织编码")
    private String orgCode;

    @ApiModelProperty(value = "组织名称")
    private String orgName;

    @ApiModelProperty(value = "上级部门编码")
    private String supCode;

    @ApiModelProperty(value = "上级部门名称")
    private String supName;

    @ApiModelProperty(value = "凭证系统 ：1用友NCC")
    private Integer syncSystem;

}
