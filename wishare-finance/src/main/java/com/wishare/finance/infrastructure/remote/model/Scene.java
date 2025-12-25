package com.wishare.finance.infrastructure.remote.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 支付场景信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
public class Scene {

    /**
     * 支付客户端ip
     */
    private String clientIp;
    /**
     * 设备id
     */
    private String deviceId;

}
