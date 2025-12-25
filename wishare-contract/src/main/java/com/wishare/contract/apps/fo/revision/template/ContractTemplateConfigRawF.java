package com.wishare.contract.apps.fo.revision.template;


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
* 合同范本字段配置表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同范本字段配置表原始请求参数", description = "合同范本字段配置表原始请求参数，会跟着表重新生成")
public class ContractTemplateConfigRawF {

    /**
    * 字段名称 不可为空
    */
    @ApiModelProperty(value = "字段名称",required = true)
    @NotBlank(message = "字段名称不可为空")
    @Length(message = "字段名称不可超过 100 个字符",max = 100)
    private String name;
    /**
    * 字段 不可为空
    */
    @ApiModelProperty(value = "字段",required = true)
    @NotBlank(message = "字段不可为空")
    @Length(message = "字段不可超过 100 个字符",max = 100)
    private String fieldName;
    /**
    * 类型 不可为空
    */
    @ApiModelProperty(value = "类型",required = true)
    private Boolean type;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"name\",\"fieldName\",\"type\",\"deleted\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\"]"
        + "id 主键ID"
        + "name 字段名称"
        + "fieldName 字段"
        + "type 类型"
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
