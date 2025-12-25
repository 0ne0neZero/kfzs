package com.wishare.contract.domains.vo.revision.income;

import com.wishare.contract.domains.vo.revision.income.fund.ContractIncomeFundV;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同计划清单详情表视图对象", description = "支出合同计划清单详情表视图对象")
public class ContractIncomePlanPidConcludeV {

    private List<ContractIncomePlanConcludeV> concludeVList;

    private List<ContractIncomeFundV> contractPayFundVList;
}
