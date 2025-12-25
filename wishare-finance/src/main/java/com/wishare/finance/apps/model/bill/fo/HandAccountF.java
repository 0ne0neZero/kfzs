package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 发起交账信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Getter
@Setter
@ApiModel("发起交账信息")
public class HandAccountF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

}
