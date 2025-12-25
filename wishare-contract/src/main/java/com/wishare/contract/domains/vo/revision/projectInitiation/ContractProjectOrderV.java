package com.wishare.contract.domains.vo.revision.projectInitiation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "立项关联订单")
public class ContractProjectOrderV {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("关联的立项ID")
    private String projectInitiationId;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("采购平台 0 线下采购、1 京东慧采、2 中交云采")
    private Integer platform;

    @ApiModelProperty("订单金额（含税）")
    private BigDecimal orderAmount;

    @ApiModelProperty("订单金额（不含税）")
    private BigDecimal orderAmountWithoutTax;

    @ApiModelProperty("商品数量")
    private Integer goodsCount;

    @ApiModelProperty("商品数量")
    private List<ContractProjectOrderInfoV> goodsInfoList;

    @ApiModelProperty("下单时间")
    private LocalDateTime orderCreateTime;

    @ApiModelProperty("下单人")
    private String orderAccount;

    @ApiModelProperty(value = "订单状态 0 待发货 1 已签收 2 已取消 3 已退货")
    private Integer orderStatus;

    @ApiModelProperty("审核状态 -1 未提审 0 待审核、1 未通过、2 已通过、3 已驳回")
    private Integer bpmReviewStatus;

    @ApiModelProperty(value = "成本确认变更审核流程id")
    private String bpmProcInstId;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "更新人")
    private String operator;

    @ApiModelProperty(value = "更新人")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

}