package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractInvoiceDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,invoiceApplyNumber,invoiceApplyAmount,invoiceApplyTime,remark,userId,userName,invoiceTax,invoiceStatus,invoiceNumber,auditStatus,auditNumber,confirmStatus,tenantId,deleted,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author ljx
* @since 2022-09-26
*/
public interface ContractInvoiceDetailFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 合同Id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 收款计划id
     */
    String COLLECTION_PLAN_ID = "collectionPlanId";

    /**
     * 开票申请编号
     */
    String INVOICE_APPLY_NUMBER = "invoiceApplyNumber";

    /**
     * 申请开票金额
     */
    String INVOICE_APPLY_AMOUNT = "invoiceApplyAmount";

    /**
     * 申请开票时间
     */
    String INVOICE_APPLY_TIME = "invoiceApplyTime";

    /**
     * 开票说明
     */
    String REMARK = "remark";

    /**
     * 开票人id
     */
    String USER_ID = "userId";

    /**
     * 开票人姓名
     */
    String USER_NAME = "userName";

    /**
     * 开票税额
     */
    String INVOICE_TAX = "invoiceTax";

    /**
     * 开票状态  0成功  1开票中  2失败
     */
    String INVOICE_STATUS = "invoiceStatus";

    /**
     * 票据号码
     */
    String INVOICE_NUMBER = "invoiceNumber";

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    String AUDIT_STATUS = "auditStatus";

    /**
     * 审批编号
     */
    String AUDIT_NUMBER = "auditNumber";

    /**
     * 财务状态  0已确认  1未确认
     */
    String CONFIRM_STATUS = "confirmStatus";

    /**
     * 租户id
     */
    String TENANT_ID = "tenantId";

    /**
     * 是否删除:0未删除，1已删除
     */
    String DELETED = "deleted";

    /**
     * 创建人ID
     */
    String CREATOR = "creator";

    /**
     * 创建人姓名
     */
    String CREATOR_NAME = "creatorName";

    /**
     * 创建时间
     */
    String GMT_CREATE = "gmtCreate";

    /**
     * 操作人ID
     */
    String OPERATOR = "operator";

    /**
     * 操作人姓名
     */
    String OPERATOR_NAME = "operatorName";

    /**
     * 操作时间
     */
    String GMT_MODIFY = "gmtModify";

    /**
     * 合同开票明细表
     */
    String CONTRACT_INVOICE_DETAIL = "contract_invoice_detail";
}
