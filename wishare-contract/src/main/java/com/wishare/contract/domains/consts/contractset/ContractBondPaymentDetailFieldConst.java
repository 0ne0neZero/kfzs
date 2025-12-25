package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractBondPaymentDetail 表字段常量 <br>
*
* id,contractId,bondPlanId,paymentAmount,paymentMethod,auditCode,auditStatus,confirmStatus,applyPaymentTime,actualPaymentTime,remark,deleted,creator,creatorName,gmtCreate,operator,operatorName,gmtModify
* </p>
*
* @author ljx
* @since 2022-10-25
*/
public interface ContractBondPaymentDetailFieldConst {

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
     * 申请付/退款金额（元）
     */
    String PAYMENT_AMOUNT = "paymentAmount";

    /**
     * 付/退款方式  0现金  1银行转帐  2汇款  3支票
     */
    String PAYMENT_METHOD = "paymentMethod";

    /**
     * 关联审批编号
     */
    String AUDIT_CODE = "auditCode";

    /**
     * 审批状态 0通过  1审核中  2未通过
     */
    String AUDIT_STATUS = "auditStatus";

    /**
     * 财务状态  0已确认  1未确认
     */
    String CONFIRM_STATUS = "confirmStatus";

    /**
     * 申请付/退款时间
     */
    String APPLY_PAYMENT_TIME = "applyPaymentTime";

    /**
     * 实际付/退款时间
     */
    String ACTUAL_PAYMENT_TIME = "actualPaymentTime";

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
                                    PAYMENT_AMOUNT,
                                    PAYMENT_METHOD,
                                    AUDIT_CODE,
                                    AUDIT_STATUS,
                                    CONFIRM_STATUS,
                                    APPLY_PAYMENT_TIME,
                                    ACTUAL_PAYMENT_TIME,
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

    String CONTRACT_BOND_PAYMENT_DETAIL = "contract_bond_payment_detail";
}
