package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/28/13:34
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同收款计划信息表列表请求参数", description = "收入合同收款计划信息列表")
public class ContractIncomePlanConcludeListF {

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("第几批")
    private Integer howOrder;

    @ApiModelProperty("合同名称和编码")
    private String nameNo;


}
