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
* 收票明细表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票明细表新增请求参数", description = "收票明细表新增请求参数")
public class ContractSettlementsBillDetailsSaveF {

    /**
    * 结算单id 不可为空
    */
    @ApiModelProperty(value = "结算单id",required = true)
    @NotBlank(message = "结算单id不可为空")
    @Length(message = "结算单id不可超过 40 个字符",max = 40)
    private String billId;
    /**
    * 票据号码
    */
    @ApiModelProperty("票据号码")
    @Length(message = "票据号码不可超过 50 个字符",max = 50)
    private String billNum;
    /**
    * 票据代码
    */
    @ApiModelProperty("票据代码")
    @Length(message = "票据代码不可超过 50 个字符",max = 50)
    private String billCode;
    /**
    * 票据类型 不可为空
    */
    @ApiModelProperty(value = "票据类型")
    private Integer billType;
    /**
     * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝
     */
    @ApiModelProperty(value = "审核状态")
    private Integer reviewStatus;
    /**
    * 发票金额
    */
    @ApiModelProperty("发票金额")
    @Digits(integer = 18,fraction =2,message = "发票金额不正确")
    private BigDecimal amount;
    /**
     * 发票金额
     */
    @ApiModelProperty("发票税额")
    @Digits(integer = 18,fraction =2,message = "发票税额不正确")
    private BigDecimal amountRate;

    /**
    * 收票时间
    */
    @ApiModelProperty("收票时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    /**
    * 扩展字段1
    */
    @ApiModelProperty("扩展字段1")
    @Length(message = "扩展字段1不可超过 100 个字符",max = 100)
    private String extend1;
    /**
    * 扩展字段2
    */
    @ApiModelProperty("扩展字段2")
    @Length(message = "扩展字段2不可超过 100 个字符",max = 100)
    private String extend2;
    /**
    * 扩展字段3
    */
    @ApiModelProperty("扩展字段3")
    @Length(message = "扩展字段3不可超过 100 个字符",max = 100)
    private String extend3;
    /**
    * 租户id 不可为空
    */
    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不可为空")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

}
