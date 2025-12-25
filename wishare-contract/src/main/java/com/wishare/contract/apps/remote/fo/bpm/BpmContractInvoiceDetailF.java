package com.wishare.contract.apps.remote.fo.bpm;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 收款计划
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpmContractInvoiceDetailF {

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
    private LocalDateTime gmtCreate;

}
