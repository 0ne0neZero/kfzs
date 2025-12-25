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
 * 新合同范本表
 * </p>
 *
 * @author zhangfy
 * @since 2023-07-21
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_newtemplate")
public class ContractNewtemplateE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 范本名称
     */
    private String name;

    /**
     * 合同分类表id
     */
    private Long categoryId;


    /**
     * 范本版本
     */
    private BigDecimal version;

    /**
     * 引用次数
     */
    private Integer useCount;

    /**
     * 引用状态：0未被引用  1被引用
     */
    private Boolean useStatus;

    /**
     * 文件名
     */
    private String fileContent;


    /**
     * 状态：0启用，1禁用
     */
    private Boolean status;

    /**
     * 合同分类路径名
     */
    private String categoryPathName;

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
    * 范本名称
    */
    public static final String NAME = "name";

    /**
    * 合同分类表id
    */
    public static final String CATEGORY_ID = "categoryId";

    /**
    * 父级范本id
    */
    public static final String PARENT_ID = "parentId";

    /**
    * 范本版本
    */
    public static final String VERSION = "version";

    /**
    * 引用次数
    */
    public static final String USE_COUNT = "useCount";

    /**
    * 引用状态：0未被引用  1被引用
    */
    public static final String USE_STATUS = "useStatus";

    /**
    * 文件名
    */
    public static final String FILE_NAME = "fileName";

    /**
    * 文件url
    */
    public static final String FILE_URL = "fileUrl";

    /**
    * 状态：0启用，1禁用
    */
    public static final String STATUS = "status";

    /**
    * 合同分类路径名
    */
    public static final String CATEGORY_PATH_NAME = "categoryPathName";

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
                                    NAME,
                                    CATEGORY_ID,
                                    PARENT_ID,
                                    VERSION,
                                    USE_COUNT,
                                    USE_STATUS,
                                    FILE_NAME,
                                    FILE_URL,
                                    STATUS,
                                    CATEGORY_PATH_NAME,
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
