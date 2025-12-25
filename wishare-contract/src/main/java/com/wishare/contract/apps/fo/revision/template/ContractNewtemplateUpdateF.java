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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 新合同范本表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表更新请求参数", description = "新合同范本表")
public class ContractNewtemplateUpdateF {

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

}
