package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.wishare.contract.domains.consts.contractset.ContractInvoiceDetailFieldConst;
/**
* <p>
* 合同开票明细表 分页请求参数
* </p>
*
* @author ljx
* @since 2022-09-26
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_invoice_detail", description = "合同开票明细表")
public class ContractInvoiceDetailPageF {

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
    private Boolean invoiceStatus;
    @ApiModelProperty("票据号码")
    private String invoiceNumber;
    @ApiModelProperty("审核状态  0通过  1审核中  2未通过")
    private Boolean auditStatus;
    @ApiModelProperty("审批编号")
    private String auditNumber;
    @ApiModelProperty("财务状态  0已确认  1未确认")
    private Boolean confirmStatus;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Boolean deleted;
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
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"collectionPlanId\",\"invoiceApplyNumber\",\"invoiceApplyAmount\",\"invoiceApplyTime\",\"remark\",\"userId\",\"userName\",\"invoiceTax\",\"invoiceStatus\",\"invoiceNumber\",\"auditStatus\",\"auditNumber\",\"confirmStatus\",\"tenantId\",\"deleted\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\"]"
        + "id id"
        + "contractId 合同Id"
        + "collectionPlanId 收款计划id"
        + "invoiceApplyNumber 开票申请编号"
        + "invoiceApplyAmount 申请开票金额"
        + "invoiceApplyTime 申请开票时间"
        + "remark 开票说明"
        + "userId 开票人id"
        + "userName 开票人姓名"
        + "invoiceTax 开票税额"
        + "invoiceStatus 开票状态  0成功  1开票中  2失败"
        + "invoiceNumber 票据号码"
        + "auditStatus 审核状态  0通过  1审核中  2未通过"
        + "auditNumber 审批编号"
        + "confirmStatus 财务状态  0已确认  1未确认"
        + "tenantId 租户id"
        + "deleted 是否删除:0未删除，1已删除"
        + "creator 创建人ID"
        + "creatorName 创建人姓名"
        + "gmtCreate 创建时间"
        + "operator 操作人ID"
        + "operatorName 操作人姓名"
        + "gmtModify 操作时间")
    private List<String> fields;


}
