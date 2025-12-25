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
* 合同范本字段配置表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同范本字段配置表更新请求参数", description = "合同范本字段配置表")
public class ContractTemplateConfigUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;
    /**
    * name
    */
    @ApiModelProperty("字段名称")
    @Length(message = "字段名称不可超过 100 个字符",max = 100)
    private String name;
    /**
    * fieldName
    */
    @ApiModelProperty("字段")
    @Length(message = "字段不可超过 100 个字符",max = 100)
    private String fieldName;
    /**
    * type
    */
    @ApiModelProperty("类型")
    private Boolean type;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

}
