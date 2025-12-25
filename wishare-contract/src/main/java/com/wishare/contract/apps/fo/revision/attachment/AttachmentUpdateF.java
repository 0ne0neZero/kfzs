package com.wishare.contract.apps.fo.revision.attachment;


import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 关联附件管理表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "关联附件管理表更新请求参数", description = "关联附件管理表")
public class AttachmentUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 40 个字符",max = 40)
    private String id;
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

}
