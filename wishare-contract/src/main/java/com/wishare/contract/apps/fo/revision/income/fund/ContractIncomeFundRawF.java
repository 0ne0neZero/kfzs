package com.wishare.contract.apps.fo.revision.income.fund;


import java.math.BigDecimal;
import java.time.LocalDate;
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
* 收入合同-款项表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同-款项表原始请求参数", description = "收入合同-款项表原始请求参数，会跟着表重新生成")
public class ContractIncomeFundRawF {

    /**
    * 关联合同ID 不可为空
    */
    @ApiModelProperty(value = "关联合同ID",required = true)
    @NotBlank(message = "关联合同ID不可为空")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * 款项名称
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 50 个字符",max = 50)
    private String name;
    /**
    * 款项类型ID
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * 款项类型
    */
    @ApiModelProperty("款项类型")
    @Length(message = "款项类型不可超过 50 个字符",max = 50)
    private String type;
    /**
    * 金额
    */
    @ApiModelProperty("金额")
    @Digits(integer = 10,fraction =2,message = "金额不正确")
    private BigDecimal amount;
    /**
    * 税率ID
    */
    @ApiModelProperty("税率ID")
    @Length(message = "税率ID不可超过 40 个字符",max = 40)
    private String taxRateId;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    @Length(message = "税率不可超过 40 个字符",max = 40)
    private String taxRate;
    /**
    * 付费类型ID
    */
    @ApiModelProperty("付费类型ID")
    @Length(message = "付费类型ID不可超过 30 个字符",max = 30)
    private String payTypeId;
    /**
    * 付费类型
    */
    @ApiModelProperty("付费类型")
    @Length(message = "付费类型不可超过 50 个字符",max = 50)
    private String payType;
    /**
    * 付费方式ID
    */
    @ApiModelProperty("付费方式ID")
    @Length(message = "付费方式ID不可超过 30 个字符",max = 30)
    private String payWayId;
    /**
    * 付费方式
    */
    @ApiModelProperty("付费方式")
    @Length(message = "付费方式不可超过 50 个字符",max = 50)
    private String payWay;
    /**
    * 开始日期
    */
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    /**
    * 结束日期
    */
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    /**
    * 收费标准ID
    */
    @ApiModelProperty("收费标准ID")
    @Length(message = "收费标准ID不可超过 30 个字符",max = 30)
    private String standardId;
    /**
    * 收费标准
    */
    @ApiModelProperty("收费标准")
    @Length(message = "收费标准不可超过 50 个字符",max = 50)
    private String standard;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 255 个字符",max = 255)
    private String remark;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
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
