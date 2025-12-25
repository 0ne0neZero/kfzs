package com.wishare.contract.apps.fo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("立项审批关联订单基础参数")
public class ContractProjectOrderF {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("关联的立项ID")
    private String projectInitiationId;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("采购平台 0 线下采购、1 京东慧采、2 中交云采")
    private Integer platform;

    @ApiModelProperty("订单金额（不含税）")
    private BigDecimal orderAmountWithoutTax;

    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderCreateTime;

    @ApiModelProperty("下单人")
    private String orderAccount;

    @ApiModelProperty(value = "订单状态 0 待发货 1 已签收 2 已取消 3 已退货")
    private Integer orderStatus;

    @ApiModelProperty("审核状态 -1 未提审 0 待审核、1 未通过、2 已通过、3 已驳回")
    private Integer bpmReviewStatus;

    @ApiModelProperty(value = "租户id")
    private String tenantId;
}