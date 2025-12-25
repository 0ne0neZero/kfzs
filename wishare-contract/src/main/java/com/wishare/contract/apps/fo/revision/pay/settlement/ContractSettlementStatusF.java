package com.wishare.contract.apps.fo.revision.pay.settlement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ContractSettlementStatusF {

    @ApiModelProperty(value = "结算单id")
    private List<String> settlementIdList;
    @ApiModelProperty(value = "待修改状态")
    private Integer applyStatus;

}
