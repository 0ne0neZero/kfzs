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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 收票明细表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票明细表更新请求参数", description = "收票明细表")
public class ContractSettlementsBillDetailsUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
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
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

}
