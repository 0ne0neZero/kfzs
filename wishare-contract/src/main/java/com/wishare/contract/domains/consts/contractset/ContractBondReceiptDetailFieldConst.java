package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractBondReceiptDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,invoiceAmount,invoiceType,invocieStatus,invoiceNumber,invoiceTime,remark,deleted,creator,creatorName,gmtCreate
* </p>
*
* @author ljx
* @since 2022-12-12
*/
public interface ContractBondReceiptDetailFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 合同id
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
     * 发票类型  6电子收据
     */
    String INVOICE_TYPE = "invoiceType";

    /**
     * 开票状态  0开票中  1成功 2失败
     */
    String INVOCIE_STATUS = "invocieStatus";

    /**
     * 票据号码  收据编号
     */
    String INVOICE_NUMBER = "invoiceNumber";

    /**
     * 申请开票时间
     */
    String INVOICE_TIME = "invoiceTime";

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
                                    INVOCIE_STATUS,
                                    INVOICE_NUMBER,
                                    INVOICE_TIME,
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
