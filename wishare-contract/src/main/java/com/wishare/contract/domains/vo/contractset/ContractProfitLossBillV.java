package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractProfitLossBillV {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("损益计划id")
    private Long profitLossPlanId;
    @ApiModelProperty("中台账单id")
    private Long billId;

}
