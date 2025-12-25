package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单导入命令
 * @author yancao
 */
@Getter
@Setter
public class UpdateBillCommand<B> {

    /**
     * 账单实体
     */
    private B bill;

    /**
     * 结算金额
     */
    private Long settleAmount;

    /**
     * 结算方式(0线上，1线下)
     */
    private Integer settleWay;

    /**
     * 账单已缴时间
     */
    private LocalDateTime chargeTime;

    /**
     * 收费开始时间
     */
    private LocalDateTime chargeStartTime;

    /**
     * 收费结束时间
     */
    private LocalDateTime chargeEndTime;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;

    /**
     * 是否已审核 true已审核，false待审核
     */
    private Boolean approvedFlag;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("收费组织id")
    private String cpOrgId;

    @ApiModelProperty("收费组织名称")
    private String cpOrgName;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty("收费单元id")
    private String cpUnitId;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("付款方手机号")
    private String payerPhone;

    @ApiModelProperty("收款方手机号")
    private String payeePhone;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;
}
