package com.wishare.contract.domains.entity.revision;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 该父类仅做属性复用，不能承载具体的数据传输功能，若要使用对应的属性，请使用子类
 *
 * @author 龙江锋
 * @date 2023/8/18 9:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class ContractPlanConcludeE extends BaseE {

    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    private Integer reviewStatus;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 结算周期
     * 旧：拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
     */
    private Integer splitMode;

    /**
     * 金额比例
     */
    private BigDecimal ratioAmount;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 税率ID,额外需要维护，不展示
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 不含税金额
     */
    private BigDecimal noTaxAmount;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * id
     */
    public static final String ID = "id";

    /**
     * id
     */
    public static final String PID = "pid";

    /**
     * 合同id
     */
    public static final String CONTRACT_ID = "contractId";

    /**
     * 删除
     */
    public static final String DELETED = "deleted";

    /**
     * 合同id
     */
    public static final String TERMDATE = "termDate";

    /**
     * 结算日期
     */
    public static final String PLANNED_COLLECTION_TIME = "plannedCollectionTime";
}
