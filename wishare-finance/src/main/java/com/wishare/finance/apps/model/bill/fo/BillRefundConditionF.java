package com.wishare.finance.apps.model.bill.fo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author ℳ๓采韵
 * @project wishare-charge
 * @title BillRefundF
 * @date 2024.08.15  15:14
 * @description
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("退款查询入参")
public class BillRefundConditionF {

    @ApiModelProperty("账单列表")
    private List<Long> billIds;

    @ApiModelProperty("流程id")
    private String procInstId;

}
