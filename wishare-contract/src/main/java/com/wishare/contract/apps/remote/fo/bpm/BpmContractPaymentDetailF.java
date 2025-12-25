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
public class BpmContractPaymentDetailF {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("申请付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("申请付款时间")
    private LocalDateTime applyPaymentTime;
    @ApiModelProperty("实际付款时间")
    private LocalDateTime actualPaymentTime;
    @ApiModelProperty("付款申请编码")
    private String paymentApplyNumber;
    @ApiModelProperty("付款申请人id")
    private String userId;
    @ApiModelProperty("付款申请人姓名")
    private String userName;
    @ApiModelProperty("请款说明")
    private String remark;
    @ApiModelProperty("付款类型  0有票付款  1无票付款")
    private Integer paymentType;
    @ApiModelProperty("付款方式  0现金  1银行转帐  2汇款  3支票")
    private Integer paymentMethod;
    @ApiModelProperty("发票文件集")
    private String invoiceUrl;
    @ApiModelProperty("票据号码集")
    private String invoiceCode;
    @ApiModelProperty("审核状态  0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("审批编号")
    private String auditCode;
    @ApiModelProperty("付款状态  0已付款  1未付款")
    private Integer paymentStatus;
    @ApiModelProperty("财务状态  0已确认  1未确认")
    private Integer confirmStatus;
    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;
    @ApiModelProperty("创建人id")
    private String creator;
    @ApiModelProperty("创建人姓名")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    private LocalDateTime gmtModify;
}
