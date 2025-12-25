package com.wishare.finance.apps.model.configure.arrearsCategory.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@Setter
@ApiModel("新增欠费原因数据")
public class CreateArrearsReasonF {

    @ApiModelProperty(value ="欠费类别id", required = true)
    @NotNull(message = "欠费类别名称不能为空")
    private Long arrearsCategoryId;

    @ApiModelProperty(value = "欠费类别名称", required = true)
    @NotBlank(message = "欠费类别名称不能为空")
    private String arrearsCategoryName;

    @ApiModelProperty(value ="账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private List<Long> billId;

    @ApiModelProperty("欠费原因")
    private String arrearsReason;

    @ApiModelProperty
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty("客户等级")
    private String customerLevel;

}
