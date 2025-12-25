package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增税目信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("修改税目信息")
public class UpdateTaxItemF {

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private String id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("关联的费项id")
    private List<Long> chargeItemIdList;

}
