package com.wishare.contract.domains.dto.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
* <p>
* 损益账单关联表
* </p>
*
* @author ljx
* @since 2022-10-17
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_profit_loss_bill请求对象", description = "损益账单关联表")
public class ContractProfitLossBillD {

    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("开票id")
    private Long invoiceId;
    @ApiModelProperty("中台账单ids, 逗号拼接")
    private String billId;

    @ApiModelProperty("租户id")
    private String tenantId;

}
