package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractPaymentDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,paymentAmount,applyPaymentTime,actualPaymentTime,userId,userName,remark,paymentType,invoiceUrl,invoiceCode,auditStatus,auditCode,paymentStatus,confirmStatus,deleted,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author ljx
* @since 2022-09-29
*/
public interface ContractPaymentDetailFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 合同id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 收款计划id
     */
    String COLLECTION_PLAN_ID = "collectionPlanId";

    /**
     * 申请付款金额
     */
    String PAYMENT_AMOUNT = "paymentAmount";

    /**
     * 申请付款时间
     */
    String APPLY_PAYMENT_TIME = "applyPaymentTime";

    /**
     * 实际付款时间
     */
    String ACTUAL_PAYMENT_TIME = "actualPaymentTime";

    /**
     * 付款申请人id
     */
    String USER_ID = "userId";

    /**
     * 付款申请人姓名
     */
    String USER_NAME = "userName";

    /**
     * 请款说明
     */
    String REMARK = "remark";

    /**
     * 付款类型  0有票付款  1无票付款
     */
    String PAYMENT_TYPE = "paymentType";

    /**
     * 发票文件集
     */
    String INVOICE_URL = "invoiceUrl";

    /**
     * 票据号码集
     */
    String INVOICE_CODE = "invoiceCode";

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    String AUDIT_STATUS = "auditStatus";

    /**
     * 审批编号
     */
    String AUDIT_CODE = "auditCode";

    /**
     * 付款状态  0已付款  1未付款
     */
    String PAYMENT_STATUS = "paymentStatus";

    /**
     * 财务状态  0已确认  1未确认
     */
    String CONFIRM_STATUS = "confirmStatus";

    /**
     * 是否删除:0未删除，1已删除
     */
    String DELETED = "deleted";

    /**
     * 创建人id
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
     * 操作人id
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
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     * 请不要直接添加或者删除该列表中的内容转而使用apache下的CollectionUtils.retainAll()进行交集操作
     */
    List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    COLLECTION_PLAN_ID,
                                    PAYMENT_AMOUNT,
                                    APPLY_PAYMENT_TIME,
                                    ACTUAL_PAYMENT_TIME,
                                    USER_ID,
                                    USER_NAME,
                                    REMARK,
                                    PAYMENT_TYPE,
                                    INVOICE_URL,
                                    INVOICE_CODE,
                                    AUDIT_STATUS,
                                    AUDIT_CODE,
                                    PAYMENT_STATUS,
                                    CONFIRM_STATUS,
                                    DELETED,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
