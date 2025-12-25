package com.wishare.contract.domains.vo.settle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class PreSettlePlanDataV {

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("费项ID")
    private String chargeItemId;

    @ApiModelProperty("成本预估金额合计")
    private BigDecimal groupPlannedCollectionAmount;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;

    @ApiModelProperty(value = "成本预估分组数据")
    private List<PreSettlePlanGroupV> preSettlePlanGroupVList;
}
