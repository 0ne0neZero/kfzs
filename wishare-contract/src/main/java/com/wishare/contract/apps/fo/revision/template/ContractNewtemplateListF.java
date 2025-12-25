package com.wishare.contract.apps.fo.revision.template;


import java.math.BigDecimal;
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
* 新合同范本表 更新请求参数
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表下拉列表请求参数", description = "新合同范本表")
public class ContractNewtemplateListF {

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
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private String indexId;
}
