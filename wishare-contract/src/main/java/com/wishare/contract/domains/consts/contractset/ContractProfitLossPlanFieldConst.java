package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractProfitLossPlan 表字段常量 <br>
*
* id,chargeItemId,costId,contractId,orgId,budgetAccount,summary,billType,taxRate,confirmTime,amountTaxIncluded,localCurrencyAmount,taxExcludedAmount,serviceStartDate,serviceEndDate,profitLossAccounting,creator,creatorName,gmtCreate,operator,operatorName,gmtModify,deleted
* </p>
*
* @author wangrui
* @since 2022-09-13
*/
public interface ContractProfitLossPlanFieldConst {

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
     * 税率
     */
    String TAX_RATE = "taxRate";

    /**
     * 损益确认时间
     */
    String CONFIRM_TIME = "confirmTime";

    /**
     * 计划损益金额（原币/含税）
     */
    String AMOUNT_TAX_INCLUDED = "amountTaxIncluded";

    /**
     * 本币金额（含税）
     */
    String LOCAL_CURRENCY_AMOUNT = "localCurrencyAmount";

    /**
     * 本币金额（不含税）
     */
    String TAX_EXCLUDED_AMOUNT = "taxExcludedAmount";

    /**
     * 服务开始日期
     */
    String SERVICE_START_DATE = "serviceStartDate";

    /**
     * 服务结束日期
     */
    String SERVICE_END_DATE = "serviceEndDate";

    /**
     * 损益核算方式
     */
    String PROFIT_LOSS_ACCOUNTING = "profitLossAccounting";

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
     * 是否删除  0 正常 1 删除
     */
    String DELETED = "deleted";
}
