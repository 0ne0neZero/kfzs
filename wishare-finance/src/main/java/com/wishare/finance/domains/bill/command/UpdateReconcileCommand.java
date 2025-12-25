package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 更新对账命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/24
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("更新对账命令")
public class UpdateReconcileCommand {

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty(value = "账单id列表")
    private List<Long> billIds;

    @ApiModelProperty("对账模式: 0账票流水对账，1商户清分对账")
    private Integer reconcileMode;

}
