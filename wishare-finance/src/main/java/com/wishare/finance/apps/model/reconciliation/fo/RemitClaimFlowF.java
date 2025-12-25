package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("汇款流水认领参数")
public class RemitClaimFlowF {

    @ApiModelProperty("汇款金额")
    @NotNull(message = "汇款金额不能为空")
    @DecimalMin(value = "0.00", message = "汇款金额不能小于0")
    private BigDecimal remitAmount;

//    @ApiModelProperty("账单类型")
//    @NotNull(message = "账单类型不能为空")
//    private Integer billType;

    @ApiModelProperty("收款账单ID")
    @NotNull(message = "收款账单ID不能为空")
    private Long billId;

    @ApiModelProperty("流水id集合")
    @NotEmpty(message = "流水id集合不能为空")
    private List<Long> flowIdList;
}
