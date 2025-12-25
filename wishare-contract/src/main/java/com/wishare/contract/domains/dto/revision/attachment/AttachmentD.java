package com.wishare.contract.domains.dto.revision.attachment;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

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
@ApiModel(value = "attachment请求对象", description = "关联附件管理表")
public class AttachmentD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("业务类型，参考FileSaveTypeEnum")
    private Integer businessType;
    @ApiModelProperty("业务主键ID（该文件所属数据的主键ID）")
    private String businessId;
    @ApiModelProperty("fileKey")
    private String fileKey;
    @ApiModelProperty("文件名")
    private String name;
    @ApiModelProperty("文件后缀")
    private String suffix;
    @ApiModelProperty("文件大小")
    private Long size;
    @ApiModelProperty("文件大小格式化字符串")
    private String fileSizeStr;
    @ApiModelProperty("文件类型:1代表正式文件,0代表临时文件")
    private Integer type;
    @ApiModelProperty("是否删除 false 未删除 null 删除")
    private Boolean deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("修改时间(最近操作时间)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("修改人姓名")
    private String operatorName;

}
