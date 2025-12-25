package com.wishare.finance.infrastructure.conts;

/**
 * reids key const
 */
public interface RedisConst {

    /** 目前： 方圆目前用来发起e签宝签署时候用来记录异常情况，用来轮询使用的时候可以针对处理 */
    //增加中交
    String SIGN = "sign";
    /** 远洋签署结果 */
    String YY_SIGN_RESULT = "yy_sign_result";
    /** 目前中交作废 */
    String ZF_SIGN = "zf_sign";


}
