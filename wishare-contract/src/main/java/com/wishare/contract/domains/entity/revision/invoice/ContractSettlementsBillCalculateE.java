package com.wishare.contract.domains.entity.revision.invoice;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * 结算单计量明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_settlements_bill_calculate")
public class ContractSettlementsBillCalculateE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 关联票据ID
     */
    private String billId;

    /**
     * 款项类型ID
     */
    private String typeId;

    /**
     * 款项类型名称
     */
    private String type;

    /**
     * 结算金额
     */
    private BigDecimal amount;

    /**
     * 费项ID
     */
    private Long chargeItemId;

    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 税率ID
     */
    private String taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 税额
     */
    private BigDecimal taxRateAmount;

    /**
     * 不含税金额
     */
    private BigDecimal amountWithOutRate;

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
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 关联票据ID
    */
    public static final String BILL_ID = "billId";

    /**
    * 款项类型ID
    */
    public static final String TYPE_ID = "typeId";

    /**
    * 款项类型名称
    */
    public static final String TYPE = "type";

    /**
    * 结算金额
    */
    public static final String AMOUNT = "amount";

    /**
    * 费项ID
    */
    public static final String CHARGE_ITEM_ID = "chargeItemId";

    /**
    * 费项
    */
    public static final String CHARGE_ITEM = "chargeItem";

    /**
    * 税率ID
    */
    public static final String TAX_RATE_ID = "taxRateId";

    /**
    * 税率
    */
    public static final String TAX_RATE = "taxRate";

    /**
    * 税额
    */
    public static final String TAX_RATE_AMOUNT = "taxRateAmount";

    /**
    * 不含税金额
    */
    public static final String AMOUNT_WITH_OUT_RATE = "amountWithOutRate";

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
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    BILL_ID,
                                    TYPE_ID,
                                    TYPE,
                                    AMOUNT,
                                    CHARGE_ITEM_ID,
                                    CHARGE_ITEM,
                                    TAX_RATE_ID,
                                    TAX_RATE,
                                    TAX_RATE_AMOUNT,
                                    AMOUNT_WITH_OUT_RATE,
                                    TENANT_ID,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
