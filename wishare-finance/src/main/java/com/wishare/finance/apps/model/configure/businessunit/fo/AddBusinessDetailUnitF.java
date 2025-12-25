package com.wishare.finance.apps.model.configure.businessunit.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建业务单元入参
 *
 * @author
 */
@Getter
@Setter
@ApiModel("新建业务单元关联数据")
public class AddBusinessDetailUnitF {

    @ApiModelProperty(value = "业务单元id", required = true)
    @NotNull(message = "业务单元id不能为空")
    private Long id;

    @ApiModelProperty("银行账号id列表")
    private List<Long> statutoryBodyAccountId;
}
