package com.wishare.contract.apps.fo.revision.template;


import java.math.BigDecimal;
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
* 新合同范本表 分页请求参数
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表分页请求参数", description = "新合同范本表")
public class ContractNewtemplatePageF {

    /**
    * name
    */
    @ApiModelProperty("范本名称")
    @Length(message = "范本名称不可超过 100 个字符",max = 100)
    private String name;
    /**
    * categoryId
    */
    @ApiModelProperty("合同分类表id")
    private Long categoryId;
    /**
    * version
    */
    @ApiModelProperty("范本版本")
    @Digits(integer = 5,fraction =1,message = "范本版本不正确")
    private BigDecimal version;
    /**
    * useCount
    */
    @ApiModelProperty("引用次数")
    private Integer useCount;
    /**
    * useStatus
    */
    @ApiModelProperty("引用状态：0未被引用  1被引用")
    private Boolean useStatus;
    /**
    * fileName
    */
    @ApiModelProperty("文件内容")
    private String fileContent;
    /**
    * status
    */
    @ApiModelProperty("状态：0启用，1禁用")
    private Boolean status;
    /**
    * categoryPathName
    */
    @ApiModelProperty("合同分类路径名")
    @Length(message = "合同分类路径名不可超过 255 个字符",max = 255)
    private String categoryPathName;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人id")
    @Length(message = "创建人id不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人名称")
    @Length(message = "创建人名称不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * operator
    */
    @ApiModelProperty("操作人id")
    @Length(message = "操作人id不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人名称")
    @Length(message = "操作人名称不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"name\",\"categoryId\",\"parentId\",\"version\",\"useCount\",\"useStatus\",\"fileName\",\"fileUrl\",\"status\",\"categoryPathName\",\"deleted\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\"]"
        + "id 主键ID"
        + "name 范本名称"
        + "categoryId 合同分类表id"
        + "parentId 父级范本id"
        + "version 范本版本"
        + "useCount 引用次数"
        + "useStatus 引用状态：0未被引用  1被引用"
        + "fileName 文件名"
        + "fileUrl 文件url"
        + "status 状态：0启用，1禁用"
        + "categoryPathName 合同分类路径名"
        + "deleted 是否删除：0未删除，1已删除"
        + "tenantId 租户id"
        + "creator 创建人id"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人id"
        + "operatorName 操作人名称"
        + "gmtModify 更新时间")
    private List<String> fields;


}
