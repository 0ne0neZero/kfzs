package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("应收账单结算入参")
public class AddBillSettleRf {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）",required = true)
    @NotNull(message = "收费对象类型不能为空")
    private Integer payerType;

    @ApiModelProperty("收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("收款人id")
    private String payeeId;

    @ApiModelProperty("收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款方id (结算的收费对象为账单的付款方)",required = true)
    @NotBlank(message = "付款方id不能为空")
    private String payerId;

    @ApiModelProperty(value = "付款方名称",required = true)
    @NotBlank(message = "付款方名称不能为空")
    private String payerName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）",required = true)
    @NotBlank(message = "结算渠道不能为空")
    private String settleChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)",required = true)
    @NotNull(message = "结算方式不能为空")
    private Integer settleWay;

    @ApiModelProperty("外部结算编号（支付宝单号，银行流水号等）")
    private String outSettleNo;

    @ApiModelProperty("结转账单id")
    private Long carriedBillId;

    @ApiModelProperty("结转账单编号")
    private Long carriedBillNo;

    @ApiModelProperty(value = "结算金额（单位：分）",required = true)
    @NotNull(message = "结算金额不能为空")
    private Long settleAmount;

    @ApiModelProperty(value = "收款金额（单位：分）(合单支付时，收款金额 > 结算金额)",required = true)
    @NotNull(message = "收款金额不能为空")
    private Long payAmount;

    @ApiModelProperty("减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty("结转金额（单位：分）")
    private Long carriedAmount;

    @ApiModelProperty("减免说明列表\n" +
            "[{\n" +
            " \"outDiscountId\": \"外部减免id\",\n" +
            " \"discountType\": \"减免类型\",\n" +
            " \"amount\": 100, //减免金额（分）\n" +
            " \"discription\": \"减免说明\" \n" +
            "}]")
    private String discounts;

    @ApiModelProperty("结算时间")
    private LocalDateTime settleTime;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;
}
