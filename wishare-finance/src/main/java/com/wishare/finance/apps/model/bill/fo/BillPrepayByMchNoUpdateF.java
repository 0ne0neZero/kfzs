package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayByIdUpdateF
 * @date 2023.11.10  10:27
 * @description 根据ID更新预支付信息传参
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("根据ID更新预支付信息状态")
public class BillPrepayByMchNoUpdateF {

    @NotBlank(message = "商户订单号")
    @ApiModelProperty(value = "商户订单号")
    private String mchOrderNo;

    @ApiModelProperty("上级收费单元ID")
    @NotNull(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("支付状态")
    private Integer payStatus;
}
