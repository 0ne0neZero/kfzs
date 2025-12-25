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
* 新合同范本表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfy
* @since 2023-07-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "新合同范本表新增请求参数", description = "新合同范本表新增请求参数")
public class ContractNewtemplateSaveF {

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
    @ApiModelProperty(value = "范本版本")
    @Digits(integer = 5,fraction =1,message = "范本版本不正确")
    @NotNull(message = "范本版本不可为空")
    private BigDecimal version;
    /**
    * 引用次数 不可为空
    */
    @ApiModelProperty(value = "引用次数")
    private Integer useCount;
    /**
    * 引用状态：0未被引用  1被引用 不可为空
    */
    @ApiModelProperty(value = "引用状态：0未被引用  1被引用")
    private Boolean useStatus;
    /**
    * 文件名 不可为空
    */
    @ApiModelProperty(value = "文件名")
    private String fileContent;
    /**
    * 状态：0启用，1禁用 不可为空
    */
    @ApiModelProperty(value = "状态：0启用，1禁用")
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
    @ApiModelProperty(value = "租户id")
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

}
