package com.wishare.finance.infrastructure.remote.vo.payment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付渠道配置实体
 *
 * @Author zyj
 * @Date 2023/07/05
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("支付渠道配置实体")
public class MchInfo {

    @ApiModelProperty(value = "商户号")
    private String orgId;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethodKey;

}
