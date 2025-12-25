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
 * @title BillPrepayInfoF
 * @date 2023.11.08  17:15
 * @description 账单预支付信息入参
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("账单预支付信息入参")
public class BillPrepayInfoF {

    @ApiModelProperty("账单id列表")
    @Size(max = 1000, min = 1, message = "账单id列表大小不正确，大小区间为[1,1000]")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元ID")
    @NotBlank(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

    @ApiModelProperty("账单类型")
    private Integer billType;
}
