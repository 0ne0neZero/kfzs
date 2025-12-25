package com.wishare.contract.domains.entity.revision.attachment;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 关联附件管理表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("attachment")
public class AttachmentE {

    /**
     * 主键ID
     */
    @TableId(value = ID)
    private String id;

    /**
     * 业务类型，参考FileSaveTypeEnum
     */
    private Integer businessType;

    /**
     * 业务主键ID（该文件所属数据的主键ID）
     */
    private String businessId;

    /**
     * fileKey
     */
    private String fileKey;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件大小格式化字符串
     */
    private String fileSizeStr;

    /**
     * 文件类型:1代表正式文件,0代表临时文件
     */
    private Integer type;

    /**
     * 是否删除 false 未删除 null 删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间(最近操作时间)
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 影像系统返回的附件文件唯一ID
     */
    private String fileuuid;


    /**
     * 主键ID
     */
    public static final String ID = "id";

    /**
     * 业务类型，参考FileSaveTypeEnum
     */
    public static final String BUSINESS_TYPE = "businessType";

    /**
     * 业务主键ID（该文件所属数据的主键ID）
     */
    public static final String BUSINESS_ID = "businessId";

    /**
     * fileKey
     */
    public static final String FILE_KEY = "fileKey";

    /**
     * 文件名
     */
    public static final String NAME = "name";

    /**
     * 文件后缀
     */
    public static final String SUFFIX = "suffix";

    /**
     * 文件大小
     */
    public static final String SIZE = "size";

    /**
     * 文件大小格式化字符串
     */
    public static final String FILE_SIZE_STR = "fileSizeStr";

    /**
     * 文件类型:1代表正式文件,0代表临时文件
     */
    public static final String TYPE = "type";

    /**
     * 是否删除 false 未删除 null 删除
     */
    public static final String DELETED = "deleted";

    /**
     * 租户id
     */
    public static final String TENANT_ID = "tenantId";

    /**
     * 创建人ID
     */
    public static final String CREATOR = "creator";

    /**
     * 操作人ID
     */
    public static final String OPERATOR = "operator";

    /**
     * 创建时间
     */
    public static final String GMT_CREATE = "gmtCreate";

    /**
     * 修改时间(最近操作时间)
     */
    public static final String GMT_MODIFY = "gmtModify";

    /**
     * 创建人姓名
     */
    public static final String CREATOR_NAME = "creatorName";

    /**
     * 修改人姓名
     */
    public static final String OPERATOR_NAME = "operatorName";

    /**
     * 影像系统返回的附件文件唯一ID
     */
    public static final String FILEUUID = "fileuuid";

    /**
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final List<String> ALLOW_FIELDS = Arrays.asList(ID,
            BUSINESS_TYPE,
            BUSINESS_ID,
            FILE_KEY,
            NAME,
            SUFFIX,
            SIZE,
            FILE_SIZE_STR,
            TYPE,
            DELETED,
            TENANT_ID,
            CREATOR,
            OPERATOR,
            GMT_CREATE,
            GMT_MODIFY,
            CREATOR_NAME,
            OPERATOR_NAME,
            FILEUUID);
    /**
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    public static final String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
