package com.wishare.contract.apps.fo.revision.template;


import java.math.BigDecimal;
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
* 合同修改记录表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同修改记录表原始请求参数", description = "合同修改记录表原始请求参数，会跟着表重新生成")
public class ContractRecordInfoRawF {

    /**
    * 合同id 不可为空
    */
    @ApiModelProperty(value = "合同id",required = true)
    @NotBlank(message = "合同id不可为空")
    @Length(message = "合同id不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * 合同名称 不可为空
    */
    @ApiModelProperty(value = "合同名称",required = true)
    @NotBlank(message = "合同名称不可为空")
    @Length(message = "合同名称不可超过 100 个字符",max = 100)
    private String contractName;
    /**
    * 模板id 不可为空
    */
    @ApiModelProperty(value = "模板id",required = true)
    @NotBlank(message = "模板id不可为空")
    @Length(message = "模板id不可超过 100 个字符",max = 100)
    private String templateId;
    /**
    * 模板名称 不可为空
    */
    @ApiModelProperty(value = "模板名称",required = true)
    @NotBlank(message = "模板名称不可为空")
    @Length(message = "模板名称不可超过 100 个字符",max = 100)
    private String templateName;
    /**
    * 合同版本 不可为空
    */
    @ApiModelProperty(value = "合同版本",required = true)
    @Digits(integer = 5,fraction =1,message = "合同版本不正确")
    @NotNull(message = "合同版本不可为空")
    private BigDecimal version;
    /**
    * 更新记录 不可为空
    */
    @ApiModelProperty(value = "更新记录",required = true)
    @NotBlank(message = "更新记录不可为空")
    @Length(message = "更新记录不可超过 1,000 个字符",max = 1000)
    private String fieldRecord;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"contractName\",\"templateId\",\"templateName\",\"version\",\"fieldRecord\",\"deleted\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\"]"
        + "id 主键ID"
        + "contractId 合同id"
        + "contractName 合同名称"
        + "templateId 模板id"
        + "templateName 模板名称"
        + "version 合同版本"
        + "fieldRecord 更新记录"
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
