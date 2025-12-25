package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.apps.fo.revision.ContractPlanAddDetailF;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 收入合同付款计划信息表新增参数细节
 * 主要包含收款细节和税务细节
 *
 * @author 龙江锋
 * @date 2023/8/17 14:09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同付款计划信息表新增参数细节", description = "收入合同付款计划信息表新增参数细节")
public class ContractIncomePlanAddDetailF extends ContractPlanAddDetailF {
    // 在这里放收入合同特有独立的属性细节
}
