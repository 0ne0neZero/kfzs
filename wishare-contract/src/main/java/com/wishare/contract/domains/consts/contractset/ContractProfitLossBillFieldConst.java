package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractProfitLossBill 表字段常量 <br>
*
* id,collectionPlanId,profitLossPlanId,billId
* </p>
*
* @author ljx
* @since 2022-10-17
*/
public interface ContractProfitLossBillFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 收款计划id
     */
    String COLLECTION_PLAN_ID = "collectionPlanId";

    /**
     * 损益计划id
     */
    String PROFIT_LOSS_PLAN_ID = "profitLossPlanId";

    /**
     * 中台账单id
     */
    String BILL_ID = "billId";


    String CONTRACT_PROFIT_LOSS_BILL = "contract_profit_loss_bill";
}
