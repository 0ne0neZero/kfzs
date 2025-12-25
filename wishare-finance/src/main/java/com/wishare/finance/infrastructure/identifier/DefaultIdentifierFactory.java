package com.wishare.finance.infrastructure.identifier;

import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.starter.consts.CacheConst;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.helpers.UidHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Objects;
import java.util.Optional;

/**
 * 默认唯一标识工厂
 *
 * @Author dxclay
 * @Date 2022/12/29
 * @Version 1.0
 */
public class DefaultIdentifierFactory extends IdentifierFactory {

    private static final Logger log = LoggerFactory.getLogger(DefaultIdentifierFactory.class);

    private static final long EXPIRE_TIME = (24 * 60 * 60 * 1000) + 60000;
    private static final String SERIAL_KEY_PREFIX = "Serial:";
    private static final String IDENTIFIER_KEY = "Identifier";
    private static final String GLF = "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    @Override
    public Object generateIdentifier() {
        return generateIdentifier(IDENTIFIER_KEY, 2);
    }

    @Override
    public Object generateIdentifier(String key) {
        return generateIdentifier(key, 2);
    }

    @Override
    public Object generateIdentifier(String key, int digits) {
        return UidHelper.nextId(key, digits);
    }

    @Override
    public String serialNumber(String key, String prefix, int length) {
        Optional<String> optionalTenantId = ApiData.API.getTenantId();
        if (!optionalTenantId.isPresent()) {
            throw new IllegalArgumentException("分布式流水号异常-租户信息不存在！");
        }
        String tenantId = optionalTenantId.get();
        String tenantInfoJSON = RedisHelper.getG(CacheConst.TENANT + tenantId);
        if (Objects.isNull(tenantInfoJSON)) {
            throw new IllegalArgumentException("分布式流水号异常-登录无租户信息！");
        }
        TenantInfo tenantInfo = JSONObject.parseObject(tenantInfoJSON, TenantInfo.class);
        String englishName = tenantInfo.getEnglishName();
        if (Objects.isNull(englishName)){
            englishName = "";
        }
        String serialPrefix = englishName.toUpperCase() + prefix + DateTimeUtil.nowDateNoc();
        String cacheSerialKey = RedisHelper.nameSpace + SERIAL_KEY_PREFIX + tenantId + ":" + key +":"+ serialPrefix;
        //获取值
        Jedis jedis = RedisHelper.jedisPool.getResource();
        String serialNumber = serialPrefix;
        try {
            //设置流水号key
            jedis.set(cacheSerialKey, "0", new SetParams().nx().px(EXPIRE_TIME));
            long index = jedis.incr(cacheSerialKey);
            String incrStr = String.valueOf(index);
            //剩余可用长度
            int relSize = length - serialPrefix.length();
            if (relSize < 0){
                throw new IllegalArgumentException("分布式流水号异常-流水号长度租户前置长度大于流水号长度");
            }
            if (incrStr.length() >= relSize){
                serialNumber += incrStr;
            }else {
                String sreNumber = GLF + incrStr;
                serialNumber += sreNumber.substring(sreNumber.length() - relSize);
            }
        }finally {
            jedis.close();
        }
        return serialNumber;
    }


}
