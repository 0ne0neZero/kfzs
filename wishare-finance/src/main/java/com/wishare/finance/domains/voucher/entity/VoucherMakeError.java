package com.wishare.finance.domains.voucher.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 凭证预制错误信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
public class VoucherMakeError {

    /**
     * 错误信息
     */
    private List<String> messages = new ArrayList<>();

    public VoucherMakeError addMessage(String message){
        this.messages.add(message);
        return this;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
