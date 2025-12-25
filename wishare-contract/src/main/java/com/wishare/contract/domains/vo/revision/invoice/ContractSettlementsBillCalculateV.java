package com.wishare.contract.domains.vo.revision.invoice;

import java.math.BigDecimal;
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
/**
* <p>
* 结算单计量明细表视图对象
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "结算单计量明细表视图对象", description = "结算单计量明细表视图对象")
public class ContractSettlementsBillCalculateV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联票据ID
    */
    @ApiModelProperty("关联票据ID")
    private String billId;
    /**
    * 款项类型ID
    */
    @ApiModelProperty("款项类型ID")
    private String typeId;
    /**
    * 款项类型名称
    */
    @ApiModelProperty("款项类型名称")
    private String type;
    /**
    * 结算金额
    */
    @ApiModelProperty("结算金额")
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
    * 税额
    */
    @ApiModelProperty("税额")
    private BigDecimal taxRateAmount;
    /**
    * 不含税金额
    */
    @ApiModelProperty("不含税金额")
    private BigDecimal amountWithOutRate;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
