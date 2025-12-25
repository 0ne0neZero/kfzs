package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "导入应收账单请求信息", parent = AddReceivableBillF.class)
public class ImportReceivableBillF extends AddReceivableBillF{

    @ApiModelProperty(value = "结算金额")
    private Long settleAmount;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty(value = "账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty(value = "行号标识", required = true)
    private Integer index;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue = 0;

    @ApiModelProperty(value = "关联账单id")
    private Long refBillId;

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空!")
    private String supCpUnitId;
}
