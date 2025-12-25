package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractBpmProcessRecord 表字段常量 <br>
*
* id,processId,bpmBoUuid,type,tenantId,creator,creatorName,gmtCreate,operator,operatorName,deleted,gmtModify,reviewStatus,rejectReason
* </p>
*
* @author jinhui
* @since 2023-02-24
*/
public interface ContractBpmProcessRecordFieldConst {

    /**
     * 主键
     */
    String ID = "id";

    /**
     * 流程id
     */
    String PROCESS_ID = "processId";

    /**
     * Bo对象中的id
     */
    String BPM_BO_UUID = "bpmBoUuid";

    /**
     * 类型（1合同订立支出2合同订立收入）
     */
    String TYPE = "type";

    /**
     * 租户ID
     */
    String TENANT_ID = "tenantId";

    /**
     * 创建人id
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
     * 修改人id
     */
    String OPERATOR = "operator";

    /**
     * 修改人名称
     */
    String OPERATOR_NAME = "operatorName";

    /**
     * 是否删除（0否1是）
     */
    String DELETED = "deleted";

    /**
     * 修改时间
     */
    String GMT_MODIFY = "gmtModify";

    /**
     * 审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批
     */
    String REVIEW_STATUS = "reviewStatus";

    /**
     * 驳回原因
     */
    String REJECT_REASON = "rejectReason";


    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     * 请不要直接添加或者删除该列表中的内容转而使用apache下的CollectionUtils.retainAll()进行交集操作
     */
    List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    PROCESS_ID,
                                    BPM_BO_UUID,
                                    TYPE,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    DELETED,
                                    GMT_MODIFY,
                                    REVIEW_STATUS,
                                    REJECT_REASON);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);

    /**
     * 合同流程记录表
     */
    String CONTRACT_BPM_PROCESS_RECORD_INFO = "contract_bpm_process_record";
}
