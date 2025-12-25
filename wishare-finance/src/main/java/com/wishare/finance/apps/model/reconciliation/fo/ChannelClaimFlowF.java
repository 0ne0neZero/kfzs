package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("支付流水认领参数")
public class ChannelClaimFlowF {
    @ApiModelProperty("领用金额")
    private BigDecimal claimAmount;

    @ApiModelProperty("账单编号集合")
    @NotEmpty(message = "账单编号集合不能为空")
    @Size(min = 1, message = "收款单数量不能为空")
    @Size(max = 50, message = "收款单数量不能大于50")
    private List<String> billNoList;

    @ApiModelProperty("支付流水集合")
    @NotEmpty(message = "支付流水不能为空")
    private List<String> tradeNoList;

    @ApiModelProperty("上级收费单元ID")
    private String supCpUnitId;
}
