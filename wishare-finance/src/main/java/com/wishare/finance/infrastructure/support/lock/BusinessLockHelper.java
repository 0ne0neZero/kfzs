package com.wishare.finance.infrastructure.support.lock;

import com.wishare.starter.helpers.RedisHelper;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务锁
 * 业务在处理过程中需要将业务进行锁定，防止业务在执行过程中相同的业务数据被操作
 *
 * @Author dxclay
 * @Date 2022/12/20
 * @Version 1.0
 */
public class BusinessLockHelper {

    private Locker locker;

    private MultiLocker multiLocker;

    /**
     * 单个加锁
     *
     * @param locker
     */
    public static void tryLock(Locker locker) {
        BusinessLockHelper businessLockHelper = new BusinessLockHelper();
        businessLockHelper.locker = locker;
        if (!businessLockHelper.lock()) {
            //业务锁加锁失败则抛出异常
            throw new IllegalStateException("请勿重复操作");
        }
    }

    /**
     * 多个加锁
     *
     * @param locker
     */
    public static void tryMultiLock(MultiLocker locker) {
        BusinessLockHelper businessLockHelper = new BusinessLockHelper();
        businessLockHelper.multiLocker = locker;
        if (!businessLockHelper.multiLock()) {
            throw new LockException("请勿重复操作");
        }
    }

    /**
     * 解锁
     *
     * @param locker
     */
    public static void unLock(Locker locker) {
        BusinessLockHelper businessLockHelper = new BusinessLockHelper();
        businessLockHelper.locker = locker;
        businessLockHelper.unLock();
    }

    /**
     * 解锁
     *
     * @param locker
     */
    public static void unLock(MultiLocker locker) {
        BusinessLockHelper businessLockHelper = new BusinessLockHelper();
        businessLockHelper.multiLocker = locker;
        businessLockHelper.multiUnLock();
    }


    /**
     * 获取命名空间
     *
     * @return
     */
    public String getNamespace() {
        return RedisHelper.nameSpace;
    }


    /**
     * 单个加锁
     *
     * @return
     */
    private boolean lock() {
        boolean res;
        Jedis jedis = RedisHelper.jedisPool.getResource();
        try {
            res = doLock(jedis, locker.generateKey(), String.valueOf(locker.getSystemLeaseTime()));
        } finally {
            jedis.close();
        }
        return res;
    }

    /**
     * 多个上锁
     *
     * @return
     */
    private boolean multiLock() {
        List<String> lockKeys = new ArrayList<>();
        Jedis jedis = RedisHelper.jedisPool.getResource();
        try {
            String leaseTime = String.valueOf(multiLocker.getSystemLeaseTime());
            for (String k : multiLocker.getValues()) {
                String lockKey = multiLocker.generateKey(k);
                if (doLock(jedis, lockKey, leaseTime)) {
                    lockKeys.add(lockKey);
                } else {
                    //如果存在一个枷锁失败，则解除当前已上的所有的锁
                    doMultiUnLock(lockKeys);
                    return false;
                }
            }
        } finally {
            jedis.close();
        }
        return true;
    }

    /**
     * 解锁
     *
     * @return
     */
    private void unLock() {
        doUnLock(locker.generateKey());
    }

    /**
     * 解锁
     *
     * @return
     */
    private void doUnLock(String key) {
        Jedis jedis = RedisHelper.jedisPool.getResource();
        try {
            jedis.del(key);
        }finally {
            jedis.close();
        }
    }

    /**
     * 解锁
     *
     * @return
     */
    private void multiUnLock() {
        doMultiUnLock(multiLocker.getValues());
    }

    /**
     * 解锁
     *
     * @return
     */
    private void doMultiUnLock(List<String> lockKeys) {
        lockKeys.forEach(k -> {
            doUnLock(multiLocker.generateKey(k));
        });
    }

    /**
     * 上锁
     *
     * @param jedis     jedis对象
     * @param lockKey   上锁的key
     * @param leaseTime 过期时间戳（long类型）
     * @return
     */
    private boolean doLock(Jedis jedis, String lockKey, String leaseTime) {
        //锁不存在则获取锁
        if (jedis.setnx(lockKey, leaseTime) == 1) {
            return true;
        }
        //如果锁已经存在，获取锁的过期时间
        String systemLeaseTime = jedis.get(lockKey);
        if (systemLeaseTime != null && Long.parseLong(systemLeaseTime) < System.currentTimeMillis()) {
            //获取旧锁时间，并设置新锁
            String oldTime = jedis.getSet(lockKey, leaseTime);
            //判断是否当前锁是否是同一个锁，保证并发情况下只有一个线程被锁
            if (oldTime != null && oldTime.equals(systemLeaseTime)) {
                return true;
            }
        }
        return false;
    }


}
