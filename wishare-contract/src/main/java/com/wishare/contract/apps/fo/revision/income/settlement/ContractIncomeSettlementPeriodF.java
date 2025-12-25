package com.wishare.contract.apps.fo.revision.income.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel("计量周期信息")
public class ContractIncomeSettlementPeriodF {

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

}
