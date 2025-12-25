package com.wishare.contract.domains.entity.revision.invoice;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 收票款项明细表
 * </p>
 *
 * @author zhangfuyu
 * @since 2024-05-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_settlements_bill_item")
public class ContractSettlementsBillItemE {

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
     * 业务类型ID
     */
    private String bussTypeId;

    /**
     * 业务类型名称
     */
    private String bussTypeName;

    /**
     * 变动ID
     */
    private String changeId;

    /**
     * 变动名称
     */
    private String changeName;

    /**
     * 款项
     */
    private String itemId;

    /**
     * 款项名称
     */
    private String itemName;

    /**
     * 核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}
     */
    private String writeOffInfo;

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
    * 业务类型ID
    */
    public static final String BUSS_TYPE_ID = "bussTypeId";

    /**
    * 业务类型名称
    */
    public static final String BUSS_TYPE_NAME = "bussTypeName";

    /**
    * 变动ID
    */
    public static final String CHANGE_ID = "changeId";

    /**
    * 变动名称
    */
    public static final String CHANGE_NAME = "changeName";

    /**
    * 款项
    */
    public static final String ITEM_ID = "itemId";

    /**
    * 款项名称
    */
    public static final String ITEM_NAME = "itemName";

    /**
    * 核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}
    */
    public static final String WRITE_OFF_INFO = "writeOffInfo";

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
                                    BUSS_TYPE_ID,
                                    BUSS_TYPE_NAME,
                                    CHANGE_ID,
                                    CHANGE_NAME,
                                    ITEM_ID,
                                    ITEM_NAME,
                                    WRITE_OFF_INFO,
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
