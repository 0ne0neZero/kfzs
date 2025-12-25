package com.wishare.finance.infrastructure.support.tenant;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.function.Supplier;

/**
 * 租户上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/17
 */
public class WishareTenantContext {

    private static final String IGNORE_STR = "igte";

    /**
     * 设置租户忽略
     * @param supplier 运行函数
     * @return 函数运行结果
     * @param <R> 函数运行参数
     */
    public static <R> R ignore(Supplier<R> supplier){
        initIgnoreWithRequest();
        R apply = supplier.get();
        WishareTenantHolder.clear();
        return apply;
    }

    /**
     * 设置租户忽略
     * @param ignoreStr 租户忽略
     * @param supplier 运行函数
     * @return 函数运行结果
     * @param <R> 函数运行参数
     */
    public static <R> R ignore(String ignoreStr, Supplier<R> supplier){
        WishareTenantHolder.setIgnore(ignoreStr);
        R apply = supplier.get();
        WishareTenantHolder.clear();
        return apply;
    }

    /**
     * 租户忽略
     * @return true：忽略 false：不忽略
     */
    public static boolean withIgnore(){
        return IGNORE_STR.equals(WishareTenantHolder.getIgnore());
    }

    /**
     * 加载请求忽略租户隔离
     */
    public static void initIgnoreWithRequest(){
        WishareTenantHolder.setIgnore(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getHeader(IGNORE_STR));
    }

}
