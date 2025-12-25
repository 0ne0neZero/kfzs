package com.wishare.contract.domains.entity.revision.bond;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Arrays;
/**
 * <p>
 * 保证金改版-收取类保证金
 * </p>
 *
 * @author chenglong
 * @since 2023-07-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("revision_bond_collect")
public class RevisionBondCollectE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 计划编号
     */
    private String code;

    /**
     * 保证金类型Code
     */
    private String typeCode;

    /**
     * 保证金类型名称
     */
    private String type;

    /**
     * 供应商ID
     */
    private String supplierId;

    /**
     * 供应商名称
     */
    private String supplier;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 成本中心ID
     */
    private String costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 所属项目ID
     */
    private String communityId;

    /**
     * 所属项目名称
     */
    private String communityName;

    /**
     * 保证金总额
     */
    private BigDecimal bondAmount;

    /**
     * 计划收款日期
     */
    private LocalDate plannedCollectionDate;

    /**
     * 计划收款金额
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 所属部门ID
     */
    private String orgId;

    /**
     * 所属部门名称
     */
    private String orgName;

    /**
     * 负责人ID
     */
    private String chargeManId;

    /**
     * 负责人
     */
    private String chargeMan;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0 待提交   3 未完成   5 已完成）
     */
    private Integer status;

    /**
     * 已收款金额
     */
    private BigDecimal collectAmount;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款状态  1未退款    2部分退款    3已退款
     */
    private Integer refundStatus;

    /**
     * 已开收据金额
     */
    private BigDecimal receiptAmount;

    /**
     * 已结转金额
     */
    private BigDecimal settleTransferAmount;

    /**
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 中台临时账单id
     */
    private Long billId;

    /**
     * 中台临时账单编号（招投标保证金才有）
     */
    private String billNo;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 银行账户
     */
    private String bankAccount;

    /**
     * 是否关联投标保证金   1 是    0 否
     */
    private Integer isTender;

    /**
     * 关联投标保证金ID
     */
    private String tenderBondId;

    /**
     * 投标保证金金额
     */
    private BigDecimal tenderBondAmount;


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 计划编号
    */
    public static final String CODE = "code";

    /**
    * 保证金类型Code
    */
    public static final String TYPE_CODE = "typeCode";

    /**
    * 保证金类型名称
    */
    public static final String TYPE = "type";

    /**
    * 供应商ID
    */
    public static final String SUPPLIER_ID = "supplierId";

    /**
    * 供应商名称
    */
    public static final String SUPPLIER = "supplier";

    /**
    * 合同ID
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 合同编号
    */
    public static final String CONTRACT_CODE = "contractCode";

    /**
    * 合同名称
    */
    public static final String CONTRACT_NAME = "contractName";

    /**
    * 成本中心ID
    */
    public static final String COST_CENTER_ID = "costCenterId";

    /**
    * 成本中心名称
    */
    public static final String COST_CENTER_NAME = "costCenterName";

    /**
    * 所属项目ID
    */
    public static final String COMMUNITY_ID = "communityId";

    /**
    * 所属项目名称
    */
    public static final String COMMUNITY_NAME = "communityName";

    /**
    * 保证金总额
    */
    public static final String BOND_AMOUNT = "bondAmount";

    /**
    * 计划收款日期
    */
    public static final String PLANNED_COLLECTION_DATE = "plannedCollectionDate";

    /**
    * 计划收款金额
    */
    public static final String PLANNED_COLLECTION_AMOUNT = "plannedCollectionAmount";

    /**
    * 所属部门ID
    */
    public static final String ORG_ID = "orgId";

    /**
    * 所属部门名称
    */
    public static final String ORG_NAME = "orgName";

    /**
    * 负责人ID
    */
    public static final String CHARGE_MAN_ID = "chargeManId";

    /**
    * 负责人
    */
    public static final String CHARGE_MAN = "chargeMan";

    /**
    * 备注
    */
    public static final String REMARK = "remark";

    /**
    * 状态（0 待提交   3 未完成   5 已完成）
    */
    public static final String STATUS = "status";

    /**
    * 已收款金额
    */
    public static final String COLLECT_AMOUNT = "collectAmount";

    /**
    * 已退款金额
    */
    public static final String REFUND_AMOUNT = "refundAmount";

    /**
    * 已开收据金额
    */
    public static final String RECEIPT_AMOUNT = "receiptAmount";

    /**
    * 已结转金额
    */
    public static final String SETTLE_TRANSFER_AMOUNT = "settleTransferAmount";

    /**
    * 扣款金额
    */
    public static final String DEDUCTION_AMOUNT = "deductionAmount";

    /**
    * 是否删除  0 正常 1 删除
    */
    public static final String DELETED = "deleted";

    /**
    * 租户id
    */
    public static final String TENANT_ID = "tenantId";

    /**
    * 创建人
    */
    public static final String CREATOR = "creator";

    /**
    * 创建人名称
    */
    public static final String CREATOR_NAME = "creatorName";

    /**
    * 创建时间
    */
    public static final String GMT_CREATE = "gmtCreate";

    /**
    * 操作人
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人名称
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
    * 操作时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
    * 中台临时账单id
    */
    public static final String BILL_ID = "billId";

    /**
    * 中台临时账单编号（招投标保证金才有）
    */
    public static final String BILL_NO = "billNo";

    /**
    * 开户行
    */
    public static final String BANK_NAME = "bankName";

    /**
    * 银行账户
    */
    public static final String BANK_ACCOUNT = "bankAccount";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CODE,
                                    TYPE_CODE,
                                    TYPE,
                                    SUPPLIER_ID,
                                    SUPPLIER,
                                    CONTRACT_ID,
                                    CONTRACT_CODE,
                                    CONTRACT_NAME,
                                    COST_CENTER_ID,
                                    COST_CENTER_NAME,
                                    COMMUNITY_ID,
                                    COMMUNITY_NAME,
                                    BOND_AMOUNT,
                                    PLANNED_COLLECTION_DATE,
                                    PLANNED_COLLECTION_AMOUNT,
                                    ORG_ID,
                                    ORG_NAME,
                                    CHARGE_MAN_ID,
                                    CHARGE_MAN,
                                    REMARK,
                                    STATUS,
                                    COLLECT_AMOUNT,
                                    REFUND_AMOUNT,
                                    RECEIPT_AMOUNT,
                                    SETTLE_TRANSFER_AMOUNT,
                                    DEDUCTION_AMOUNT,
                                    DELETED,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    BILL_ID,
                                    BILL_NO,
                                    BANK_NAME,
                                    BANK_ACCOUNT);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
