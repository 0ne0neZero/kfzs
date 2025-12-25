package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
/**
 * <p>
 * 合同收入损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_profit_loss")
public class ContractIncomeConcludeProfitLossE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 关联合同ID
     */
    private String contractId;

    /**
     * 收款计划编号
     */
    private String payNotecode;

    /**
     * 客户id
     */
    private String customer;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 期数
     */
    private Integer termDate;

    /**
     * 计划收付款时间
     */
    private LocalDate plannedCollectionTime;

    /**
     * 计划收付款金额
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 结算金额
     */
    private BigDecimal settlementAmount;

    /**
     * 减免金额
     */
    private BigDecimal deductionAmount;

    /**
     * 开票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 收款金额
     */
    private BigDecimal receiptAmount;

    /**
     * 未收款金额
     */
    private BigDecimal noReceiptAmount;

    /**
     * 计划状态 0待提交  1未完成  2已完成
     */
    private Integer planStatus;

    /**
     * 结算状态 0未结算  1未完成  2已完成
     */
    private Integer paymentStatus;

    /**
     * 开票状态 0未完成  1已完成
     */
    private Integer invoiceStatus;

    /**
     * 审核状态0待提交1审批中2已通过3已拒绝
     */
    private Integer reviewStatus;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
     */
    private Integer splitMode;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 费项ID
     */
    private String chargeItemId;

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
     * 第几笔
     */
    private Integer howOrder;

    /**
     * 金额比例
     */
    private BigDecimal ratioAmount;

    /**
     * 服务类型
     */
    private Integer serviceType;

    /**
     * 未计划金额
     */
    private BigDecimal noPlanAmount;

    /**
     * 税率路径
     */
    private String taxRateIdPath;

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
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 收入状态
     */
    @ApiModelProperty("收入状态")
    private Integer acceptStatus;

    /**
     * 收入确认金额(元)
     */
    @ApiModelProperty("收入确认金额(元)")
    private BigDecimal acceptAmount;


    /**
     * 财务中台账单id
     */
    @ApiModelProperty("财务中台账单id")
    private Long billId;

    /**
     * 财务中台账单编号
     */
    @ApiModelProperty("财务中台账单编号")
    private String billNo;



    public static final String BILLID = "billId";

    public static final String BILLNO = "billNo";

    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 关联合同ID
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 收款计划编号
    */
    public static final String PAY_NOTECODE = "payNotecode";

    /**
    * 客户id
    */
    public static final String CUSTOMER = "customer";

    /**
    * 客户名称
    */
    public static final String CUSTOMER_NAME = "customerName";

    /**
    * 合同编号
    */
    public static final String CONTRACT_NO = "contractNo";

    /**
    * 合同名称
    */
    public static final String CONTRACT_NAME = "contractName";

    /**
    * 期数
    */
    public static final String TERM_DATE = "termDate";

    /**
    * 计划收付款时间
    */
    public static final String PLANNED_COLLECTION_TIME = "plannedCollectionTime";

    /**
    * 计划收付款金额
    */
    public static final String PLANNED_COLLECTION_AMOUNT = "plannedCollectionAmount";

    /**
    * 结算金额
    */
    public static final String SETTLEMENT_AMOUNT = "settlementAmount";

    /**
    * 减免金额
    */
    public static final String DEDUCTION_AMOUNT = "deductionAmount";

    /**
    * 开票金额
    */
    public static final String INVOICE_APPLY_AMOUNT = "invoiceApplyAmount";

    /**
    * 收款金额
    */
    public static final String RECEIPT_AMOUNT = "receiptAmount";

    /**
    * 未收款金额
    */
    public static final String NO_RECEIPT_AMOUNT = "noReceiptAmount";

    /**
    * 计划状态 0待提交  1未完成  2已完成
    */
    public static final String PLAN_STATUS = "planStatus";

    /**
    * 结算状态 0未结算  1未完成  2已完成
    */
    public static final String PAYMENT_STATUS = "paymentStatus";

    /**
    * 开票状态 0未完成  1已完成
    */
    public static final String INVOICE_STATUS = "invoiceStatus";

    /**
    * 审核状态0待提交1审批中2已通过3已拒绝
    */
    public static final String REVIEW_STATUS = "reviewStatus";

    /**
    * 部门名称
    */
    public static final String DEPART_NAME = "departName";

    /**
     * 成本中心名称
     */
    public static final String COSTCENTER_NAME = "costCenterName";

    /**
    * 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
    */
    public static final String SPLIT_MODE = "splitMode";

    /**
    * 费项
    */
    public static final String CHARGE_ITEM = "chargeItem";

    /**
    * 费项ID
    */
    public static final String CHARGE_ITEM_ID = "chargeItemId";

    /**
    * 税率ID,额外需要维护，不展示
    */
    public static final String TAX_RATE_ID = "taxRateId";

    /**
    * 税率
    */
    public static final String TAX_RATE = "taxRate";

    /**
    * 不含税金额
    */
    public static final String NO_TAX_AMOUNT = "noTaxAmount";

    /**
    * 税额
    */
    public static final String TAX_AMOUNT = "taxAmount";

    /**
    * 备注
    */
    public static final String REMARK = "remark";

    /**
    * 第几笔
    */
    public static final String HOW_ORDER = "howOrder";

    /**
    * 金额比例
    */
    public static final String RATIO_AMOUNT = "ratioAmount";

    /**
    * 服务类型
    */
    public static final String SERVICE_TYPE = "serviceType";

    /**
    * 未计划金额
    */
    public static final String NO_PLAN_AMOUNT = "noPlanAmount";

    /**
    * 税率路径
    */
    public static final String TAX_RATE_ID_PATH = "taxRateIdPath";

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
    * 是否删除  0 正常 1 删除
    */
    public static final String DELETED = "deleted";

    /**
     * 收入状态
     */
    public static final String ACCEPT_STATUS = "acceptStatus";

    /**
     * 收入确认金额
     */
    public static final String ACCEPT_AMOUNT = "acceptAmount";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    BILLID,
                                    CONTRACT_ID,
                                    PAY_NOTECODE,
                                    CUSTOMER,
                                    CUSTOMER_NAME,
                                    CONTRACT_NO,
                                    CONTRACT_NAME,
                                    TERM_DATE,
                                    PLANNED_COLLECTION_TIME,
                                    PLANNED_COLLECTION_AMOUNT,
                                    SETTLEMENT_AMOUNT,
                                    DEDUCTION_AMOUNT,
                                    INVOICE_APPLY_AMOUNT,
                                    RECEIPT_AMOUNT,
                                    NO_RECEIPT_AMOUNT,
                                    PLAN_STATUS,
                                    PAYMENT_STATUS,
                                    INVOICE_STATUS,
                                    REVIEW_STATUS,
                                    DEPART_NAME,
                                    COSTCENTER_NAME,
                                    SPLIT_MODE,
                                    CHARGE_ITEM,
                                    CHARGE_ITEM_ID,
                                    TAX_RATE_ID,
                                    TAX_RATE,
                                    NO_TAX_AMOUNT,
                                    TAX_AMOUNT,
                                    REMARK,
                                    HOW_ORDER,
                                    RATIO_AMOUNT,
                                    SERVICE_TYPE,
                                    NO_PLAN_AMOUNT,
                                    TAX_RATE_ID_PATH,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    ACCEPT_STATUS,
                                    ACCEPT_AMOUNT,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
