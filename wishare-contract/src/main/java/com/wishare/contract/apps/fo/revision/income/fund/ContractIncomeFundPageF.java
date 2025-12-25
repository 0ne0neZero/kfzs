package com.wishare.contract.apps.fo.revision.income.fund;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
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
* 收入合同-款项表 分页请求参数
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同-款项表分页请求参数", description = "收入合同-款项表")
public class ContractIncomeFundPageF {

    /**
    * contractId
    */
    @ApiModelProperty("关联合同ID")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * name
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 50 个字符",max = 50)
    private String name;
    /**
    * typeId
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * type
    */
    @ApiModelProperty("款项类型")
    @Length(message = "款项类型不可超过 50 个字符",max = 50)
    private String type;
    /**
    * amount
    */
    @ApiModelProperty("金额")
    @Digits(integer = 10,fraction =2,message = "金额不正确")
    private BigDecimal amount;
    /**
    * taxRateId
    */
    @ApiModelProperty("税率ID")
    @Length(message = "税率ID不可超过 40 个字符",max = 40)
    private String taxRateId;
    /**
    * taxRate
    */
    @ApiModelProperty("税率")
    @Length(message = "税率不可超过 40 个字符",max = 40)
    private String taxRate;
    /**
    * payTypeId
    */
    @ApiModelProperty("付费类型ID")
    @Length(message = "付费类型ID不可超过 30 个字符",max = 30)
    private String payTypeId;
    /**
    * payType
    */
    @ApiModelProperty("付费类型")
    @Length(message = "付费类型不可超过 50 个字符",max = 50)
    private String payType;
    /**
    * payWayId
    */
    @ApiModelProperty("付费方式ID")
    @Length(message = "付费方式ID不可超过 30 个字符",max = 30)
    private String payWayId;
    /**
    * payWay
    */
    @ApiModelProperty("付费方式")
    @Length(message = "付费方式不可超过 50 个字符",max = 50)
    private String payWay;
    /**
    * startDate
    */
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    /**
    * endDate
    */
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /**
    * standardId
    */
    @ApiModelProperty("收费标准ID")
    @Length(message = "收费标准ID不可超过 30 个字符",max = 30)
    private String standardId;
    /**
    * standard
    */
    @ApiModelProperty("收费标准")
    @Length(message = "收费标准不可超过 50 个字符",max = 50)
    private String standard;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 255 个字符",max = 255)
    private String remark;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人")
    @Length(message = "创建人不可超过 40 个字符",max = 40)
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
    @ApiModelProperty("操作人")
    @Length(message = "操作人不可超过 40 个字符",max = 40)
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
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"name\",\"typeId\",\"type\",\"amount\",\"taxRateId\",\"taxRate\",\"payTypeId\",\"payType\",\"payWayId\",\"payWay\",\"startDate\",\"endDate\",\"standardId\",\"standard\",\"remark\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 主键ID"
        + "contractId 关联合同ID"
        + "name 款项名称"
        + "typeId 款项类型ID"
        + "type 款项类型"
        + "amount 金额"
        + "taxRateId 税率ID"
        + "taxRate 税率"
        + "payTypeId 付费类型ID"
        + "payType 付费类型"
        + "payWayId 付费方式ID"
        + "payWay 付费方式"
        + "startDate 开始日期"
        + "endDate 结束日期"
        + "standardId 收费标准ID"
        + "standard 收费标准"
        + "remark 备注"
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "deleted 是否删除  0 正常 1 删除")
    private List<String> fields;


}
