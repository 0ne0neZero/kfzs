package com.wishare.finance.infrastructure.remote.vo.contract;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* <p>
* 关联附件管理表视图对象
* </p>
*
* @author chenglong
* @since 2023-06-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "关联附件管理表视图对象", description = "关联附件管理表视图对象")
public class AttachmentV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 业务类型，参考FileSaveTypeEnum
    */
    @ApiModelProperty("业务类型，参考FileSaveTypeEnum")
    private Integer businessType;
    /**
    * 业务主键ID（该文件所属数据的主键ID）
    */
    @ApiModelProperty("业务主键ID（该文件所属数据的主键ID）")
    private String businessId;
    /**
    * fileKey
    */
    @ApiModelProperty("fileKey")
    private String fileKey;
    /**
    * 文件名
    */
    @ApiModelProperty("文件名")
    private String name;
    /**
    * 文件后缀
    */
    @ApiModelProperty("文件后缀")
    private String suffix;
    /**
    * 文件大小
    */
    @ApiModelProperty("文件大小")
    private Long size;
    /**
    * 文件大小格式化字符串
    */
    @ApiModelProperty("文件大小格式化字符串")
    private String fileSizeStr;
    /**
    * 文件类型:1代表正式文件,0代表临时文件
    */
    @ApiModelProperty("文件类型:1代表正式文件,0代表临时文件")
    private Integer type;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人ID
    */
    @ApiModelProperty("创建人ID")
    private String creator;
    /**
    * 操作人ID
    */
    @ApiModelProperty("操作人ID")
    private String operator;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 修改时间(最近操作时间)
    */
    @ApiModelProperty("修改时间(最近操作时间)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * 创建人姓名
    */
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    /**
    * 修改人姓名
    */
    @ApiModelProperty("修改人姓名")
    private String operatorName;
    /**
     * 附件文件唯一ID
     */
    @ApiModelProperty("附件文件唯一ID")
    private String fileuuid;

    /**
     * 附件文件唯一ID
     */
    @ApiModelProperty("合同类型")
    private Integer contractType;

    @ApiModelProperty("合同推送状态")
    private Integer contractNature;
}
