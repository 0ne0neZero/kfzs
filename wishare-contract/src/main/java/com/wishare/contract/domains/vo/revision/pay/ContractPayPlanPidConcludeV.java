package com.wishare.contract.domains.vo.revision.pay;

import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/3/19/19:33
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同计划清单详情表视图对象", description = "支出合同计划清单详情表视图对象")
public class ContractPayPlanPidConcludeV {

    private List<ContractPayPlanConcludeV> concludeVList;

    private List<ContractPayFundV> contractPayFundVList;
}
