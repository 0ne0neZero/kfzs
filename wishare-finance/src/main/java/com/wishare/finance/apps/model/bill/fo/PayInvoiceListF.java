package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
* @description:查询开票收款单列表
* @author: zhenghui
* @date: 2023/3/27 22:05
*/
@Getter
@Setter
@ApiModel("查询开票收款单列表入参")
public class PayInvoiceListF extends BasePageF{

    @ApiModelProperty(value = "项目ID")
    @NotBlank(message = "项目ID不能为空")
    private String communityId;

    @ApiModelProperty(value = "项目集合ID")
    private List<String> communityIds;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号集合")
    private List<String> roomIds;

    @ApiModelProperty(value = "收费对象id")
    private String targetObjId;

    @ApiModelProperty(value = "收费对象id")
    private List<String> targetObjIds;

    @ApiModelProperty(value = "结算渠道 ALIPAY：支付宝， WECHATPAY:微信支付， CASH:现金， POS: POS机， UNIONPAY:银联， SWIPE: 刷卡， BANK:银行汇款， CARRYOVER:结转， CHEQUE: 支票 OTHER: 其他")
    private List<String> payChannels;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private List<Integer> invoiceStates;

    @ApiModelProperty(value = "收款开始时间")
    private LocalDateTime payStartTime;

    @ApiModelProperty(value = "收款结束时间")
    private LocalDateTime payEndTime;

}
