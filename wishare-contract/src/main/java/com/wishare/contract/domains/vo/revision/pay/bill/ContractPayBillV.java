package com.wishare.contract.domains.vo.revision.pay.bill;

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
import java.time.LocalDateTime;

/**
* <p>
* 视图对象
* </p>
*
* @author chenglong
* @since 2023-06-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "票据视图对象", description = "票据视图对象")
public class ContractPayBillV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;

    /**
     * 合同ID
     */
    @ApiModelProperty("合同ID")
    private String contractId;

    /**
    * 票据号码
    */
    @ApiModelProperty("发票号码")
    private String billNum;
    /**
    * 票据代码
    */
    @ApiModelProperty("发票代码")
    private String billCode;
    /**
     * 发票类型
     */
    @ApiModelProperty("发票类型")
    private String billType;
    /**
     * 发票类型名称
     */
    @ApiModelProperty("发票类型名称")
    private String billTypeName;

    /**
     * 发票状态
     */
    @ApiModelProperty("发票状态")
    private String invoiceStatus;

    /**
     * 发票状态名称
     */
    @ApiModelProperty("发票状态名称")
    private String invoiceStatusName;

    /**
    * 票据性质
    */
    @ApiModelProperty("票据性质")
    private String type;
    /**
    * 收票金额
    */
    @ApiModelProperty("收票金额")
    private BigDecimal amount;
    /**
    * 抬头
    */
    @ApiModelProperty("抬头")
    private String title;

    /**
    * 收票时间
    */
    @ApiModelProperty("收票时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime collectTime;
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
