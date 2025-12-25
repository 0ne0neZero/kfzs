package com.wishare.contract.domains.entity.revision.pay.bill;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenglong
 * @since 2023-06-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_settlements_bill")
public class ContractSettlementsBillE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 结算单ID
     */
    private String settlementId;

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 票据类型
     */
    private String billType;

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
    private String type;

    /**
     * 收票金额
     */
    private BigDecimal amount;

    /**
     * 抬头
     */
    private String title;

    /**
     * 纳税人识别号
     */
    private String creditCode;


    private String attachments;

    /**
     * 报账单id
     */
    private Long paymentid;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收票时间
     */
    private LocalDateTime collectTime;

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
    @TableField(fill = FieldFill.INSERT)
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
    @TableField(fill = FieldFill.INSERT_UPDATE)
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
     * 关联供应商ID
     */
    public static final String SUPPLIER_ID = "supplierId";

    /**
    * 票据号码
    */
    public static final String BILL_NUM = "billNum";

    /**
    * 票据代码
    */
    public static final String BILL_CODE = "billCode";

    /**
    * 票据类型编码
    */
    public static final String TYPE_CODE = "typeCode";

    /**
    * 票据类型
    */
    public static final String TYPE = "type";

    /**
    * 收票金额
    */
    public static final String AMOUNT = "amount";

    /**
    * 抬头
    */
    public static final String TITLE = "title";

    /**
    * 收票时间
    */
    public static final String COLLECT_TIME = "collectTime";

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
                                    BILL_NUM,
                                    BILL_CODE,
                                    TYPE_CODE,
                                    TYPE,
                                    AMOUNT,
                                    TITLE,
                                    COLLECT_TIME,
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
