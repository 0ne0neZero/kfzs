package com.wishare.contract.apps.fo.revision.invoice;


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
* 结算单计量明细表 更新请求参数
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单计量明细表下拉列表请求参数", description = "结算单计量明细表")
public class ContractSettlementsBillCalculateListF {

    /**
    * billId
    */
    @ApiModelProperty("关联票据ID")
    @Length(message = "关联票据ID不可超过 50 个字符",max = 50)
    private String billId;
    /**
    * typeId
    */
    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;
    /**
    * type
    */
    @ApiModelProperty("款项类型名称")
    @Length(message = "款项类型名称不可超过 50 个字符",max = 50)
    private String type;
    /**
    * amount
    */
    @ApiModelProperty("结算金额")
    @Digits(integer = 10,fraction =2,message = "结算金额不正确")
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
    @Length(message = "税率ID不可超过 40 个字符",max = 40)
    private String taxRateId;
    /**
    * taxRate
    */
    @ApiModelProperty("税率")
    @Length(message = "税率不可超过 40 个字符",max = 40)
    private String taxRate;
    /**
    * taxRateAmount
    */
    @ApiModelProperty("税额")
    @Digits(integer = 10,fraction =6,message = "税额不正确")
    private BigDecimal taxRateAmount;
    /**
    * amountWithOutRate
    */
    @ApiModelProperty("不含税金额")
    @Digits(integer = 10,fraction =6,message = "不含税金额不正确")
    private BigDecimal amountWithOutRate;
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
