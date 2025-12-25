package com.wishare.contract.apps.fo.revision.template;


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
* 合同范本字段配置表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同范本字段配置表新增请求参数", description = "合同范本字段配置表新增请求参数")
public class ContractTemplateConfigSaveF {

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

}
