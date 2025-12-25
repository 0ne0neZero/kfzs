package com.wishare.finance.infrastructure.support.lock;

/**
 * 锁接口
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
public interface ILockerParam {

    /**
     * 锁前缀名
     * @return
     */
    String prefix();

    /**
     * 锁描述
     * @return
     */
    String desc();

    /**
     * 锁时间
     * @return
     */
    int defLeaseTime();

}
