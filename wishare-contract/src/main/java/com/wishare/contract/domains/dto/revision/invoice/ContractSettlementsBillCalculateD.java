package com.wishare.contract.domains.dto.revision.invoice;

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
* 结算单计量明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_settlements_bill_calculate请求对象", description = "结算单计量明细表")
public class ContractSettlementsBillCalculateD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("关联票据ID")
    private String billId;
    @ApiModelProperty("款项类型ID")
    private String typeId;
    @ApiModelProperty("款项类型名称")
    private String type;
    @ApiModelProperty("结算金额")
    private BigDecimal amount;
    @ApiModelProperty("费项ID")
    private Long chargeItemId;
    @ApiModelProperty("费项")
    private String chargeItem;
    @ApiModelProperty("税率ID")
    private String taxRateId;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("税额")
    private BigDecimal taxRateAmount;
    @ApiModelProperty("不含税金额")
    private BigDecimal amountWithOutRate;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
