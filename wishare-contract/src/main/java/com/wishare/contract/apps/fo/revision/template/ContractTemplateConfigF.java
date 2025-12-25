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
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 合同范本字段配置表
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同范本字段配置表请求参数", description = "合同范本字段配置表")
public class ContractTemplateConfigF {

    /**
    * id
    */
    @ApiModelProperty("主键ID")
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
    * deleted
    */
    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Boolean deleted;
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

}
