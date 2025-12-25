package com.wishare.finance.domains.configure.chargeitem.command.tax;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/1
 * @Description: 查询税种command
 */
@Getter
@Setter
public class GetTaxCategoryCommand {

    @ApiModelProperty("税种id")
    private Long id;

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("租户id")
    private String tenantId;
}
