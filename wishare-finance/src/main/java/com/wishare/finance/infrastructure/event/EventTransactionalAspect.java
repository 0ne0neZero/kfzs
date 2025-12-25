package com.wishare.finance.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 * 事件事务切面
 *
 * @Author dxclay
 * @Date 2022/12/26
 * @Version 1.0
 */
@Slf4j
@Component
@Aspect
public class EventTransactionalAspect {

    @Pointcut("@annotation(com.wishare.finance.infrastructure.event.EventTransactional)")
    public void pointcut(){}

    /**
     * 返回通知 用于拦截操作，在方法返回后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "pointcut()", returning="result")
    public void doReturning(JoinPoint joinPoint, Object result) {
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (synchronizationActive) { // 当前存在事务，在事务提交后执行
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() { // 监听事务提交完成
                            sendMessage(joinPoint);
                        }
                    }
            );
        } else {
            // 当前不存在事务，直接执行
            sendMessage(joinPoint);
        }

    }

    public void sendMessage(JoinPoint joinPoint) {
        try {
            EventTransactional eventTransactional = getEventTransactional(joinPoint);
            EventLifecycle.applyLocalMessages(eventTransactional.retry());
        }catch (Exception e){
            log.error("派发事件异常", e);
        }
    }

    /**
     * 前置处理
     * @param joinPoint
     */
    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint){
        log.debug("=========== 开启派发事件事务 =========");
        EventMessageHolder.getAndSet();
    }

    /**
     * 后置处理
     * @param joinPoint
     */
    @After("pointcut()")
    public void doAfter(JoinPoint joinPoint){
        log.debug("=========== 派发事件后置处理： 开始 =========");
        log.debug("=========== 派发事件后置处理： 结束 =========");
    }

    /**
     * 拦截异常操作，有异常时执行
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        log.debug("=========== 事件异常处理： 开始 =========");
        //清空所有的即将发布的消息
        try {
            EventTransactional eventTransactional = getEventTransactional(joinPoint);
            Class<? extends Throwable>[] rollbackExceptions = eventTransactional.rollbackFor();
            boolean isRollback = false;
            if (rollbackExceptions.length > 0){
                for (Class<? extends Throwable> rollbackException : rollbackExceptions) {
                    if (rollbackException.isAssignableFrom(e.getClass())){
                        isRollback = true;
                        break;
                    }
                }
            }else {
                isRollback = true;
            }
            if (isRollback){
                EventLifecycle.clearLocalMessages();
            }
        }catch (Exception e1){
            log.error("消息发布处理异常{}", e);
        }
        log.debug("=========== 事件异常处理： 结束 =========");
    }

    /**
     * 获取事务注解
     * @param joinPoint
     * @return
     */
    private EventTransactional getEventTransactional(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        EventTransactional annotation = method.getAnnotation(EventTransactional.class);
        if (annotation == null){
            Class<?> proceedClass = joinPoint.getTarget().getClass();
            if (proceedClass.isAnnotationPresent(EventTransactional.class)){
                annotation = proceedClass.getAnnotation(EventTransactional.class);
            }
        }
        return annotation;
    }

}
