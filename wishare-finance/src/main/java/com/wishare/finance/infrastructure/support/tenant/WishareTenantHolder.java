package com.wishare.finance.infrastructure.support.tenant;

import java.util.Objects;

/**
 * 租户隔离数据持有者
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/17
 */
public class WishareTenantHolder {

    private static final ThreadLocal<String> ignoreFlag = new ThreadLocal<>();

    /**
     * 获取租户忽略
     * @return 租户忽略
     */
    public static String getIgnore(){
        return ignoreFlag.get();
    }

    /**
     * 设置租户忽略
     * @param ignore 租户忽略
     */
    public static void setIgnore(String ignore){
        if (Objects.nonNull(ignore)){
            ignoreFlag.set(ignore);
        }
    }

    /**
     * 清楚租户忽略信息
     */
    public static void clear(){
        ignoreFlag.remove();
    }

}
