package com.wishare.contract.apps.fo.revision.attachment;


import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 关联附件管理表 分页请求参数
* </p>
*
* @author chenglong
* @since 2023-06-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "关联附件管理表分页请求参数", description = "关联附件管理表")
public class AttachmentPageF {

    /**
    * businessType
    */
    @ApiModelProperty("业务类型，参考FileSaveTypeEnum")
    private Integer businessType;
    /**
    * businessId
    */
    @ApiModelProperty("业务主键ID（该文件所属数据的主键ID）")
    @Length(message = "业务主键ID（该文件所属数据的主键ID）不可超过 40 个字符",max = 40)
    private String businessId;
    /**
    * fileKey
    */
    @ApiModelProperty("fileKey")
    @Length(message = "fileKey不可超过 255 个字符",max = 255)
    private String fileKey;
    /**
    * name
    */
    @ApiModelProperty("文件名")
    @Length(message = "文件名不可超过 255 个字符",max = 255)
    private String name;
    /**
    * suffix
    */
    @ApiModelProperty("文件后缀")
    @Length(message = "文件后缀不可超过 255 个字符",max = 255)
    private String suffix;
    /**
    * size
    */
    @ApiModelProperty("文件大小")
    private Long size;
    /**
    * fileSizeStr
    */
    @ApiModelProperty("文件大小格式化字符串")
    @Length(message = "文件大小格式化字符串不可超过 40 个字符",max = 40)
    private String fileSizeStr;
    /**
    * type
    */
    @ApiModelProperty("文件类型:1代表正式文件,0代表临时文件")
    private Integer type;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人ID")
    @Length(message = "创建人ID不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * operator
    */
    @ApiModelProperty("操作人ID")
    @Length(message = "操作人ID不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * gmtModify
    */
    @ApiModelProperty("修改时间(最近操作时间)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人姓名")
    @Length(message = "创建人姓名不可超过 50 个字符",max = 50)
    private String creatorName;
    /**
    * operatorName
    */
    @ApiModelProperty("修改人姓名")
    @Length(message = "修改人姓名不可超过 50 个字符",max = 50)
    private String operatorName;
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"businessType\",\"businessId\",\"fileKey\",\"name\",\"suffix\",\"size\",\"fileSizeStr\",\"type\",\"deleted\",\"tenantId\",\"creator\",\"operator\",\"gmtCreate\",\"gmtModify\",\"creatorName\",\"operatorName\"]"
        + "id 主键ID"
        + "businessType 业务类型，参考FileSaveTypeEnum"
        + "businessId 业务主键ID（该文件所属数据的主键ID）"
        + "fileKey fileKey"
        + "name 文件名"
        + "suffix 文件后缀"
        + "size 文件大小"
        + "fileSizeStr 文件大小格式化字符串"
        + "type 文件类型:1代表正式文件,0代表临时文件"
        + "deleted 是否删除 false 未删除 null 删除"
        + "tenantId 租户id"
        + "creator 创建人ID"
        + "operator 操作人ID"
        + "gmtCreate 创建时间"
        + "gmtModify 修改时间(最近操作时间)"
        + "creatorName 创建人姓名"
        + "operatorName 修改人姓名")
    private List<String> fields;


}
