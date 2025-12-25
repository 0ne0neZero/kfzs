package com.wishare.finance.infrastructure.support.lock;


import com.wishare.starter.Global;
import com.wishare.starter.helpers.RedisHelper;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @NAME: LockHelper
 * @Author: xujian
 * @Date: 2021/10/27
 */
public class LockHelper implements AutoCloseable {

    RLock rLock;
    RedissonMultiLock redissonMultiLock;

    public static LockHelper tryLock(Locker locker) {
        LockHelper lockHelper = new LockHelper();
        RedissonClient redissonClient = Global.ac.getBean(RedissonClient.class);
        lockHelper.rLock = redissonClient.getLock(locker.generateKey());
        lockHelper.rLock.lock(locker.getLeaseTime(), TimeUnit.SECONDS); // 默认加锁3分钟
        return lockHelper;
    }

    /**
     * 联合锁
     *
     * @param lockers
     * @return
     */
    public static LockHelper tryMultiLock(Locker... lockers) {
        LockHelper lockHelper = new LockHelper();
        RedissonClient redissonClient = Global.ac.getBean(RedissonClient.class);
        RLock[] rLocks = new RLock[lockers.length];
        for (int i = 0; i < lockers.length; i++) {
            rLocks[i] = redissonClient.getLock(lockers[i].generateKey());
        }
        lockHelper.redissonMultiLock = new RedissonMultiLock(rLocks);
        lockHelper.redissonMultiLock.lock(180, TimeUnit.SECONDS);
        return lockHelper;
    }


    @Override
    public void close()  {
        if (null != rLock) {
            try {
                rLock.unlock();
            } catch (Exception e) {
                // 解锁失败：可能是锁时间已过
                // TODO: 后续维护打印预警日志，以短信或者钉钉形式通知
            }
        }

        if (null != redissonMultiLock) {
            try {
                redissonMultiLock.unlock();
            } catch (Exception e) {
                // TODO: 后续维护打印预警日志，以短信或者钉钉形式通知
            }
        }
    }

    /**
     * 获取命名空间
     * @return
     */
    public String getNamespace(){
        return RedisHelper.nameSpace;
    }


}


