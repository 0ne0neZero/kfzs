package com.wishare.finance.infrastructure.utils;

import cn.hutool.core.util.StrUtil;
import com.wishare.starter.exception.ApiException;
import com.wishare.starter.exception.BizException;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @see com.wishare.starter.utils.ErrorAssertUtil
 */
public class ErrorAssertUtils {

    public static void ifTrueThrow300(boolean expression, String msg) {
        if (expression) {
            throw BizException.throw300(msg);
        }
    }
    /**
     * 检查资源为空，否则就抛业务异常
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     */
    public static void isNullThrow300(Object obj, String msg) {
        if (obj != null) {
            throw BizException.throw300(msg);
        }
    }

    /**
     * 检查资源不为空，否则就抛业务异常
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     */
    public static void notNullThrow300(Object obj, String msg) {
        if (obj == null) {
            throw BizException.throw300(msg);
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isTrueThrow300(boolean expression, String msg) {
        if (!expression) {
            throw BizException.throw300(msg);
        }
    }


    /**
     * 检查表达式结果为假，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isFalseThrow300(boolean expression, String msg) {
        if (expression) {
            throw BizException.throw300(msg);
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isFalseThrow300(boolean expression, String msg, Object... arguments) {
        if (expression) {
            throw BizException.throw300(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查资源为空，否则就抛操作未生效
     *
     * @param obj 检查对象
     */
    public static void isNullThrow301(Object obj) {
        if (obj != null) {
            throw BizException.throw301();
        }
    }

    /**
     * 检查资源为空，否则就抛业务异常
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     */
    public static void isNullThrow301(Object obj, String msg) {
        if (obj != null) {
            throw BizException.throw301(msg);
        }
    }

    /**
     * 检查资源不为空，否则就抛操作未生效
     *
     * @param obj 检查对象
     */
    public static void notNullThrow301(Object obj) {
        if (obj == null) {
            throw BizException.throw301();
        }
    }

    /**
     * 检查资源不为空，否则就抛业务异常
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     */
    public static void notNullThrow301(Object obj, String msg) {
        if (obj == null) {
            throw BizException.throw301(msg);
        }
    }


    /**
     * 检查表达式结果为真，否则就抛操作未生效
     *
     * @param expression 条件表达式
     */
    public static void isTrueThrow301(boolean expression) {
        if (!expression) {
            throw BizException.throw301();
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isTrueThrow301(boolean expression, String msg) {
        if (!expression) {
            throw BizException.throw301(msg);
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isTrueThrow301(boolean expression, String msg, Object... arguments) {
        if (!expression) {
            throw BizException.throw301(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查表达式结果为假，否则就抛操作未生效
     *
     * @param expression 条件表达式
     */
    public static void isFalseThrow301(boolean expression) {
        if (expression) {
            throw BizException.throw301();
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isFalseThrow301(boolean expression, String msg) {
        if (expression) {
            throw BizException.throw301(msg);
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isFalseThrow301(boolean expression, String msg, Object... arguments) {
        if (expression) {
            throw BizException.throw301(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 抛参数错误
     *
     * @param msg 提示信息定义
     */
    public static void throw400(String msg) {
        throw BizException.throw400(msg);
    }


    /**
     * 检查资源为空，否则就抛参数错误
     *
     * @param text 检查的字符
     * @param msg  提示信息定义
     */
    public static void isEmptyThrow400(String text, String msg) {
        if (StringUtils.hasText(text)) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 检查资源为空，否则就抛参数错误
     *
     * @param text      检查的字符
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void isEmptyThrow400(String text, String msg, Object... arguments) {
        if (StringUtils.hasText(text)) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查资源不为空，否则就抛参数错误
     *
     * @param text 检查的字符
     * @param msg  提示信息定义
     */
    public static void notEmptyThrow400(String text, String msg) {
        if (!StringUtils.hasText(text)) {
            throw BizException.throw400(msg);
        }
    }

    public static void notBlankThrow400(String text, String msg) {
        if (StrUtil.isBlank(text)) {
            throw BizException.throw400(msg);
        }
    }
    /**
     * 检查资源不为空，否则就抛参数错误
     *
     * @param text      检查的字符
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void notEmptyThrow400(String text, String msg, Object... arguments) {
        if (!StringUtils.hasText(text)) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查集合为空，否则就抛参数错误
     *
     * @param collection 检查的集合
     * @param msg        提示信息定义
     */
    public static void isEmptyThrow400(Collection<?> collection, String msg) {
        if (Objects.nonNull(collection) && !collection.isEmpty()) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 检查集合为空，否则就抛参数错误
     *
     * @param collection 检查的集合
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isEmptyThrow400(Collection<?> collection, String msg, Object... arguments) {
        if (Objects.nonNull(collection) && !collection.isEmpty()) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查集合不为空，否则就抛参数错误
     *
     * @param collection 检查的集合
     * @param msg        提示信息定义
     */
    public static void notEmptyThrow400(Collection<?> collection, String msg) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 检查集合不为空，否则就抛参数错误
     *
     * @param collection 检查的集合
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void notEmptyThrow400(Collection<?> collection, String msg, Object... arguments) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查Map集合为空，否则就抛参数错误
     *
     * @param map 检查的Map集合
     * @param msg 提示信息定义
     */
    public static void isEmptyThrow400(Map<?, ?> map, String msg) {
        if (Objects.nonNull(map) && !map.isEmpty()) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 检查Map集合为空，否则就抛参数错误
     *
     * @param map       检查的Map集合
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void isEmptyThrow400(Map<?, ?> map, String msg, Object... arguments) {
        if (Objects.nonNull(map) && !map.isEmpty()) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查Map集合不为空，否则就抛参数错误
     *
     * @param map 检查的Map集合
     * @param msg 提示信息定义
     */
    public static void notEmptyThrow400(Map<?, ?> map, String msg) {
        if (Objects.isNull(map) || map.isEmpty()) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 检查Map集合不为空，否则就抛参数错误
     *
     * @param map       检查的Map集合
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void notEmptyThrow400(Map<?, ?> map, String msg, Object... arguments) {
        if (Objects.isNull(map) || map.isEmpty()) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查表达式结果为真，否则就抛参数错误异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isTrueThrow400(boolean expression, String msg) {
        if (!expression) {
            throw BizException.throw400(msg);
        }
    }


    /**
     * 成立 抛异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isFalseThrow400(boolean expression, String msg) {
        if (expression) {
            throw BizException.throw400(msg);
        }
    }

    /**
     * 成立 抛异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isThrow400(String msg) {
        isFalseThrow400(true,msg);
    }


    /**
     * 检查表达式结果为真，否则就抛参数错误异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isTrueThrow400(boolean expression, String msg, Object... arguments) {
        if (!expression) {
            throw BizException.throw400(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     */
    public static void isTrueThrow402(boolean expression) {
        if (!expression) {
            throw BizException.throw402();
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isTrueThrow402(boolean expression, String msg) {
        if (!expression) {
            throw BizException.throw402(msg);
        }
    }

    /**
     * 检查表达式结果为真，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isTrueThrow402(boolean expression, String msg, Object... arguments) {
        if (!expression) {
            throw BizException.throw402(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     */
    public static void isFalseThrow402(boolean expression) {
        if (expression) {
            throw BizException.throw402();
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     */
    public static void isFalseThrow402(boolean expression, String msg) {
        if (expression) {
            throw BizException.throw402(msg);
        }
    }

    /**
     * 检查表达式结果为假，否则就抛业务限制异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isFalseThrow402(boolean expression, String msg, Object... arguments) {
        if (expression) {
            throw BizException.throw402(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查表达式结果为真，否则就抛限制异常
     *
     * @param expression 条件表达式
     * @param msg        提示信息定义
     * @param arguments  可拼接参数
     */
    public static void isTrueThrow403(boolean expression, String msg, Object... arguments) {
        if (!expression) {
            throw ApiException.throw403(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查资源不为空，否则就抛限制异常
     *
     * @param obj       检查对象
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void notNullThrow403(Object obj, String msg, Object... arguments) {
        if (obj == null) {
            throw ApiException.throw403(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查资源为空，否则就抛资源不存在
     *
     * @param obj 检查对象
     * @deprecated 从1.5.6开始，建议改用方法 {@link #notNullThrow404(Object)}
     */
    @Deprecated
    public static void isNullThrow404(Object obj) {
        if (obj != null) {
            throw BizException.throw404();
        }
    }

    /**
     * 检查资源为空，否则就抛资源不存在
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     * @deprecated 从1.5.6开始，建议改用方法 {@link #notNullThrow404(Object, String)}
     */
    @Deprecated
    public static void isNullThrow404(Object obj, String msg) {
        if (obj != null) {
            throw BizException.throw404(msg);
        }
    }

    /**
     * 检查资源为空，否则就抛资源不存在
     *
     * @param obj       检查对象
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     * @deprecated 从1.5.6开始，建议改用方法 {@link #notNullThrow404(Object, String, Object...)}
     */
    @Deprecated
    public static void isNullThrow404(Object obj, String msg, Object... arguments) {
        if (obj != null) {
            throw BizException.throw404(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 检查资源不为空，否则就抛资源不存在
     *
     * @param obj 检查对象
     */
    public static void notNullThrow404(Object obj) {
        if (obj == null) {
            throw BizException.throw404();
        }
    }

    /**
     * 检查资源不为空，否则就抛资源不存在
     *
     * @param obj 检查对象
     * @param msg 提示信息定义
     */
    public static void notNullThrow404(Object obj, String msg) {
        if (obj == null) {
            throw BizException.throw404(msg);
        }
    }

    /**
     * 检查资源不为空，否则就抛资源不存在
     *
     * @param obj       检查对象
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void notNullThrow404(Object obj, String msg, Object... arguments) {
        if (obj == null) {
            throw BizException.throw404(MessageFormat.format(msg, arguments));
        }
    }

    /**
     * 验证请求头中的前置条件不为空，否则验证失败
     *
     * @param obj       检查对象
     * @param msg       提示信息定义
     * @param arguments 可拼接参数
     */
    public static void notNullThrow412(Object obj, String msg, Object... arguments) {
        if (obj == null) {
            throw BizException.throw404(MessageFormat.format(msg, arguments));
        }
    }
}
