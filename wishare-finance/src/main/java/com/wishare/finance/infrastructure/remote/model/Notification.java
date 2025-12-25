package com.wishare.finance.infrastructure.remote.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 通知签名数据
 *
 * @Author dxclay
 * @Date 2022/12/27
 * @Version 1.0
 */
@Getter
@Setter
public class Notification implements Serializable {

    private static final long serialVersionUID = -7375322316794874859L;

    /**
     * 通知id
     */
    private String notifyId;

    /**
     * 通知时间
     */
    private String notifyTime;

    /**
     * 通知类型 TRANSACTION: 交易， REFUND: 退款
     */
    private String notifyType;
    /**
     * 资源类型
     *   encrypt: 加密资源，需要使用秘钥进行解密
     *   decrypt: 未加密资源（安全性较低，仅用于内部系统）
     *   sign:    签名资源
     */
    private String resourceType;
    /**
     * 通知资源数据
     */
    private NotifyResource resource;

    @Getter
    @Setter
    public static class NotifyResource {
        /**
         * 加密算法
         */
        private String cipherType = "RSA";
        /**
         * 数据密文
         */
        private String cipherData;
        /**
         * 随机串, 32位长度
         */
        private String nonce;
        /**
         * 时间戳
         */
        private String timestamp;

        public NotifyResource() {
        }

        public NotifyResource(String cipherData, String timestamp) {
            this.cipherData = cipherData;
            this.timestamp = timestamp;
        }
    }

}
