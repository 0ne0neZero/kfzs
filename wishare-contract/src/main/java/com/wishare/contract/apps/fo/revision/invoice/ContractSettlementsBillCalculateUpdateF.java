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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 结算单计量明细表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单计量明细表更新请求参数", description = "结算单计量明细表")
public class ContractSettlementsBillCalculateUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;
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

}
