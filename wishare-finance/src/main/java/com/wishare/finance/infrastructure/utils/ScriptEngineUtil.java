package com.wishare.finance.infrastructure.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 脚本执行器工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/4
 */
public class ScriptEngineUtil {

    private static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("JavaScript");


    /**
     * 执行脚本
     * @param formula
     * @return
     * @throws ScriptException
     */
    public static Object eval(String formula) throws ScriptException {
        //执行语句安全校验
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null){
            securityManager.checkExec(formula);
        }
        return SCRIPT_ENGINE.eval(formula);
    }

}
