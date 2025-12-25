package com.wishare.contract.apps.fo.revision.invoice;


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
* 收票明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票明细表请求参数", description = "收票明细表")
public class ContractSettlementsBillDetailsF {

    /**
    * id
    */
    @ApiModelProperty("主键ID")
    @Length(message = "主键ID不可超过 40 个字符",max = 40)
    private String id;
    /**
    * billId
    */
    @ApiModelProperty("结算单id")
    @Length(message = "结算单id不可超过 40 个字符",max = 40)
    private String billId;
    /**
    * billNum
    */
    @ApiModelProperty("票据号码")
    @Length(message = "票据号码不可超过 50 个字符",max = 50)
    private String billNum;
    /**
    * billCode
    */
    @ApiModelProperty("票据代码")
    @Length(message = "票据代码不可超过 50 个字符",max = 50)
    private String billCode;
    /**
    * billType
    */
    @ApiModelProperty("票据类型")
    private Boolean billType;
    /**
    * amount
    */
    @ApiModelProperty("发票金额")
    @Digits(integer = 18,fraction =2,message = "发票金额不正确")
    private BigDecimal amount;
    /**
    * billDate
    */
    @ApiModelProperty("收票时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    /**
    * extend1
    */
    @ApiModelProperty("扩展字段1")
    @Length(message = "扩展字段1不可超过 100 个字符",max = 100)
    private String extend1;
    /**
    * extend2
    */
    @ApiModelProperty("扩展字段2")
    @Length(message = "扩展字段2不可超过 100 个字符",max = 100)
    private String extend2;
    /**
    * extend3
    */
    @ApiModelProperty("扩展字段3")
    @Length(message = "扩展字段3不可超过 100 个字符",max = 100)
    private String extend3;
    /**
    * deleted
    */
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Boolean deleted;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * creator
    */
    @ApiModelProperty("创建人ID")
    @Length(message = "创建人ID不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人姓名")
    @Length(message = "创建人姓名不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * operator
    */
    @ApiModelProperty("操作人ID")
    @Length(message = "操作人ID不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人姓名")
    @Length(message = "操作人姓名不可超过 40 个字符",max = 40)
    private String operatorName;

}
