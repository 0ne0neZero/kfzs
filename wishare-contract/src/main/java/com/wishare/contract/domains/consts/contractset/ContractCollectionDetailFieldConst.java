package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractCollectionDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,invoice,receiptNumber,serialNumber,remark,receivedAmount,collectionType,receiptVoucher,userId,userName,collectionTime,tenantId,deleted,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author ljx
* @since 2022-09-26
*/
public interface ContractCollectionDetailFieldConst {

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
     * 开票信息
     */
    String INVOICE = "invoice";

    /**
     * 收款编号
     */
    String RECEIPT_NUMBER = "receiptNumber";

    /**
     * 收款流水号
     */
    String SERIAL_NUMBER = "serialNumber";

    /**
     * 备注
     */
    String REMARK = "remark";

    /**
     * 本次收款金额
     */
    String RECEIVED_AMOUNT = "receivedAmount";

    /**
     * 收款方式   0现金  1网上转账  2支付宝  3微信
     */
    String COLLECTION_TYPE = "collectionType";

    /**
     * 收款凭证文件集
     */
    String RECEIPT_VOUCHER = "receiptVoucher";

    /**
     * 收款人id
     */
    String USER_ID = "userId";

    /**
     * 收款人姓名
     */
    String USER_NAME = "userName";

    /**
     * 收款时间
     */
    String COLLECTION_TIME = "collectionTime";

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
     * 合同收款明细表
     */
    String CONTRACT_COLLECTION_DETAIL = "contract_collection_detail";
}
