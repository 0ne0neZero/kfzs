package com.wishare.contract.domains.entity.revision.income.fund;

import com.baomidou.mybatisplus.annotation.*;
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
 * 收入合同-款项表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_fund")
public class ContractIncomeFundE {

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
     * 款项名称
     */
    private String name;

    /**
     * 款项类型ID
     */
    private String typeId;

    /**
     * 款项类型
     */
    private String type;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 税率ID
     */
    private String taxRateId;

    /**
     * 税率
     */
    private String taxRate;

    /**
     * 付费类型ID
     */
    private String payTypeId;

    /**
     * 付费类型
     */
    private String payType;

    /**
     * 付费方式ID
     */
    private String payWayId;

    /**
     * 付费方式
     */
    private String payWay;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 收费标准ID
     */
    private String standardId;

    /**
     * 收费标准
     */
    private String standard;

    private BigDecimal standAmount;

    private BigDecimal amountNum;

    /**
     * 备注
     */
    private String remark;

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
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;


    /**
     * 税额
     */
    private BigDecimal taxRateAmount;
    /**
     * 不含税金额
     */
    private BigDecimal amountWithOutRate;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 费项ID
     */
    private String chargeItemId;
    /**
     * 费项
     */
    private String chargeItem;

    /**
     * 是否主合同清单（1.是）
     */
    private Integer isMain;
    //收费方式ID
    private String chargeMethodId;
    //收费方式
    private String chargeMethodName;
    //修正记录表示（1.新，0.历史）
    private Integer correctionTag;
    //对应数据ID
    private String mainId;

    public static final String NUM = "num";
    public static final String CORRECTION_TAG = "correctionTag";

    public static final String TAX_RATE_AMOUNT = "taxRateAmount";

    public static final String AMOUNT_WITHOUT_RATE = "amountWithOutRate";

    public static final String CHARGE_ITEM_ID = "chargeItemId";

    public static final String CHARGE_ITEM = "chargeItem";

    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 关联合同ID
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 款项名称
    */
    public static final String NAME = "name";

    /**
    * 款项类型ID
    */
    public static final String TYPE_ID = "typeId";

    /**
    * 款项类型
    */
    public static final String TYPE = "type";

    /**
    * 金额
    */
    public static final String AMOUNT = "amount";

    /**
    * 税率ID
    */
    public static final String TAX_RATE_ID = "taxRateId";

    /**
    * 税率
    */
    public static final String TAX_RATE = "taxRate";

    /**
    * 付费类型ID
    */
    public static final String PAY_TYPE_ID = "payTypeId";

    /**
    * 付费类型
    */
    public static final String PAY_TYPE = "payType";

    /**
    * 付费方式ID
    */
    public static final String PAY_WAY_ID = "payWayId";

    /**
    * 付费方式
    */
    public static final String PAY_WAY = "payWay";

    /**
    * 开始日期
    */
    public static final String START_DATE = "startDate";

    /**
    * 结束日期
    */
    public static final String END_DATE = "endDate";

    /**
    * 收费标准ID
    */
    public static final String STANDARD_ID = "standardId";

    /**
    * 收费标准
    */
    public static final String STANDARD = "standard";

    /**
    * 备注
    */
    public static final String REMARK = "remark";

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
                                    CONTRACT_ID,
                                    NAME,
                                    TYPE_ID,
                                    TYPE,
                                    AMOUNT,
                                    TAX_RATE_ID,
                                    TAX_RATE,
                                    PAY_TYPE_ID,
                                    PAY_TYPE,
                                    PAY_WAY_ID,
                                    PAY_WAY,
                                    START_DATE,
                                    END_DATE,
                                    STANDARD_ID,
                                    STANDARD,
                                    REMARK,
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
