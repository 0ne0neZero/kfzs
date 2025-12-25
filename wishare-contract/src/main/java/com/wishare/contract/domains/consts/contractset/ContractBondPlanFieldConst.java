package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractBondPlan 表字段常量 <br>
*
* id,chargeItemId,costId,contractId,orgId,budgetAccount,summary,billType,bidBond,plannedCollectionTime,plannedCollectionAmount,localCurrencyAmount,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
public interface ContractBondPlanFieldConst {

    /**
     * 主键
     */
    String ID = "id";

    /**
     * 费项Id
     */
    String CHARGE_ITEM_ID = "chargeItemId";

    /**
     * 成本中心Id
     */
    String COST_ID = "costId";

    /**
     * 合同Id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 责任部门（组织id）
     */
    String ORG_ID = "orgId";

    /**
     * 预算科目
     */
    String BUDGET_ACCOUNT = "budgetAccount";

    /**
     * 摘要
     */
    String SUMMARY = "summary";

    /**
     * 票据类型
     */
    String BILL_TYPE = "billType";

    /**
     * 招投标保证金
     */
    String BID_BOND = "bidBond";

    /**
     * 计划收款时间
     */
    String PLANNED_COLLECTION_TIME = "plannedCollectionTime";

    /**
     * 计划收款金额（原币）
     */
    String PLANNED_COLLECTION_AMOUNT = "plannedCollectionAmount";

    /**
     * 本币金额（元）
     */
    String LOCAL_CURRENCY_AMOUNT = "localCurrencyAmount";

    /**
     * 创建人
     */
    String CREATOR = "creator";

    /**
     * 创建人名称
     */
    String CREATOR_NAME = "creatorName";

    /**
     * 创建时间
     */
    String GMT_CREATE = "gmtCreate";

    /**
     * 操作人
     */
    String OPERATOR = "operator";

    /**
     * 操作人名称
     */
    String OPERATOR_NAME = "operatorName";

    /**
     * 操作时间
     */
    String GMT_MODIFY = "gmtModify";

}
