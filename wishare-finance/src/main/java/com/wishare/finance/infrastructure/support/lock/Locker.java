package com.wishare.finance.infrastructure.support.lock;

/**
 * @NAME: Locker
 * @Author: xujian
 * @Date: 2021/10/27
 */
public class Locker implements ILocker{

    private String filedName;

    private String desc;

    /**
     * 加锁时间：默认180秒
     */
    private int leaseTime = 180;

    /**
     * 加锁的值
     */
    private String value;

    public Locker() {
    }

    public Locker(String filedName, String desc, int leaseTime) {
        this.filedName = filedName;
        this.desc = desc;
        this.leaseTime = leaseTime;
    }


    private Locker(String filedName, String desc, int leaseTime, String value) {
        this.filedName = filedName;
        this.desc = desc;
        this.leaseTime = leaseTime;
        this.value = value;
    }

    public static Locker of(ILockerParam lockerParam, String key){
        return new Locker(lockerParam.prefix(), lockerParam.desc(), lockerParam.defLeaseTime(), key);
    }

    public static Locker of(ILockerParam lockerParam, String key, int leaseTime){
        return new Locker(lockerParam.prefix(), lockerParam.desc(), leaseTime, key);
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String generateKey() {
        return generateKey(value);
    }

    @Override
    public String generateKey(String key) {
        return getNamespace() + "locker:" + filedName + ":" + key;
    }


}


