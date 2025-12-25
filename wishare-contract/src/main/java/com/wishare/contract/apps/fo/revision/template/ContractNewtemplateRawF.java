package com.wishare.contract.apps.fo.revision.template;


import java.math.BigDecimal;
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
* 新合同范本表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表原始请求参数", description = "新合同范本表原始请求参数，会跟着表重新生成")
public class ContractNewtemplateRawF {

    /**
    * 范本名称 不可为空
    */
    @ApiModelProperty(value = "范本名称",required = true)
    @NotBlank(message = "范本名称不可为空")
    @Length(message = "范本名称不可超过 100 个字符",max = 100)
    private String name;
    /**
    * 合同分类表id 不可为空
    */
    @ApiModelProperty(value = "合同分类表id",required = true)
    private Long categoryId;
    /**
    * 范本版本 不可为空
    */
    @ApiModelProperty(value = "范本版本",required = true)
    @Digits(integer = 5,fraction =1,message = "范本版本不正确")
    @NotNull(message = "范本版本不可为空")
    private BigDecimal version;
    /**
    * 引用次数 不可为空
    */
    @ApiModelProperty(value = "引用次数",required = true)
    private Integer useCount;
    /**
    * 引用状态：0未被引用  1被引用 不可为空
    */
    @ApiModelProperty(value = "引用状态：0未被引用  1被引用",required = true)
    private Boolean useStatus;
    /**
    * 文件名 不可为空
    */
    @ApiModelProperty(value = "文件内容",required = true)
    private String fileContent;
    /**
    * 状态：0启用，1禁用 不可为空
    */
    @ApiModelProperty(value = "状态：0启用，1禁用",required = true)
    private Boolean status;
    /**
    * 合同分类路径名 不可为空
    */
    @ApiModelProperty(value = "合同分类路径名",required = true)
    @NotBlank(message = "合同分类路径名不可为空")
    @Length(message = "合同分类路径名不可超过 255 个字符",max = 255)
    private String categoryPathName;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
