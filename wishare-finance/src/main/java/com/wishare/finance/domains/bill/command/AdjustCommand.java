package com.wishare.finance.domains.bill.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 调整命令
 *
 * @author yancao
 */
@Getter
@Setter
public class AdjustCommand {

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 项目id
     */
    private String supCpUnitId;

    /**
     * 调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他
     */
    private Integer adjustWay;

    /**
     * 调整内容
     */
    private String content;

    /**
     * 调整原因 1：物业服务终止、失联，2：破产或房屋拍卖、易主房屋，3: 销售承诺、房屋质量问题,4: 服务质量瑕疵,5: 法院判决类、属地法规规定的空置减免类, 99: 其他
     */
    private Integer reason;

    /**
     * 调整金额
     */
    private Long adjustAmount;

    /**
     * 调整类型： 1减免，2调高，3调低
     */
    private Integer adjustType;

    /**
     * 调整比例，区间[0.01, 100]
     */
    private BigDecimal adjustRatio;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 收费对象ID
     */
    private String payerId;

    /**
     * 收费对象名称
     */
    private String payerName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 计费面积 (单位：m²)
     */
    private BigDecimal chargingArea;

    /**
     * 减免凭证号
     */
    private String voucher;

    /**
     * 已审核已结算账单调低和减免审核后操作：1退款，2转预收
     */
    private Integer approvedAction;

    /**
     * 转预收的费项id
     */
    private Long chargeItemId;

    /**
     * 转预收的费项名称
     */
    private String chargeItemName;
}
