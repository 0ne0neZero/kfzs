package com.wishare.contract.domains.dto.revision.template;

import java.math.BigDecimal;
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
@ApiModel(value = "contract_newtemplate请求对象", description = "新合同范本表")
public class ContractNewtemplateD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("范本名称")
    private String name;
    @ApiModelProperty("合同分类表id")
    private Long categoryId;
    @ApiModelProperty("父级范本id")
    private Long parentId;
    @ApiModelProperty("范本版本")
    private BigDecimal version;
    @ApiModelProperty("引用次数")
    private Integer useCount;
    @ApiModelProperty("引用状态：0未被引用  1被引用")
    private Boolean useStatus;
    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("文件url")
    private String fileUrl;
    @ApiModelProperty("状态：0启用，1禁用")
    private Boolean status;
    @ApiModelProperty("合同分类路径名")
    private String categoryPathName;
    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Boolean deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人id")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
