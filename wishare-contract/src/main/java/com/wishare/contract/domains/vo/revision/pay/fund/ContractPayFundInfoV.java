package com.wishare.contract.domains.vo.revision.pay.fund;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* 支出合同-款项表视图对象
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同-款项表信息对象", description = "支出合同-款项表视图对象")
public class ContractPayFundInfoV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 款项名称
    */
    @ApiModelProperty("款项名称")
    private String name;

    @ApiModelProperty("关联合同ID")
    private String contractId;

    /**
    * 款项类型
    */
    @ApiModelProperty("款项类型")
    private String type;
    /**
    * 金额
    */
    @ApiModelProperty("金额")
    private BigDecimal amount;

    /**
     * 变更金额
     */
    @ApiModelProperty("变更金额")
    private BigDecimal changeAmount;

    /**
     * 变更金额
     */
    @ApiModelProperty("变更金额字符串")
    private String changeAmountString;

    /**
    * 费项
    */
    @ApiModelProperty("费项")
    private String chargeItem;


    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private String taxRate;

    /**
    * 付费类型
    */
    @ApiModelProperty("付费类型")
    private String payType;

    /**
    * 付费方式
    */
    @ApiModelProperty("付费方式")
    private String payWay;
    /**
    * 收费标准
    */
    @ApiModelProperty("收费标准")
    private String standard;

    /**
     * 变更收费标准
     */
    @ApiModelProperty("变更收费标准")
    private String changeStandard;

    /**
     * 收费标准展示
     */
    @ApiModelProperty("收费标准展示")
    private String standardShow;
    /**
     * 收费标准金额
     */
    @ApiModelProperty("收费标准金额")
    @Digits(integer = 10,fraction =2,message = "收费标准金额不正确")
    private BigDecimal standAmount;

    /**
     * 变更收费标准金额
     */
    @ApiModelProperty("变更收费标准金额")
    private BigDecimal changeStandAmount;

    /**
    * 备注
    */
    @ApiModelProperty("备注")
    private String remark;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("税额")
    @Digits(integer = 10,fraction =6,message = "税额不正确")
    private BigDecimal taxRateAmount;

    @ApiModelProperty("不含税金额")
    @Digits(integer = 10,fraction =6,message = "不含税金额不正确")
    private BigDecimal amountWithOutRate;

    @ApiModelProperty("数量")
    private BigDecimal num;

    @ApiModelProperty("变更数量")
    private BigDecimal changeNum;

    @ApiModelProperty("本期金额")
    private BigDecimal thisAmount;

    @ApiModelProperty("本期数量")
    private BigDecimal thisNum;

    @ApiModelProperty("至上期末统计金额")
    private BigDecimal fromThisAmount;

    @ApiModelProperty("至上期末统计金额字符串")
    private String fromThisAmountString;

    @ApiModelProperty("至上期末统计数量")
    private BigDecimal fromThisNum;

    @ApiModelProperty("至上期末统计数量字符串")
    private String fromThisNumString;

    @ApiModelProperty("至本期末统计金额")
    private BigDecimal toThisAmount;

    @ApiModelProperty("至本期末统计数量")
    private BigDecimal toThisNum;



}
