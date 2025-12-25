package com.wishare.finance.infrastructure.remote.vo.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ContractSettlementF {

    @ApiModelProperty(value = "结算单id")
    private List<String> settlementIdList;
    @ApiModelProperty(value = "待修改状态")
    private Integer applyStatus;

}
