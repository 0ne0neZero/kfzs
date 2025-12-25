package com.wishare.contract.apps.fo.contractset;


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
* 合同付款明细表 保存请求参数
* </p>
*
* @author ljx
* @since 2022-09-29
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_payment_detail", description = "合同付款明细表")
public class ContractPaymentDetailSaveF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;
    @ApiModelProperty("申请付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("申请付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyPaymentTime;
    @ApiModelProperty("实际付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualPaymentTime;
    @ApiModelProperty("付款申请人id")
    private String userId;
    @ApiModelProperty("付款申请人姓名")
    private String userName;
    @ApiModelProperty("请款说明")
    private String remark;
    @ApiModelProperty("付款类型  0有票付款  1无票付款")
    private Integer paymentType;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人id")
    private String operator;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
