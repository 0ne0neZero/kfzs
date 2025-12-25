package com.wishare.finance.infrastructure.support;

import com.wishare.starter.helpers.UidHelper;

import java.util.UUID;

/**
 * 序列号生成工具类
 *
 * @author: Dxclay
 * @date: 2021/12/30
 */
public class AppIdentifierFactory {


    private AppIdentifierFactory(){}

    /**
     * 生成UUID
     * @return
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成字符串型UID
     * @param key
     * @return
     */
    public static String generateStrIdentifier(String key){
        return UidHelper.nextIdStr(key, 4);
    }

    /**
     * 生成long UID
     * @param key
     * @return
     */
    public static Long generateLongIdentifier(String key){
        return UidHelper.nextId(key, 4);
    }

}
