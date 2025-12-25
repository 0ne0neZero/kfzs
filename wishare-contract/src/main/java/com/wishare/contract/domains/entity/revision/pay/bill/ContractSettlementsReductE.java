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
@TableName("contract_settlements_reduct")
public class ContractSettlementsReductE {

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
    private String type;

    /**
     * 减免金额
     */
    private BigDecimal amount;



    private String attachments;


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
    * 票据类型
    */
    public static final String TYPE = "type";

    /**
    * 收票金额
    */
    public static final String AMOUNT = "amount";


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
                                    TYPE,
                                    AMOUNT,
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
