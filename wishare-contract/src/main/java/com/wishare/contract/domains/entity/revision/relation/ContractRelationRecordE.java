package com.wishare.contract.domains.entity.revision.relation;

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
 * 
 * </p>
 *
 * @author chenglong
 * @since 2023-06-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_relation_record")
public class ContractRelationRecordE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 操作类型  1变更  2续签
     */
    private Integer type;

    /**
     * 合同类型 1支出  2收入
     */
    private Integer contractType;

    /**
     * 新增合同数据的主键ID
     */
    private String addId;

    /**
     * 原先合同数据的主键ID
     */
    private String oldId;

    /**
     * 是否已执行  1已执行  0未执行
     */
    private Integer isDone;

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
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 操作类型  1变更  2续签
    */
    public static final String TYPE = "type";

    public static final String CONTRACT_TYPE = "contractType";

    /**
    * 新增合同数据的主键ID
    */
    public static final String ADD_ID = "addId";

    /**
    * 原先合同数据的主键ID
    */
    public static final String OLD_ID = "oldId";

    /**
     * 是否已执行  1已执行  0未执行
     */
    public static final String IS_DONE = "isDone";

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
                                    TYPE,
                                    ADD_ID,
                                    OLD_ID,
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
