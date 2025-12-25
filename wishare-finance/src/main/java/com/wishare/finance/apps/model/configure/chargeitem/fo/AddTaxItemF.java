package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 新增税目信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("新增税目信息")
public class AddTaxItemF {

    @ApiModelProperty("编码")
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("关联的费项id")
    private List<Long> chargeItemIdList;

}
