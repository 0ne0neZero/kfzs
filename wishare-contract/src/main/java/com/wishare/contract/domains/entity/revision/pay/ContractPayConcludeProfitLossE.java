package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;
import java.util.Arrays;
/**
 * <p>
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_pay_conclude_profit_loss")
public class ContractPayConcludeProfitLossE {

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
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 供应商
     */
    private String merchant;

    /**
     * 供应商名称
     */
    private String merchantName;

    /**
     * 付款计划编号
     */
    private String payNotecode;

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
     * 扣款金额
     */
    private BigDecimal deductionAmount;

    /**
     * 收票金额
     */
    private BigDecimal invoiceApplyAmount;

    /**
     * 付款金额
     */
    private BigDecimal paymentAmount;

    /**
     * 计划状态 0待提交  1未完成  2已完成
     */
    private Integer planStatus;

    /**
     * 结算状态 0未结算  1未完成  2已完成
     */
    private Integer paymentStatus;

    /**
     * 收票状态 0未完成  1已完成
     */
    private Integer invoiceStatus;

    /**
     * 审核状态0待提交1审批中2已通过3已拒绝
     */
    private Integer reviewStatus;

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
     * 税率
     */
    private String taxRate;

    /**
     * 税率ID
     */
    private String taxRateId;

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
     * 未付金额
     */
    private BigDecimal noPayAmount;

    /**
     * 金额比例
     */
    private BigDecimal ratioAmount;

    /**
     * 次
     */
    private Integer howOrder;

    /**
     * 服务类型
     */
    private Integer serviceType;

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
     * 收入状态 0-未确收 1-已确收
     */
    private Integer acceptStatus;

    /**
     * 收入确认金额(元)
     */
    private BigDecimal acceptAmount;


    /**
     * 所属部门(元)
     */
    private String departName;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 财务中台账单id
     */
    private Long billId;

    /**
     * 财务中台账单编号
     */
    @ApiModelProperty("财务中台账单编号")
    private String billNo;

    public static final String BILLID = "billId";

    public static final String BILLNO = "billNo";

    public static final String DEPARTNAME = "departName";

    public static final String COSTCENTERNAME = "costCenterName";


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 关联合同ID
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 合同编号
    */
    public static final String CONTRACT_NO = "contractNo";

    /**
    * 合同名称
    */
    public static final String CONTRACT_NAME = "contractName";

    /**
    * 供应商
    */
    public static final String MERCHANT = "merchant";

    /**
    * 供应商名称
    */
    public static final String MERCHANT_NAME = "merchantName";

    /**
    * 付款计划编号
    */
    public static final String PAY_NOTECODE = "payNotecode";

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
    * 扣款金额
    */
    public static final String DEDUCTION_AMOUNT = "deductionAmount";

    /**
    * 收票金额
    */
    public static final String INVOICE_APPLY_AMOUNT = "invoiceApplyAmount";

    /**
    * 付款金额
    */
    public static final String PAYMENT_AMOUNT = "paymentAmount";

    /**
    * 计划状态 0待提交  1未完成  2已完成
    */
    public static final String PLAN_STATUS = "planStatus";

    /**
    * 结算状态 0未结算  1未完成  2已完成
    */
    public static final String PAYMENT_STATUS = "paymentStatus";

    /**
    * 收票状态 0未完成  1已完成
    */
    public static final String INVOICE_STATUS = "invoiceStatus";

    /**
    * 审核状态0待提交1审批中2已通过3已拒绝
    */
    public static final String REVIEW_STATUS = "reviewStatus";

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
    * 税率
    */
    public static final String TAX_RATE = "taxRate";

    /**
    * 税率ID
    */
    public static final String TAX_RATE_ID = "taxRateId";

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
    * 未付金额
    */
    public static final String NO_PAY_AMOUNT = "noPayAmount";

    /**
    * 金额比例
    */
    public static final String RATIO_AMOUNT = "ratioAmount";

    /**
    * 次
    */
    public static final String HOW_ORDER = "howOrder";

    /**
    * 服务类型
    */
    public static final String SERVICE_TYPE = "serviceType";

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
    * 收入状态 0-未确收 1-已确收
    */
    public static final String ACCEPT_STATUS = "acceptStatus";

    /**
    * 收入确认金额(元)
    */
    public static final String ACCEPT_AMOUNT = "acceptAmount";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    COSTCENTERNAME,
                                    BILLID,
                                    DEPARTNAME,
                                    CONTRACT_ID,
                                    CONTRACT_NO,
                                    CONTRACT_NAME,
                                    MERCHANT,
                                    MERCHANT_NAME,
                                    PAY_NOTECODE,
                                    TERM_DATE,
                                    PLANNED_COLLECTION_TIME,
                                    PLANNED_COLLECTION_AMOUNT,
                                    SETTLEMENT_AMOUNT,
                                    DEDUCTION_AMOUNT,
                                    INVOICE_APPLY_AMOUNT,
                                    PAYMENT_AMOUNT,
                                    PLAN_STATUS,
                                    PAYMENT_STATUS,
                                    INVOICE_STATUS,
                                    REVIEW_STATUS,
                                    SPLIT_MODE,
                                    CHARGE_ITEM,
                                    CHARGE_ITEM_ID,
                                    TAX_RATE,
                                    TAX_RATE_ID,
                                    NO_TAX_AMOUNT,
                                    TAX_AMOUNT,
                                    REMARK,
                                    NO_PAY_AMOUNT,
                                    RATIO_AMOUNT,
                                    HOW_ORDER,
                                    SERVICE_TYPE,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED,
                                    ACCEPT_STATUS,
                                    ACCEPT_AMOUNT);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
