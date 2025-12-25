package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("数据推送结果查询")
public class OrderDealResult {
    @ApiModelProperty("响应数据")
    private String data;
}
