package com.wishare.contract.domains.entity.revision.template;

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
 * 合同修改记录表
 * </p>
 *
 * @author zhangfuyu
 * @since 2023-07-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_record_info")
public class ContractRecordInfoE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 合同版本
     */
    private BigDecimal version;

    /**
     * 更新记录
     */
    private String fieldRecord;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 是否删除：0未删除，1已删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人id
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
     * 操作人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;


    /**
    * 主键ID
    */
    public static final String ID = "id";

    /**
    * 合同id
    */
    public static final String CONTRACT_ID = "contractId";

    /**
    * 合同名称
    */
    public static final String CONTRACT_NAME = "contractName";

    /**
    * 模板id
    */
    public static final String TEMPLATE_ID = "templateId";

    /**
    * 模板名称
    */
    public static final String TEMPLATE_NAME = "templateName";

    /**
    * 合同版本
    */
    public static final String VERSION = "version";

    /**
    * 更新记录
    */
    public static final String FIELD_RECORD = "fieldRecord";

    /**
    * 是否删除：0未删除，1已删除
    */
    public static final String DELETED = "deleted";

    /**
    * 租户id
    */
    public static final String TENANT_ID = "tenantId";

    /**
    * 创建人id
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
    * 操作人id
    */
    public static final String OPERATOR = "operator";

    /**
    * 操作人名称
    */
    public static final String OPERATOR_NAME = "operatorName";

    /**
    * 更新时间
    */
    public static final String GMT_MODIFY = "gmtModify";

    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    CONTRACT_NAME,
                                    TEMPLATE_ID,
                                    TEMPLATE_NAME,
                                    VERSION,
                                    FIELD_RECORD,
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
