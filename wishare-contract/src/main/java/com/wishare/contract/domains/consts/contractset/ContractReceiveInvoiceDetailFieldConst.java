package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractReceiveInvoiceDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,invoiceAmount,invoiceType,invocieUrl,invoiceNumber,invoiceTime,auditCode,auditStatus,remark,deleted,creator,creatorName,gmtCreate
* </p>
*
* @author ljx
* @since 2022-11-29
*/
public interface ContractReceiveInvoiceDetailFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 合同Id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 付款计划id
     */
    String COLLECTION_PLAN_ID = "collectionPlanId";

    /**
     * 收票金额
     */
    String INVOICE_AMOUNT = "invoiceAmount";

    /**
     * 发票类型  1增值税电子发票
     */
    String INVOICE_TYPE = "invoiceType";

    /**
     * 票据url
     */
    String INVOCIE_URL = "invocieUrl";

    /**
     * 票据号码
     */
    String INVOICE_NUMBER = "invoiceNumber";

    /**
     * 收票时间
     */
    String INVOICE_TIME = "invoiceTime";

    /**
     * 关联审批
     */
    String AUDIT_CODE = "auditCode";

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    String AUDIT_STATUS = "auditStatus";

    /**
     * 备注
     */
    String REMARK = "remark";

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
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     * 请不要直接添加或者删除该列表中的内容转而使用apache下的CollectionUtils.retainAll()进行交集操作
     */
    List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    COLLECTION_PLAN_ID,
                                    INVOICE_AMOUNT,
                                    INVOICE_TYPE,
                                    INVOCIE_URL,
                                    INVOICE_NUMBER,
                                    INVOICE_TIME,
                                    AUDIT_CODE,
                                    AUDIT_STATUS,
                                    REMARK,
                                    DELETED,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
