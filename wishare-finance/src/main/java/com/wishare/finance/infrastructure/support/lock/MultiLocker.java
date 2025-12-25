package com.wishare.finance.infrastructure.support.lock;

import java.util.List;

/**
 * @NAME: Locker
 * @Author: xujian
 * @Date: 2021/10/27
 */
public class MultiLocker implements ILocker {

    private String filedName;

    private String desc;

    /**
     * 加锁时间：默认180秒
     */
    private int leaseTime = 180;

    /**
     * 加锁的值
     */
    private List<String> values;

    public MultiLocker() {
    }

    public MultiLocker(String filedName, String desc, int leaseTime, List<String> values) {
        this.filedName = filedName;
        this.desc = desc;
        this.leaseTime = leaseTime;
        this.values = values;
    }

    public static MultiLocker of(ILockerParam lockerParam, List<String> keys){
        return new MultiLocker(lockerParam.prefix(), lockerParam.desc(), lockerParam.defLeaseTime(), keys);
    }

    public static MultiLocker of(ILockerParam lockerParam, List<String> keys, int leaseTime){
        return new MultiLocker(lockerParam.prefix(), lockerParam.desc(), leaseTime, keys);
    }

    public String getFiledName() {
        return filedName;
    }

    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLeaseTime() {
        return leaseTime;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    /**
     * 获取系统过期锁时间
     * @return
     */
    public long getSystemLeaseTime(){
        return System.currentTimeMillis() + leaseTime * 1000;
    }

    public void setLeaseTime(int leaseTime) {
        this.leaseTime = leaseTime;
    }

    @Override
    public String generateKey() {
        return generateKey("");
    }

    @Override
    public String generateKey(String key) {
        return getNamespace() + "locker:" + filedName + ":" + key;
    }
}


