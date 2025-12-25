package com.wishare.finance.infrastructure.support.lock;

import com.wishare.starter.helpers.RedisHelper;

/**
 * 分布锁接口
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
public interface ILocker {


    /**
     * 获取命名空间
     * @return
     */
    default String getNamespace(){
        return RedisHelper.nameSpace;
    }

    /**
     * 生成锁的key
     * @return
     */
    String generateKey();
    /**
     * 生成锁的key
     * @return
     */
    String generateKey(String key);

}
