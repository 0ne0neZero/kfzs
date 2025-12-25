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
* 合同修改记录表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同修改记录表更新请求参数", description = "合同修改记录表")
public class ContractRecordInfoUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;
    /**
    * contractId
    */
    @ApiModelProperty("合同id")
    @Length(message = "合同id不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * contractName
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 100 个字符",max = 100)
    private String contractName;
    /**
    * templateId
    */
    @ApiModelProperty("模板id")
    @Length(message = "模板id不可超过 100 个字符",max = 100)
    private String templateId;
    /**
    * templateName
    */
    @ApiModelProperty("模板名称")
    @Length(message = "模板名称不可超过 100 个字符",max = 100)
    private String templateName;
    /**
    * version
    */
    @ApiModelProperty("合同版本")
    @Digits(integer = 5,fraction =1,message = "合同版本不正确")
    private BigDecimal version;
    /**
    * fieldRecord
    */
    @ApiModelProperty("更新记录")
    @Length(message = "更新记录不可超过 1,000 个字符",max = 1000)
    private String fieldRecord;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 64 个字符",max = 64)
    private String tenantId;

}
