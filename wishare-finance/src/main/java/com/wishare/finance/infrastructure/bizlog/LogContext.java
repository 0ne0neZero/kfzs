package com.wishare.finance.infrastructure.bizlog;

import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.infrastructure.support.ApiData;

/**
 * 日志上下文
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/3
 */
public class LogContext {

    /**
     *
     * @return
     */
    public static Operator getOperator(){
        return new Operator(ApiData.API.getUserId().orElse("administrator"), ApiData.API.getUserName().orElse("系统默认"));
    }


}
