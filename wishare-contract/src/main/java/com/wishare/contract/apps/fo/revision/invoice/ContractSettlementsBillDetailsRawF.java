package com.wishare.contract.apps.fo.revision.invoice;


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
* 收票明细表 原生请求参数，该类会在每次重新自动生成时，重新生成！！！
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票明细表原始请求参数", description = "收票明细表原始请求参数，会跟着表重新生成")
public class ContractSettlementsBillDetailsRawF {

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
    @ApiModelProperty(value = "票据类型",required = true)
    private Boolean billType;
    /**
    * 发票金额
    */
    @ApiModelProperty("发票金额")
    @Digits(integer = 18,fraction =2,message = "发票金额不正确")
    private BigDecimal amount;
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

    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"billId\",\"billNum\",\"billCode\",\"billType\",\"amount\",\"billDate\",\"extend1\",\"extend2\",\"extend3\",\"deleted\",\"tenantId\",\"gmtCreate\",\"creator\",\"creatorName\",\"gmtModify\",\"operator\",\"operatorName\"]"
        + "id 主键ID"
        + "billId 结算单id"
        + "billNum 票据号码"
        + "billCode 票据代码"
        + "billType 票据类型"
        + "amount 发票金额"
        + "billDate 收票时间"
        + "extend1 扩展字段1"
        + "extend2 扩展字段2"
        + "extend3 扩展字段3"
        + "deleted 是否删除:0未删除，1已删除"
        + "tenantId 租户id"
        + "gmtCreate 创建时间"
        + "creator 创建人ID"
        + "creatorName 创建人姓名"
        + "gmtModify 操作时间"
        + "operator 操作人ID"
        + "operatorName 操作人姓名")
    private List<String> fields;


}
