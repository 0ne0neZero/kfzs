package com.wishare.contract.domains.dto.revision.bond.pay;

import java.math.BigDecimal;
import java.time.LocalDate;
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
* 缴纳保证金改版关联单据明细表
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "pay_bond_relation_bill请求对象", description = "缴纳保证金改版关联单据明细表")
public class PayBondRelationBillD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("保证金计划ID")
    private String bondId;
    @ApiModelProperty("编号")
    private String code;
    @ApiModelProperty("业务类型编码（收款，收据，结转，退款）")
    private String typeCode;
    @ApiModelProperty("业务类型名称（收款，收据，结转，退款）")
    private String type;
    @ApiModelProperty("金额（收款，收据，结转，退款）")
    private BigDecimal amount;
    @ApiModelProperty("交易方式编码（现金，微信，支付宝，网上转账等）")
    private String dealWayCode;
    @ApiModelProperty("交易方式名称（现金，微信，支付宝，网上转账等）")
    private String dealWay;
    @ApiModelProperty("业务费项ID")
    private String chargeItemId;
    @ApiModelProperty("业务费项名称")
    private String chargeItem;
    @ApiModelProperty("（收款，收据，结转，退款）交易日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate dealDate;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("剩余金额")
    private BigDecimal residueAmount;
    @ApiModelProperty("审批流id")
    private Long procId;
    @ApiModelProperty("状态（0 待提交  1审批中  2 已拒绝  3已通过  4已完成）")
    private Integer status;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
