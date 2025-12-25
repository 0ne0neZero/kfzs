package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ApiModel("保证金计划付/退款入参")
public class ContractBondPlanPaymentF {

    @ApiModelProperty("合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty("付/退类型  1 付 2退 3扣")
    private Integer type;

    @ApiModelProperty("保证金类型 0 收取类 1缴纳类")
    private Integer bondType;

    @ApiModelProperty("保证金计划id、金额")
    private List<BondPlanAmountF> bondPlanAmountFList;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("付/退款方式  0现金  1银行转帐  2汇款  3支票")
    private Integer paymentMethod;

    @ApiModelProperty("付/退款时间")
    private LocalDateTime applyPaymentTime;

    @ApiModelProperty("备注")
    private String remark;
}
