package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
/**
* <p>
* 损益账单关联表 更新请求参数
* </p>
*
* @author ljx
* @since 2022-10-17
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_profit_loss_bill", description = "损益账单关联表")
public class ContractProfitLossBillUpdateF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("损益计划id")
    private Long profitLossPlanId;
    @ApiModelProperty("中台账单id")
    private Long billId;

}
