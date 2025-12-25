package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 支付场景信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("支付场景信息")
public class SceneF {

    @ApiModelProperty(value = "支付客户端ip")
    @NotNull(message = "支付客户端ip不能为空")
    @Length(max = 20, message = "支付客户端ip格式不正确")
    private String clientIp;

    @ApiModelProperty(value = "设备id")
    @Length(max = 64, message = "设备id格式不正确")
    private String deviceId;

}
