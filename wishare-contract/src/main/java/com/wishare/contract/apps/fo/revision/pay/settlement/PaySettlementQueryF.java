package com.wishare.contract.apps.fo.revision.pay.settlement;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
public class PaySettlementQueryF {

    @ApiModelProperty(value = "合同id")
    private String contractId;


    @ApiModelProperty(value = "结算单id集合")
    private List<String> settlementIdList;
}
