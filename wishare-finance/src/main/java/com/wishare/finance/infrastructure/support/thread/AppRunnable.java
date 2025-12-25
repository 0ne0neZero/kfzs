package com.wishare.finance.infrastructure.support.thread;

import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;

/**
 * 应用线程
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/19
 */
public abstract class AppRunnable implements Runnable {

    private IdentityInfo identityInfo;

    public AppRunnable() {
        identityInfo = ThreadLocalUtil.curIdentityInfo();
    }

    /**
     * 执行
     */
    public abstract void execute();


    @Override
    public void run() {
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
        execute();
    }
}
