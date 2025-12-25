package com.wishare.contract.apps.fo.revision.attachment;


import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 关联附件管理表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-06-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "关联附件管理表原始请求参数", description = "关联附件管理表原始请求参数，会跟着表重新生成")
public class AttachmentRawF {

    /**
    * 业务类型，参考FileSaveTypeEnum 不可为空
    */
    @ApiModelProperty(value = "业务类型，参考FileSaveTypeEnum",required = true)
    private Integer businessType;
    /**
    * 业务主键ID（该文件所属数据的主键ID） 不可为空
    */
    @ApiModelProperty(value = "业务主键ID（该文件所属数据的主键ID）",required = true)
    @NotBlank(message = "业务主键ID（该文件所属数据的主键ID）不可为空")
    @Length(message = "业务主键ID（该文件所属数据的主键ID）不可超过 40 个字符",max = 40)
    private String businessId;
    /**
    * fileKey 不可为空
    */
    @ApiModelProperty(value = "fileKey",required = true)
    @NotBlank(message = "fileKey不可为空")
    @Length(message = "fileKey不可超过 255 个字符",max = 255)
    private String fileKey;
    /**
    * 文件名 不可为空
    */
    @ApiModelProperty(value = "文件名",required = true)
    @NotBlank(message = "文件名不可为空")
    @Length(message = "文件名不可超过 255 个字符",max = 255)
    private String name;
    /**
    * 文件后缀
    */
    @ApiModelProperty("文件后缀")
    @Length(message = "文件后缀不可超过 255 个字符",max = 255)
    private String suffix;
    /**
    * 文件大小 不可为空
    */
    @ApiModelProperty(value = "文件大小",required = true)
    private Long size;
    /**
    * 文件大小格式化字符串
    */
    @ApiModelProperty("文件大小格式化字符串")
    @Length(message = "文件大小格式化字符串不可超过 40 个字符",max = 40)
    private String fileSizeStr;
    /**
    * 文件类型:1代表正式文件,0代表临时文件 不可为空
    */
    @ApiModelProperty(value = "文件类型:1代表正式文件,0代表临时文件",required = true)
    private Integer type;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
