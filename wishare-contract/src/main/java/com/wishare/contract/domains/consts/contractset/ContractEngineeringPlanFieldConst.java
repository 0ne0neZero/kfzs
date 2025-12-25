package com.wishare.contract.domains.consts.contractset;

/**
 * <p>
 * ContractEngineeringPlan 表字段常量 <br>
 * <p>
 * id,contractId,profitLossId,tenantId,accrualCode,lastPercent,lastAmount,thisTimePercent,thisTimeAmount,accrualAmount,accrualData,remarks,creator,creatorName,gmtCreate,operator,operatorName,gmtModify,deleted
 * </p>
 *
 * @author wangrui
 * @since 2022-11-29
 */
public interface ContractEngineeringPlanFieldConst {

    /**
     * 主键
     */
    String ID = "id";

    /**
     * 合同id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 损益计划Id
     */
    String PROFIT_LOSS_ID = "profitLossId";

    /**
     * 租户Id
     */
    String TENANT_ID = "tenantId";

    /**
     * 计提编号
     */
    String ACCRUAL_CODE = "accrualCode";

    /**
     * 上次工程完成百分比
     */
    String LAST_PERCENT = "lastPercent";

    /**
     * 上次工程总金额
     */
    String LAST_AMOUNT = "lastAmount";

    /**
     * 本次工程百分比
     */
    String THIS_TIME_PERCENT = "thisTimePercent";

    /**
     * 本次工程总金额
     */
    String THIS_TIME_AMOUNT = "thisTimeAmount";

    /**
     * 本次计提金额
     */
    String ACCRUAL_AMOUNT = "accrualAmount";

    /**
     * 计提资料
     */
    String ACCRUAL_DATA = "accrualData";

    /**
     * 备注
     */
    String REMARKS = "remarks";

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

    /**
     * 工程类合同计提信息表
     */
    String CONTRACT_ENGINEERING = "contract_engineering_plan";
}
