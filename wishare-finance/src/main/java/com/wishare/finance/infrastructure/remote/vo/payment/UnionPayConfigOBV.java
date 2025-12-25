package com.wishare.finance.infrastructure.remote.vo.payment;

import lombok.Getter;
import lombok.Setter;

/**
 * 银联支付配置信息
 *
 * @Author dxclay
 * @Date 2022/11/22
 * @Version 1.0
 */
@Getter
@Setter
public class UnionPayConfigOBV  {

    /**
     * 应用id
     */
    private String appId;

    /**
     * 产品秘钥
     */
    private String appKey;

    /**
     * 机构商户号
     */
    private String instMid;

    /**
     * 来源编号
     */
    private String msgSrcId;

    /**
     * 通讯秘钥
     */
    private String privateKey;

}
