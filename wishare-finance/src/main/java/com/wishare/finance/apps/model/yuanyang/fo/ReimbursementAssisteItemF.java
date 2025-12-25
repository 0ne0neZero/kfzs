package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 辅助核算明细
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Getter
@Setter
@ApiModel("辅助核算明细")
public class ReimbursementAssisteItemF {

    @ApiModelProperty(value = "辅助核算类型： 1部门，2业务单元，3收支项目， 4业务类型，5客商，6增值税税率，7")
    @NotNull(message = "辅助核算类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "辅助核算编码")
    @NotBlank(message = "辅助核算编码不能为空")
    private String code;

    @ApiModelProperty(value = "辅助核算名称")
    @NotBlank(message = "辅助核算名称不能为空")
    private String name;

}
