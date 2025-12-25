package com.wishare.contract.domains.entity.revision.projectInitiation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.contract.domains.entity.revision.BaseE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 立项关联订单实体类
 */
@Data
@Accessors(chain = true)
@TableName("contract_project_order")
@EqualsAndHashCode(callSuper = true)
public class ContractProjectOrderE extends BaseE {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 关联的立项ID
     */
    private String projectInitiationId;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 采购平台 0 线下采购、1 京东慧采、2 中交云采
     */
    private Integer platform;

    /**
     * 订单金额（含税）
     */
    private BigDecimal orderAmount;

    /**
     * 订单金额（不含税）
     */
    private BigDecimal orderAmountWithoutTax;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品信息明细
     */
    private String goodsInfo;

    /**
     * 下单时间
     */
    private LocalDateTime orderCreateTime;

    /**
     * 下单人
     */
    private String orderAccount;

    /**
     * 订单状态 0 待发货 1 已签收 2 已取消 3 已退货
     */
    private Integer orderStatus;

    /**
     * 审核状态 -1 未提审 0 待审核、1 未通过、2 已通过、3 已驳回
     */
    private Integer bpmReviewStatus;

    /**
     * 慧采下单审核流程id
     */
    private String bpmProcInstId;

    /**
     * 京东慧采采购账号
     */
    private String jdHuiCaiUserName;

    /**
     * 京东慧采采购账号密码
     */
    private String jdHuiCaiPwdMd5;

    /**
     * 租户id
     */
    private String tenantId;

}