package com.wishare.contract.apps.fo.revision.pay;

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
@ApiModel(value = "支出合同付款计划信息表列表请求参数", description = "支出合同付款计划信息列表")
public class ContractPaySettlementConcludeListF {

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("结算单")
    private String id;


}
