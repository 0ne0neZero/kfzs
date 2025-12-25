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
* 保证金结转明细表
* </p>
*
* @author ljx
* @since 2022-11-21
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_bond_carryover_detail请求对象", description = "保证金结转明细表")
public class ContractBondCarryoverDetailD {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("保证金计划id")
    private Long bondPlanId;
    @ApiModelProperty("关联投标保证金账单编号")
    private String bidBondBillNo;
    @ApiModelProperty("投标保证金金额")
    private BigDecimal bidBondAmount;
    @ApiModelProperty("申请结转时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime carryoverTime;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("审批状态 0通过  1审核中  2未通过")
    private Integer auditStatus;
    @ApiModelProperty("关联审批编号")
    private String auditCode;
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
