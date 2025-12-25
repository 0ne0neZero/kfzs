package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* CollectionPlanDerateDetail 表字段常量 <br>
*
* id,contractId,collectionPlanId,derateAmount,derateReason,auditStatus,auditCode,deleted,creator,creatorName,gmtCreate
* </p>
*
* @author ljx
* @since 2022-11-07
*/
public interface CollectionPlanDerateDetailFieldConst {

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
     * 减免金额（元）
     */
    String DERATE_AMOUNT = "derateAmount";

    /**
     * 减免原因
     */
    String DERATE_REASON = "derateReason";

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    String AUDIT_STATUS = "auditStatus";

    /**
     * 审批编号
     */
    String AUDIT_CODE = "auditCode";

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
                                    DERATE_AMOUNT,
                                    DERATE_REASON,
                                    AUDIT_STATUS,
                                    AUDIT_CODE,
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
