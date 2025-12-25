package com.wishare.contract.domains.dto.revision.template;

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
@ApiModel(value = "contract_template_config请求对象", description = "合同范本字段配置表")
public class ContractTemplateConfigD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("字段名称")
    private String name;
    @ApiModelProperty("字段")
    private String fieldName;
    @ApiModelProperty("类型")
    private Boolean type;
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
