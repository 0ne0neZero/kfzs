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
* 保证金计划付/退款明细
* </p>
*
* @author ljx
* @since 2022-10-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bond_payment_detail", description = "保证金计划付/退款明细")
public class ContractBondPaymentDetailF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("保证金计划id")
    private Long bondPlanId;
    @ApiModelProperty("申请付/退款金额（元）")
    private BigDecimal paymentAmount;
    @ApiModelProperty("付/退款方式  0现金  1银行转帐  2汇款  3支票")
    private Integer paymentMethod;
    @ApiModelProperty("关联审批编号")
    private String auditCode;
    @ApiModelProperty("审批状态 0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("财务状态  0已确认  1未确认")
    private Integer confirmStatus;
    @ApiModelProperty("申请付/退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyPaymentTime;
    @ApiModelProperty("实际付/退款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualPaymentTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否删除")
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
    @ApiModelProperty("付/退类型  1 付 2退")
    private Integer type;
    @ApiModelProperty("付/退申请编号")
    private String paymentNumber;
}
