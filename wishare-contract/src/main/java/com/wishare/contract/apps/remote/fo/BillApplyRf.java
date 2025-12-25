package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("账单申请信息")
public class BillApplyRf {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty(value = "审核原因")
    private String reason;

    @ApiModelProperty(value = "审核操作类型 1调整，2作废，3结转，4退款，5冲销")
    private Integer approveOperateType;

    @ApiModelProperty(value = "审核详情（该字段为JSON字符串，根据不同操作传不同参数）")
    private String detail;
}
