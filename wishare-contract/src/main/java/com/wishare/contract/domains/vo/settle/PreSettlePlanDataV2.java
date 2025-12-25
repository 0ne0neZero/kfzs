package com.wishare.contract.domains.vo.settle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-13
 */
@Data
public class PreSettlePlanDataV2 {

    @ApiModelProperty("费项")
    private String chargeItem;

    @ApiModelProperty("费项ID")
    private String chargeItemId;

    @ApiModelProperty("成本预估金额合计")
    private BigDecimal groupPlannedCollectionAmount;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "结算周期名称")
    private String splitModeName;

    @ApiModelProperty(value = "成本预估分组数据")
    private List<PayConcludePlanV2> planVList;

    @ApiModelProperty(value = "补充合同ID")
    private String supplyContractId;

    @ApiModelProperty(value = "补充合同开始日期")
    private LocalDate supplyStartTime;

    @ApiModelProperty(value = "补充合同结束日期")
    private LocalDate supplyEndTime;
}
