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
 * 收取保证金改版关联单据明细表
 * </p>
 *
 * @author chenglong
 * @since 2023-07-27
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("collect_bond_relation_bill")
public class CollectBondRelationBillE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 保证金计划ID
     */
    private String bondId;

    /**
     * 编号
     */
    private String code;

    /**
     * 业务类型编码（收款，收据，结转，退款）
     */
    private String typeCode;

    /**
     * 业务类型名称（收款，收据，结转，退款）
     */
    private String type;

    /**
     * 金额（收款，收据，结转，退款）
     */
    private BigDecimal amount;

    /**
     * 交易方式编码（现金，微信，支付宝，网上转账等）
     */
    private String dealWayCode;

    /**
     * 交易方式名称（现金，微信，支付宝，网上转账等）
     */
    private String dealWay;

    /**
     * 业务费项ID
     */
    private String chargeItemId;

    /**
     * 业务费项名称
     */
    private String chargeItem;

    /**
     * （收款，收据，结转，退款）交易日期
     */
    private LocalDate dealDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 原因
     */
    private String reason;

    /**
     * 剩余金额
     */
    private BigDecimal residueAmount;

    /**
     * 审批流id
     */
    private Long procId;

    /**
     * 状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）
     */
    private Integer status;

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
     * bankAccount
     */
    private String bankAccount;

    /**
     * 转履约关联数据ID
     */
    private String volumUpId;


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 保证金计划ID
    */
    public static final String BOND_ID = "bondId";

    /**
    * 编号
    */
    public static final String CODE = "code";

    /**
    * 业务类型编码（收款，收据，结转，退款）
    */
    public static final String TYPE_CODE = "typeCode";

    /**
    * 业务类型名称（收款，收据，结转，退款）
    */
    public static final String TYPE = "type";

    /**
    * 金额（收款，收据，结转，退款）
    */
    public static final String AMOUNT = "amount";

    /**
    * 交易方式编码（现金，微信，支付宝，网上转账等）
    */
    public static final String DEAL_WAY_CODE = "dealWayCode";

    /**
    * 交易方式名称（现金，微信，支付宝，网上转账等）
    */
    public static final String DEAL_WAY = "dealWay";

    /**
    * 业务费项ID
    */
    public static final String CHARGE_ITEM_ID = "chargeItemId";

    /**
    * 业务费项名称
    */
    public static final String CHARGE_ITEM = "chargeItem";

    /**
    * （收款，收据，结转，退款）交易日期
    */
    public static final String DEAL_DATE = "dealDate";

    /**
    * 备注
    */
    public static final String REMARK = "remark";

    /**
    * 原因
    */
    public static final String REASON = "reason";

    /**
    * 剩余金额
    */
    public static final String RESIDUE_AMOUNT = "residueAmount";

    /**
    * 审批流id
    */
    public static final String PROC_ID = "procId";

    /**
    * 状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）
    */
    public static final String STATUS = "status";

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
     * 转履约关联数据ID
     */
    public static final String VOLUM_UP_ID = "volumUpId";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    BOND_ID,
                                    CODE,
                                    TYPE_CODE,
                                    TYPE,
                                    AMOUNT,
                                    DEAL_WAY_CODE,
                                    DEAL_WAY,
                                    CHARGE_ITEM_ID,
                                    CHARGE_ITEM,
                                    DEAL_DATE,
                                    REMARK,
                                    REASON,
                                    RESIDUE_AMOUNT,
                                    PROC_ID,
                                    STATUS,
                                    DELETED,
                                    TENANT_ID,
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
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
