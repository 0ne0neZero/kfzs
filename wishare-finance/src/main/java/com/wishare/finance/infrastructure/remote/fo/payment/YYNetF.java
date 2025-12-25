package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 银联支付信息
 */
@Getter
@Setter
@ApiModel("远洋银联支付额外参数")
@Accessors(chain = true)
public class YYNetF {

    @ApiModelProperty(value = "商户号")
    private String mid;

    @ApiModelProperty(value = "终端号")
    private String tid;
}
