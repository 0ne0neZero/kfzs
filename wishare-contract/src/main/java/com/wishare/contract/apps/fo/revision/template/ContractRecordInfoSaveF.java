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
* 合同修改记录表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同修改记录表新增请求参数", description = "合同修改记录表新增请求参数")
public class ContractRecordInfoSaveF {

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

}
