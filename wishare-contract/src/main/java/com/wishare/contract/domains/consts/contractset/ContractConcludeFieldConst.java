package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractConclude 表字段常量 <br>
*
* id,name,tenantId,communityId,partyAName,PartyBName,category,contractNature,amountTaxIncluded,amountTaxExcluded,estimatedAmount,gmtExpireStart,gmtExpireEnd,virtualContract,backdatingContract,signingMethod,contractState,bondAmount,bidBond,bidBondName,bidBondAmount,projectSource,businessType,propertyModel,propertyFeePrice,expenditureContract,contractAbstract,referenceModel,ModelName,incomeContract,contractText,contractEnclosure,otherDocuments,reviewStatus,procId,handledBy,orgId,bond,chargeItemId,bondType,frameworkContract,creator,creatorName,gmtCreate,operator,operatorName,gmtModify,deleted
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
public interface ContractConcludeFieldConst {

    /**
     * 合同id
     */
    String ID = "id";

    /**
     * 合同名称
     */
    String NAME = "name";

    /**
     * 公司id 来源 org_tenant.id
     */
    String TENANT_ID = "tenantId";

    /**
     * 项目id 来源 space_community.id
     */
    String COMMUNITY_ID = "communityId";

    /**
     * 所属部门ID
     */
    String BELONG_ORG_ID = "belongOrgId";

    /**
     * 所属部门名称
     */
    String BELONG_ORG_NAME = "belongOrgName";

    /**
     * 所属部门组织路径
     */
    String BELONG_ORG_PATH = "belongOrgPath";

    /**
     * 甲方名称
     */
    String PARTY_ANAME = "partyAName";

    /**
     * 乙方名称
     */
    String PARTY_BNAME = "PartyBName";

    /**
     * 合同分类Id
     */
    String CATEGORY = "category";

    /**
     * 合同性质 1 收入 2 支出
     */
    String CONTRACT_NATURE = "contractNature";

    /**
     * 金额（含税）
     */
    String AMOUNT_TAX_INCLUDED = "amountTaxIncluded";

    /**
     * 金额（不含税）
     */
    String AMOUNT_TAX_EXCLUDED = "amountTaxExcluded";

    /**
     * 合同预估金额
     */
    String ESTIMATED_AMOUNT = "estimatedAmount";

    /**
     * 金额（不含税）
     */
    String GMT_EXPIRE_START = "gmtExpireStart";

    /**
     * 合同生效日期
     */
    String GMT_EXPIRE_END = "gmtExpireEnd";

    /**
     * 是否虚拟合同 0 否 1 是
     */
    String VIRTUAL_CONTRACT = "virtualContract";

    /**
     * 是否倒签合同 0 否 1 是
     */
    String BACKDATING_CONTRACT = "backdatingContract";

    /**
     * 签约方式
     */
    String SIGNING_METHOD = "signingMethod";

    /**
     * 合同状态 1 未履行 2 履行中 3 已到期 4 已终止
     */
    String CONTRACT_STATE = "contractState";

    /**
     * 保证金额
     */
    String BOND_AMOUNT = "bondAmount";

    /**
     * 是否关联招投保证金（收入类为招标保证金，支出类为投标）
     */
    String BID_BOND = "bidBond";

    /**
     * 招投保证金名称
     */
    String BID_BOND_NAME = "bidBondName";

    /**
     * 招投保证金金额
     */
    String BID_BOND_AMOUNT = "bidBondAmount";

    /**
     * 项目来源
     */
    String PROJECT_SOURCE = "projectSource";

    /**
     * 业态
     */
    String BUSINESS_TYPE = "businessType";

    /**
     * 物业模式
     */
    String PROPERTY_MODEL = "propertyModel";

    /**
     * 物业费单价
     */
    String PROPERTY_FEE_PRICE = "propertyFeePrice";

    /**
     * 关联支出类合同
     */
    String EXPENDITURE_CONTRACT = "expenditureContract";

    /**
     * 合同内容摘要
     */
    String CONTRACT_ABSTRACT = "contractAbstract";

    /**
     * 是否引用范本 0 否 1 是
     */
    String REFERENCE_MODEL = "referenceModel";

    /**
     * 范本名称
     */
    String MODEL_NAME = "ModelName";

    /**
     * 关联收入类合同
     */
    String INCOME_CONTRACT = "incomeContract";

    /**
     * 合同文本
     */
    String CONTRACT_TEXT = "contractText";

    /**
     * 合同附件
     */
    String CONTRACT_ENCLOSURE = "contractEnclosure";

    /**
     * 其他说明文件
     */
    String OTHER_DOCUMENTS = "otherDocuments";

    /**
     * 审核状态 1 通过 2 未提交 3 审批中 4 已驳回
     */
    String REVIEW_STATUS = "reviewStatus";

    /**
     * 审批流id
     */
    String PROC_ID = "procId";




    /**
     * 经办人ID
     */
    String HANDLER_ID = "handlerId";
    /**
     * 经办人
     */
    String HANDLED_BY = "handledBy";

    /**
     * 经办人所在部门
     */
    String ORG_ID = "orgId";

    /**
     * 是否保证金 0 否 1 是
     */
    String BOND = "bond";

    /**
     * 费项Id
     */
    String CHARGE_ITEM_ID = "chargeItemId";

    /**
     * 保证金类型
     */
    String BOND_TYPE = "bondType";

    /**
     * 是否框架合同 0 否 1 是
     */
    String FRAMEWORK_CONTRACT = "frameworkContract";

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
     * 合同订立信息表
     */
    String CONTRACT_INFO = "contract_conclude";
}
