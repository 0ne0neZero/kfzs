package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
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
* 合同开票明细表
* </p>
*
* @author ljx
* @since 2022-09-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_invoice_detail请求对象", description = "合同开票明细表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractInvoiceDetailV {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("开票申请编号")
    private String invoiceApplyNumber;
    @ApiModelProperty("申请开票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("申请开票时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceApplyTime;
    @ApiModelProperty("开票说明")
    private String remark;
    @ApiModelProperty("开票人id")
    private String userId;
    @ApiModelProperty("开票人姓名")
    private String userName;
    @ApiModelProperty("开票税额")
    private BigDecimal invoiceTax;
    @ApiModelProperty("开票状态  0成功  1开票中  2失败")
    private Integer invoiceStatus;
    @ApiModelProperty("票据号码")
    private String invoiceNumber;
    @ApiModelProperty("审核状态  0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("审批编号")
    private String auditNumber;
    @ApiModelProperty("财务状态  0已确认  1未确认")
    private Integer confirmStatus;
    @ApiModelProperty("租户id")
    private String tenantId;
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
    @ApiModelProperty("操作人ID")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("货品名称")
    private String productName;
    @ApiModelProperty("发票类型  1 增值类普通发票  2  增值类专用发票  3其他发票")
    private String billType;
    @ApiModelProperty("中台发票id")
    private Long invoiceId;
    @ApiModelProperty("发票url（诺诺）")
    private String invocieUrl;
    @ApiModelProperty("开票失败原因")
    private String failReason;
}
