package com.wishare.contract.domains.vo.revision.template;

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
* 合同范本字段配置表视图对象
* </p>
*
* @author zhangfuyu
* @since 2023-07-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同范本字段配置表视图对象", description = "合同范本字段配置表视图对象")
public class ContractTemplateConfigV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 字段名称
    */
    @ApiModelProperty("字段名称")
    private String name;
    /**
    * 字段
    */
    @ApiModelProperty("字段")
    private String fieldName;
    /**
    * 类型
    */
    @ApiModelProperty("类型")
    private Boolean type;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人id
    */
    @ApiModelProperty("创建人id")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人id
    */
    @ApiModelProperty("操作人id")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
