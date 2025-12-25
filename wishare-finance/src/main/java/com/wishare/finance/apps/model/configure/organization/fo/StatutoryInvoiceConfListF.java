package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/11/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("列表查询")
public class StatutoryInvoiceConfListF {

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;
}
