package com.wishare.contract.apps.fo.revision.pay.settdetails;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
* <p>
* 支出合同-款项表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同结算单明细-款项表更新请求参数", description = "支出合同结算单明细-款项表")
public class ContractPaySettDetailsUpdateF {

    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;

    @ApiModelProperty("合同清单ID")
    private String payFundId;

    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 50 个字符",max = 50)
    private String name;
    @ApiModelProperty(value = "关联合同ID", required = true)
    private String settlementId;

    @ApiModelProperty("款项类型ID")
    @Length(message = "款项类型ID不可超过 40 个字符",max = 40)
    private String typeId;

    @ApiModelProperty("款项类型")
    @Length(message = "款项类型不可超过 50 个字符",max = 50)
    private String type;

    @ApiModelProperty("结算金额")
    @Digits(integer = 10,fraction =2,message = "结算金额不正确")
    private BigDecimal amount;

    @ApiModelProperty("扣款金额")
    @Digits(integer = 10,fraction =2,message = "扣款金额不正确")
    private BigDecimal deductionAmount;

    @ApiModelProperty("费项ID")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;

    @ApiModelProperty("税率ID")
    private String taxRateId;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("付费类型ID")
    @Length(message = "付费类型ID不可超过 30 个字符",max = 30)
    private String payTypeId;

    @ApiModelProperty("付费类型")
    @Length(message = "付费类型不可超过 50 个字符",max = 50)
    private String payType;

    @ApiModelProperty("付费方式ID")
    @Length(message = "付费方式ID不可超过 30 个字符",max = 30)
    private String payWayId;

    @ApiModelProperty("付费方式")
    @Length(message = "付费方式不可超过 50 个字符",max = 50)
    private String payWay;

    @ApiModelProperty("收费标准ID")
    @Length(message = "收费标准ID不可超过 30 个字符",max = 30)
    private String standardId;

    @ApiModelProperty("收费标准")
    @Length(message = "收费标准不可超过 50 个字符",max = 50)
    private String standard;

    @ApiModelProperty("收费标准金额")
    @Digits(integer = 10,fraction =2,message = "收费标准金额不正确")
    private BigDecimal standAmount;

    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 255 个字符",max = 255)
    private String remark;

    @ApiModelProperty("税额")
    @Digits(integer = 10,fraction =6,message = "税额不正确")
    private BigDecimal taxRateAmount;

    @ApiModelProperty("不含税金额")
    @Digits(integer = 10,fraction =6,message = "不含税金额不正确")
    private BigDecimal amountWithOutRate;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("数量")
    private BigDecimal amountNum;
}
