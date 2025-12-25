package com.wishare.contract.domains.vo.revision.template;

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
* 新合同范本表视图对象
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表视图对象", description = "新合同范本表视图对象")
public class ContractNewtemplateV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 范本名称
    */
    @ApiModelProperty("范本名称")
    private String name;
    /**
    * 合同分类表id
    */
    @ApiModelProperty("合同分类表id")
    private Long categoryId;

    /**
    * 范本版本
    */
    @ApiModelProperty("范本版本")
    private BigDecimal version;
    /**
    * 引用次数
    */
    @ApiModelProperty("引用次数")
    private Integer useCount;
    /**
    * 引用状态：0未被引用  1被引用
    */
    @ApiModelProperty("引用状态：0未被引用  1被引用")
    private Boolean useStatus;
    /**
    * 文件内容
    */
    @ApiModelProperty("文件内容")
    private String fileContent;

    /**
    * 状态：0启用，1禁用
    */
    @ApiModelProperty("状态：0启用，1禁用")
    private Boolean status;
    /**
    * 合同分类路径名
    */
    @ApiModelProperty("合同分类路径名")
    private String categoryPathName;
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
