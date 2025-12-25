package com.wishare.finance.infrastructure.remote.vo.contract;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* 收票明细表视图对象
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收票明细表视图对象", description = "收票明细表视图对象")
public class ContractSettlementsBillDetailsV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 结算单id
    */
    @ApiModelProperty("结算单id")
    private String billId;
    /**
    * 票据号码
    */
    @ApiModelProperty("票据号码")
    private String billNum;
    /**
    * 票据代码
    */
    @ApiModelProperty("票据代码")
    private String billCode;
    /**
    * 票据类型
    */
    @ApiModelProperty("票据类型")
    private Integer billType;
    /**
     * 票据类型名称
    */
    @ApiModelProperty("票据类型名称")
    private String billTypeName;
    /**
     * 审批状态
     */
    @ApiModelProperty("审批状态")
    private Integer reviewStatus;
    /**
     * 审批状态名称
     */
    @ApiModelProperty("审批状态名称")
    private String reviewStatusName;
    /**
    * 发票金额
    */
    @ApiModelProperty("发票金额")
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
    private String extend1;
    /**
    * 扩展字段2
    */
    @ApiModelProperty("扩展字段2")
    private String extend2;
    /**
    * 扩展字段3
    */
    @ApiModelProperty("扩展字段3")
    private String extend3;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 创建人ID
    */
    @ApiModelProperty("创建人ID")
    private String creator;
    /**
    * 创建人姓名
    */
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
    * 操作人ID
    */
    @ApiModelProperty("操作人ID")
    private String operator;
    /**
    * 操作人姓名
    */
    @ApiModelProperty("操作人姓名")
    private String operatorName;

}
