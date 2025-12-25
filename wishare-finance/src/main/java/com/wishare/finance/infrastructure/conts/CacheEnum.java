package com.wishare.finance.infrastructure.conts;

/**
 * 缓存常量
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
public enum CacheEnum {

    TRADE_NO("tradeLock:"),

    PAYMENT_TRANSACTION("Payment:Transaction"),

    //银联对账文件
    YINLIAN_RECONCILIATION_FILE("yinlian:recon"),

    //通联对账文件
    TONGLIAN_RECONCILIATION_FILE("tonglian:recon"),
    ;
    private String dir;

    CacheEnum(String dir) {
        this.dir = dir;
    }

    public String getCacheKey(String key){
        return this.dir + ":" + key;
    }

}
