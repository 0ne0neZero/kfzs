package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractBondCollectionDetail 表字段常量 <br>
*
* id,contractId,bondPlanId,collectionAmount,collectionMethod,collectionNumber,receiptVoucher,receiptVoucherName,collectionTime,remark,deleted,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author ljx
* @since 2022-10-25
*/
public interface ContractBondCollectionDetailFieldConst {

    /**
     * id
     */
    String ID = "id";

    /**
     * 合同Id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 保证金计划id
     */
    String BOND_PLAN_ID = "bondPlanId";

    /**
     * 收款金额
     */
    String COLLECTION_AMOUNT = "collectionAmount";

    /**
     * 收款方式  0现金  1银行转帐  2汇款  3支票
     */
    String COLLECTION_METHOD = "collectionMethod";

    /**
     * 收款流水号
     */
    String COLLECTION_NUMBER = "collectionNumber";

    /**
     * 收款凭证文件集
     */
    String RECEIPT_VOUCHER = "receiptVoucher";

    /**
     * 收款凭证文件名称
     */
    String RECEIPT_VOUCHER_NAME = "receiptVoucherName";

    /**
     * 收款时间
     */
    String COLLECTION_TIME = "collectionTime";

    /**
     * 备注
     */
    String REMARK = "remark";

    /**
     * 是否删除
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
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     * 请不要直接添加或者删除该列表中的内容转而使用apache下的CollectionUtils.retainAll()进行交集操作
     */
    List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    BOND_PLAN_ID,
                                    COLLECTION_AMOUNT,
                                    COLLECTION_METHOD,
                                    COLLECTION_NUMBER,
                                    RECEIPT_VOUCHER,
                                    RECEIPT_VOUCHER_NAME,
                                    COLLECTION_TIME,
                                    REMARK,
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

    String CONTRACT_BOND_COLLECTION_DETAIL = "contract_bond_collection_detail";
}
