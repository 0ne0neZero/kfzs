package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoQueryF
 * @date 2023.11.08  10:39
 * @description 账单预支付信息查询入参
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("账单预支付信息查询入参")
public class BillPrepayInfoQueryF {

    @ApiModelProperty("账单id列表")
    @Size(max = 1000, min = 1, message = "账单id列表大小不正确，大小区间为[1,1000]")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("支付状态不能为空")
    private Integer payState;

    @ApiModelProperty("远洋预支付id/其他环境商户订单号")
    private String mchOrderNo;
}
