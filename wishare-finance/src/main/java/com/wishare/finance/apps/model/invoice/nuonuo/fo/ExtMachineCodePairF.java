package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("分机号+机器编号联合查询入参")
public class ExtMachineCodePairF {

    @ApiModelProperty("分机号")
    private String extensionNums;

    @ApiModelProperty("机器编号")
    private String machineCode;
}
