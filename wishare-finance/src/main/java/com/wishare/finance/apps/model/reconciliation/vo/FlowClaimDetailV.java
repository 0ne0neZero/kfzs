package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title FlowClaimDetailV
 * @date 2024.02.20  14:34
 * @description 流水认领明细数据
 */
@Setter
@Getter
@ApiModel("流水认领明细数据")
public class FlowClaimDetailV {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流水领用记录id
     */
    private Long flowClaimRecordId;

    /**
     * 关联的发票id
     */
    private Long invoiceId;

    /**
     * 关联的流水id
     */
    private Long flowId;

    /**
     * 发票金额
     */
    private Long invoiceAmount;

    /**
     * 流水金额
     */
    private Long flowAmount;

    /**
     * 状态：0正常，1已撤销
     */
    private Integer state;

    /**
     * 系统来源 1收费系统，2合同系统
     */
    private Integer sysSource;

    /**
     * 认领类型：1:发票认领;2:账单认领;
     */
    private Integer claimType;

    /**
     * 认领ID类型：1:发票;2:收款单;3:退款单;
     */
    private Integer claimIdType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 修改人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;
}
