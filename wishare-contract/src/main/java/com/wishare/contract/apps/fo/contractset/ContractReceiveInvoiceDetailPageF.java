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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
* <p>
* 付款计划收票明细 分页请求参数
* </p>
*
* @author ljx
* @since 2022-11-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_receive_invoice_detail", description = "付款计划收票明细")
public class ContractReceiveInvoiceDetailPageF {

    @ApiModelProperty("id")
    @NotNull(message = "id不可为空")
    private Long id;
    @ApiModelProperty("合同Id")
    @NotNull(message = "合同Id不可为空")
    private Long contractId;
    @ApiModelProperty("付款计划id")
    @NotNull(message = "付款计划id不可为空")
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
    @ApiModelProperty("需要查询返回的字段，不传时返回以下全部，可选字段列表如下"
        + "[\"id\",\"contractId\",\"collectionPlanId\",\"invoiceAmount\",\"invoiceType\",\"invocieUrl\",\"invoiceNumber\",\"invoiceTime\",\"auditCode\",\"auditStatus\",\"remark\",\"deleted\",\"creator\",\"creatorName\",\"gmtCreate\"]"
        + "id id"
        + "contractId 合同Id"
        + "collectionPlanId 付款计划id"
        + "invoiceAmount 收票金额"
        + "invoiceType 发票类型  1增值税电子发票"
        + "invocieUrl 票据url"
        + "invoiceNumber 票据号码"
        + "invoiceTime 收票时间"
        + "auditCode 关联审批"
        + "auditStatus 审核状态  0通过  1审核中  2未通过"
        + "remark 备注"
        + "deleted 是否删除:0未删除，1已删除"
        + "creator 创建人ID"
        + "creatorName 创建人姓名"
        + "gmtCreate 创建时间")
    private List<String> fields;


}
