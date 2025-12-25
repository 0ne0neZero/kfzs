package com.wishare.finance.apps.service.yuanyang;


import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
/**
 * @author szh
 * @date 2024/4/17 11:47
 */

@Slf4j
public class MasterRetryListener<Boolean> implements RetryListener {

    
    private final static String RETRY_LOG_PRE = "主从查询重试";
    @Override
    public <Boolean> void onRetry(Attempt<Boolean> attempt) {

        // 第几次重试,(注意:第一次重试其实是第一次调用)
        log.error(RETRY_LOG_PRE+"[retry]time={},delay={},,hasException={},hasResult={}" ,
                attempt.getAttemptNumber(),
                attempt.getDelaySinceFirstAttempt(),
                attempt.hasException(),
                attempt.hasResult()
        );


        // 是什么原因导致异常
        if (attempt.hasException()) {
            log.error(",causeBy=" + attempt.getExceptionCause().toString());
        } else {
            // 正常返回时的结果
            log.error(",result=" + attempt.getResult());
        }

        // bad practice: 增加了额外的异常处理代码
        try {
            Boolean result = attempt.get();
            log.error(",rude get=" + result);
        } catch (ExecutionException e) {
            log.error("this attempt produce exception." + e.getCause().toString());
        }


    }
}