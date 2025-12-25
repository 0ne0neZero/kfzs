package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 辅助核算（收支项目）
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Getter
@Setter
@ApiModel(value="辅助核算（收支项目）", description="辅助核算（收支项目）")
public class AssisteInoutclassF {

    @ApiModelProperty(value = "辅助核算部门编码")
    private String code;

    @ApiModelProperty(value = "辅助核算部门名称")
    private String name;

    @ApiModelProperty(value = "辅助核算编码")
    private String ascCode;

    @ApiModelProperty(value = "辅助核算名称")
    private String ascName;

    @ApiModelProperty(value = "父级编码")
    private String parentCode;

    @ApiModelProperty(value = "凭证系统 ：1用友NCC")
    private Integer syncSystem;

}
