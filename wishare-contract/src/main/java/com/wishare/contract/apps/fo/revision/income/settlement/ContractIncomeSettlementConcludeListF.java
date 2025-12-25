package com.wishare.contract.apps.fo.revision.income.settlement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/28/13:34
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "确收单-获取编辑信息请求参数", description = "确收单-获取编辑信息请求参数")
public class ContractIncomeSettlementConcludeListF {

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("结算单")
    @NotBlank(message = "结算单id不能为空")
    private String id;


}
