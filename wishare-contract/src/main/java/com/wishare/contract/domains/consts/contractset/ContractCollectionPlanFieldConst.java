package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractCollectionPlan 表字段常量 <br>
*
* id,chargeItemId,costId,contractId,orgId,budgetAccount,abstract,billType,bidBond,plannedCollectionTime,plannedCollectionAmount,localCurrencyAmount,taxExcludedAmount,profitLossAccounting,serviceStartDate,serviceEndDate,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
public interface ContractCollectionPlanFieldConst {

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
     * 成本中心名称
     */
    String COST_NAME = "costName";

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
    String ABSTRACT = "abstract";

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
     * 计划收款金额（原币/含税）
     */
    String PLANNED_COLLECTION_AMOUNT = "plannedCollectionAmount";

    /**
     * 本币金额（含税）
     */
    String LOCAL_CURRENCY_AMOUNT = "localCurrencyAmount";

    /**
     * 本币金额（不含税）
     */
    String TAX_EXCLUDED_AMOUNT = "taxExcludedAmount";

    /**
     * 损益核算方式
     */
    String PROFIT_LOSS_ACCOUNTING = "profitLossAccounting";

    /**
     * 服务开始日期
     */
    String SERVICE_START_DATE = "serviceStartDate";

    /**
     * 服务结束日期
     */
    String SERVICE_END_DATE = "serviceEndDate";

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

    /**
     * 合同收款计划信息表
     */
    String COLLECTION_PLAN = "contract_collection_plan";
}
