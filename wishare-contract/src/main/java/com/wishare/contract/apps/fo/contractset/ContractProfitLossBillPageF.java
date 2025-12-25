package com.wishare.contract.apps.fo.contractset;


import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.wishare.contract.domains.consts.contractset.ContractProfitLossBillFieldConst;
/**
* <p>
* 损益账单关联表 分页请求参数
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
public class ContractProfitLossBillPageF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("损益计划id")
    private Long profitLossPlanId;
    @ApiModelProperty("中台账单id")
    private Long billId;
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"collectionPlanId\",\"profitLossPlanId\",\"billId\"]"
        + "id id"
        + "collectionPlanId 收款计划id"
        + "profitLossPlanId 损益计划id"
        + "billId 中台账单id")
    private List<String> fields;


}
