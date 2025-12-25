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
* 合同修改记录表 更新请求参数
* </p>
*
* @author zhangfuyu
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同修改记录表下拉列表请求参数", description = "合同修改记录表")
public class ContractRecordInfoListF {

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
