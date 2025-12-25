package com.wishare.contract.domains.dto.revision.invoice;

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
* 收票明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_settlements_bill_details请求对象", description = "收票明细表")
public class ContractSettlementsBillDetailsD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("结算单id")
    private String billId;
    @ApiModelProperty("票据号码")
    private String billNum;
    @ApiModelProperty("票据代码")
    private String billCode;
    @ApiModelProperty("票据类型")
    private Boolean billType;
    @ApiModelProperty("发票金额")
    private BigDecimal amount;
    @ApiModelProperty("收票时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate billDate;
    @ApiModelProperty("扩展字段1")
    private String extend1;
    @ApiModelProperty("扩展字段2")
    private String extend2;
    @ApiModelProperty("扩展字段3")
    private String extend3;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Boolean deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;

}
