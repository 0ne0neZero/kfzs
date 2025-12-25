package com.wishare.contract.domains.entity.revision.invoice;

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
 * 收票明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-10
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_settlements_bill_details")
public class ContractSettlementsBillDetailsE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 结算单id
     */
    private String billId;

    /**
     * 票据号码
     */
    private String billNum;

    /**
     * 票据代码
     */
    private String billCode;

    /**
     * 票据类型
     */
    private Integer billType;

    /**
     * 审核状态 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    private Integer reviewStatus;

    /**
     * 发票金额
     */
    private BigDecimal amount;

    /**
     * 发票税额
     */
    private BigDecimal amountRate;

    /**
     * 收票时间
     */
    private LocalDate billDate;

    /**
     * 扩展字段1
     */
    private String extend1;

    /**
     * 扩展字段2
     */
    private String extend2;

    /**
     * 扩展字段3
     */
    private String extend3;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 结算单id
    */
    public static final String BILL_ID = "billId";

    /**
    * 票据号码
    */
    public static final String BILL_NUM = "billNum";

    /**
    * 票据代码
    */
    public static final String BILL_CODE = "billCode";

    /**
    * 票据类型
    */
    public static final String BILL_TYPE = "billType";

    /**
    * 发票金额
    */
    public static final String AMOUNT = "amount";

    /**
    * 收票时间
    */
    public static final String BILL_DATE = "billDate";

    /**
    * 扩展字段1
    */
    public static final String EXTEND1 = "extend1";

    /**
    * 扩展字段2
    */
    public static final String EXTEND2 = "extend2";

    /**
    * 扩展字段3
    */
    public static final String EXTEND3 = "extend3";

    /**
    * 是否删除:0未删除，1已删除
    */
    public static final String DELETED = "deleted";

    /**
    * 租户id
    */
    public static final String TENANT_ID = "tenantId";

    /**
    * 创建时间
    */
    public static final String GMT_CREATE = "gmtCreate";

    /**
    * 创建人ID
    */
    public static final String CREATOR = "creator";

    /**
    * 创建人姓名
    */
    public static final String CREATOR_NAME = "creatorName";

    /**
    * 操作时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
    * 操作人ID
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人姓名
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    BILL_ID,
                                    BILL_NUM,
                                    BILL_CODE,
                                    BILL_TYPE,
                                    AMOUNT,
                                    BILL_DATE,
                                    EXTEND1,
                                    EXTEND2,
                                    EXTEND3,
                                    DELETED,
                                    TENANT_ID,
                                    GMT_CREATE,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_MODIFY,
                                    OPERATOR,
                                    OPERATOR_NAME);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
