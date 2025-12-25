package com.wishare.contract.domains.vo.revision.pay.fund;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.Digits;

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
@ApiModel(value = "支出合同-款项表视图对象", description = "支出合同-款项表视图对象")
public class ContractPayFundV {

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
    * 款项类型ID
    */
    @ApiModelProperty("款项类型ID")
    private String typeId;
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
    * 费项ID
    */
    @ApiModelProperty("费项ID")
    private Long chargeItemId;
    /**
    * 费项
    */
    @ApiModelProperty("费项")
    private String chargeItem;
    @ApiModelProperty("费项全路径")
    private String chargeItemAllPath;
    /**
    * 税率ID
    */
    @ApiModelProperty("税率ID")
    private String taxRateId;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private String taxRate;
    /**
    * 付费类型ID
    */
    @ApiModelProperty("付费类型ID")
    private String payTypeId;
    /**
    * 付费类型
    */
    @ApiModelProperty("付费类型")
    private String payType;
    /**
    * 付费方式ID
    */
    @ApiModelProperty("付费方式ID")
    private String payWayId;
    /**
    * 付费方式
    */
    @ApiModelProperty("付费方式")
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
    private String standardId;
    /**
    * 收费标准
    */
    @ApiModelProperty("收费标准")
    private String standard;
    /**
     * 收费标准展示
     */
    @ApiModelProperty("收费标准展示")
    private String standardShow;
    /**
     * 收费标准金额
     */
    @ApiModelProperty("收费标准金额")
    @Digits(integer = 10,fraction = 6,message = "收费标准金额不正确")
    private BigDecimal standAmount;
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
    private Integer num;

    @ApiModelProperty("数量")
    private BigDecimal amountNum;

    @ApiModelProperty("删除标识")
    private boolean listDeleted;

    @ApiModelProperty("是否主合同")
    private Integer isMain;
    @ApiModelProperty("是否锁定")
    private Boolean isLock = Boolean.FALSE;

    //是否HY（0.否；1.是）
    private Integer isHy;

    @ApiModelProperty("是否涉及成本数据:0.否；1.是")
    private Integer isCostData = 0;

    //成本-费项编码
    private String accountItemCode;
    //成本-费项名称
    private String accountItemName;
    //成本-费项全码
    private String accountItemFullCode;
    //成本-费项全称
    private String accountItemFullName;
    //成本-合约规划可用金额
    private BigDecimal summarySurplusAmount;
    //成本-分摊明细ID
    private String cbApportionId;
}
