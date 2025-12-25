package com.wishare.finance.infrastructure.support.lock;

/**
 * 锁异常
 *
 * @Author dxclay
 * @Date 2022/12/22
 * @Version 1.0
 */
public class LockException extends RuntimeException{

    public LockException() {
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }

    public LockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
