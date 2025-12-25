package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/1/29/13:57
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractSrfxxPayAccountExtendF {

    @ApiModelProperty("收入方账户-账户名称")
    private String name;

    @ApiModelProperty("收入方账户-账号")
    private String id;


}
