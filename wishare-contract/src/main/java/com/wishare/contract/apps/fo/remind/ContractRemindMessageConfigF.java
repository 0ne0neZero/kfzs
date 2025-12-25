package com.wishare.contract.apps.fo.remind;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ContractRemindMessageConfigF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "合同到期预警")
    private List<RemindRuleDetailF> expiredConfigs;

    @ApiModelProperty(value = "收款预警")
    private List<RemindRuleDetailF> incomeConfigs;

    @ApiModelProperty(value = "收款逾期预警")
    private List<RemindRuleDetailF> overdueConfigs;

}
