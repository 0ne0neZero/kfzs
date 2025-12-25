package com.wishare.contract.domains.dto.revision.income.fund;

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

/**
* <p>
* 收入合同-款项表
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_income_fund请求对象", description = "收入合同-款项表")
public class ContractIncomeFundD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("关联合同ID")
    private String contractId;
    @ApiModelProperty("款项名称")
    private String name;
    @ApiModelProperty("款项类型ID")
    private String typeId;
    @ApiModelProperty("款项类型")
    private String type;
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("税率ID")
    private String taxRateId;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("付费类型ID")
    private String payTypeId;
    @ApiModelProperty("付费类型")
    private String payType;
    @ApiModelProperty("付费方式ID")
    private String payWayId;
    @ApiModelProperty("付费方式")
    private String payWay;
    @ApiModelProperty("开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty("结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty("收费标准ID")
    private String standardId;
    @ApiModelProperty("收费标准")
    private String standard;
    @ApiModelProperty("备注")
    private String remark;
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
