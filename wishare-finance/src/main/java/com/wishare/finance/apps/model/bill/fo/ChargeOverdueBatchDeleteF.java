package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author fxl
 * @describe
 * @date 2024/5/7
 */
@Getter
@Setter
@ApiModel(value = "违约金管理批量删除Form")
public class ChargeOverdueBatchDeleteF {

    @ApiModelProperty(value = "项目id",required = true)
    @NotBlank(message = "项目id不能为空")
    private String communityId;

    @ApiModelProperty(value = "违约金管理数据id集合",required = true)
    @NotEmpty(message = "违约金管理数据id不能为空")
    private List<Long> overdueIds;
}
