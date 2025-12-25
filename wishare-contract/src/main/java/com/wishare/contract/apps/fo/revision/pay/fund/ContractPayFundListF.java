package com.wishare.contract.apps.fo.revision.pay.fund;


import java.math.BigDecimal;
import java.time.LocalDate;
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
* 支出合同-款项表 更新请求参数
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同-款项表下拉列表请求参数", description = "支出合同-款项表")
public class ContractPayFundListF {

    /**
    * name
    */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 50 个字符",max = 50)
    private String name;
    @ApiModelProperty("关联合同ID")
    private String contractId;
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
    * chargeItemId
    */
    @ApiModelProperty("费项ID")
    private Long chargeItemId;
    /**
    * chargeItem
    */
    @ApiModelProperty("费项")
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * taxRateId
    */
    @ApiModelProperty("税率ID")
    private String taxRateId;
    /**
    * taxRate
    */
    @ApiModelProperty("税率")
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
    @ApiModelProperty("列表返回长度，不传入时默认20")
    private Integer limit;
    @ApiModelProperty("最后一个数据的ID，用于下拉时触发加载更多动作")
    private String indexId;
}
