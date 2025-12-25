package com.wishare.contract.domains.dto.contractset;

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
* 付款计划收票明细
* </p>
*
* @author ljx
* @since 2022-11-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_receive_invoice_detail请求对象", description = "付款计划收票明细")
public class ContractReceiveInvoiceDetailD {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("付款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("发票类型  3增值税电子发票")
    private Integer invoiceType;
    @ApiModelProperty("票据url")
    private String invocieUrl;
    @ApiModelProperty("票据号码")
    private String invoiceNumber;
    @ApiModelProperty("收票时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceTime;
    @ApiModelProperty("关联审批")
    private String auditCode;
    @ApiModelProperty("审核状态  0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;
    @ApiModelProperty("创建人ID")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

}
