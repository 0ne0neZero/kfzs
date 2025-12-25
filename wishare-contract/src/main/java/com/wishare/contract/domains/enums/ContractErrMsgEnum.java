package com.wishare.contract.domains.enums;

import com.wishare.starter.interfaces.ErrMsg;

/**
 * @author 永遇乐 yeoman <76164451@.qq.com>
 * @line --------------------------------
 * @date 2023/02/25
 */
public enum ContractErrMsgEnum implements ErrMsg {
    UNSUPPORTED_INVOICE_TYPE("不支持的开票类型"),

    CONTRACT_PROJECT_INITIATION_AMOUNT_EXCEEDS("当前立项金额超出成本系统合约规划管控金额");

    /**
     * 提示信息
     */
    private String msg;

    ContractErrMsgEnum(String msg) {
        this.msg = msg;
    }

    @Override
    public String msg() {
        return msg;
    }
}
