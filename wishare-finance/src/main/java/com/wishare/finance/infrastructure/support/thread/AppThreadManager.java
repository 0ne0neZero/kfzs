package com.wishare.finance.infrastructure.support.thread;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 应用线程管理器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/19
 */
public class AppThreadManager {

    public static final String DEFAULT_POOL = "GLOBAL_DEFAULT_POOL";

    /**
     * 线程池
     */
    private static final ConcurrentHashMap<String, AppExecutor> poolMap = new ConcurrentHashMap<>();

    static {
        poolMap.put(DEFAULT_POOL, new AppExecutor(20, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>()));
    }

    /**
     * 线程池
     * @return
     */
    public static AppExecutor executor(String pool){
        if (pool == null){
            return executor();
        }
        return poolMap.get(pool);
    }

    /**
     * 线程池
     * @return
     */
    public static AppExecutor executor(){
        return poolMap.get(DEFAULT_POOL);
    }

    /**
     * 使用默认线程执行
     * @param appRunnable
     */
    public static void execute(AppRunnable appRunnable){
        executor().execute(appRunnable);
    }

    /**
     * 使用默认线程执行
     * @param pool 线程池
     * @param appRunnable 执行器
     */
    public static void execute(String pool, AppRunnable appRunnable){
        executor(pool).execute(appRunnable);
    }


}
