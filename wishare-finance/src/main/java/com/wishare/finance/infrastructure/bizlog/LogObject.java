package com.wishare.finance.infrastructure.bizlog;

import com.wishare.bizlog.entity.BizObject;

/**
 * 日志对象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/1
 */
public enum LogObject implements BizObject {

    默认("finance-bill", ""),
    账单("finance-bill", "账单"),
    ;

    String objCode;
    String objName;

    LogObject(String objCode, String objName) {
        this.objCode = objCode;
        this.objName = objName;
    }

    @Override
    public String getBizCode() {
        return objCode;
    }

    @Override
    public String getObjName() {
        return objName;
    }
}
