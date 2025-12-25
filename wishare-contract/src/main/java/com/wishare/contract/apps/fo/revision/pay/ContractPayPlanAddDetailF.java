package com.wishare.contract.apps.fo.revision.pay;

import com.wishare.contract.apps.fo.revision.ContractPlanAddDetailF;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 支出合同付款计划信息表新增参数细节
 * 主要包含收款细节和税务细节
 *
 * @author 龙江锋
 * @date 2023/8/17 11:33
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同付款计划信息表新增参数细节", description = "支出合同付款计划信息表新增参数细节")
public class ContractPayPlanAddDetailF extends ContractPlanAddDetailF {
    // 在这里放支出合同特有独立的属性细节
}
